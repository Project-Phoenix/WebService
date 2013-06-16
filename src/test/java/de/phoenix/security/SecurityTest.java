/*
 * Copyright (C) 2013 Project-Phoenix
 * 
 * This file is part of WebService.
 * 
 * WebService is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3 of the License.
 * 
 * WebService is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with WebService.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.phoenix.security;

import static org.junit.Assert.assertTrue;

import org.apache.commons.codec.digest.DigestUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.ClientResponse.Status;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.container.httpserver.HttpServerFactory;
import com.sun.net.httpserver.HttpServer;

public class SecurityTest {

    private final static String BASE_URL = "http://localhost:7766/rest";

    private static HttpServer httpServer;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        // Start Http Server
        httpServer = HttpServerFactory.create(BASE_URL);
        httpServer.start();
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        httpServer.stop(0);
    }

    @Test
    public void APITest() {

        // User information
        String user = "account";
        String password = "password";

        Client client = Client.create();

        // Create a new account
        WebResource createAccountRes = client.resource(BASE_URL).path("account").path("create");

        ClientResponse response = createAccountRes.path(user).path(DigestUtils.sha512Hex(password)).get(ClientResponse.class);

        assertTrue(response.toString(), response.getClientResponseStatus().equals(Status.OK));

        // Request a token - also check if the account is valid
        WebResource requestTokenRes = client.resource(BASE_URL).path("token").path("request");
        requestTokenRes.addFilter(new LoginFilter(user, password));
        response = requestTokenRes.get(ClientResponse.class);
        requestTokenRes.removeAllFilters();

        assertTrue(response.toString(), response.getClientResponseStatus().equals(Status.OK));

        // Validate the given token
        Token token = response.getEntity(Token.class);
        WebResource validateTokenRes = client.resource(BASE_URL).path("token").path("validate");
        client.addFilter(new TokenFilter(token));

        response = validateTokenRes.get(ClientResponse.class);

        assertTrue(response.toString(), response.getClientResponseStatus().equals(Status.OK));
    }
}
