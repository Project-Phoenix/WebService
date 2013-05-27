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

package de.phoenix.webresource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.hibernate.Session;

import de.phoenix.PhoenixApplication;
import de.phoenix.database.entity.User;
import de.phoenix.security.Encrypter;
import de.phoenix.security.LoginFilter;
import de.phoenix.security.Token;
import de.phoenix.security.TokenManager;

/**
 * A webresource for requesting tokens
 */
@Path("/token")
public class TokenResource {

    /**
     * Create a new token for the user. The HttpHeaders have to have the
     * Authorization Basic attribute for this webresource!
     * 
     * @param headers
     *            The HttpHeaders containing the Authorization Basic attribute
     * @return A validate token when the login was ok
     */
    @GET
    @Produces(MediaType.APPLICATION_XML)
    @Path("/request")
    public Response requestToken(@Context HttpHeaders headers) {

        // Extract username and sha512 encoded password from head
        MultivaluedMap<String, String> requestHeaders = headers.getRequestHeaders();
        String username = requestHeaders.getFirst(LoginFilter.NAME_HEAD);
        String password = requestHeaders.getFirst(LoginFilter.PASS_HEAD);
        // Heads are missing - invalid request
        if (username == null || password == null)
            return Response.status(Status.BAD_REQUEST).build();

        // Check if password in database the same as in the request
        Session session = PhoenixApplication.databaseManager.openSession();
        User user = (User) session.getNamedQuery("User.findByUsername").setString("username", username).iterate().next();
        session.close();
        // Passwords don't match
        if (!Encrypter.getInstance().validatePassword(password, user.getPassword(), user.getSalt())) {
            return Response.status(Status.FORBIDDEN).build();
        }

        // Generate token
        TokenManager tM = TokenManager.getInstance();
        if (tM == null) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
        Token token = tM.generateToken(user.getUsername());
        if (token == null) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }

        return Response.ok(token).build();
    }

    /**
     * Test method to check if a generated token is valid
     * 
     * @param headers
     *            The HttpHeaders containg the TokenHead attribute
     * @return 200 OK when the token is valid, otherwise the 401 UNAUTORIZED
     */
    @GET
    @Path("validate")
    public Response validateToken(@Context HttpHeaders headers) {
        TokenManager tM = TokenManager.getInstance();
        if (tM == null) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
        if (tM.isValidToken(headers))
            return Response.ok().build();
        else
            return Response.status(Status.UNAUTHORIZED).build();
    }
}
