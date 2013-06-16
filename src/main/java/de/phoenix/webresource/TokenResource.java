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
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.sun.jersey.core.util.Base64;

import de.phoenix.PhoenixApplication;

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
        // Check if the user is valid
        if (PhoenixApplication.accountManager.validateUser(headers)) {
            return Response.ok(PhoenixApplication.tokenManager.generateToken(extractUsername(headers))).build();
        } else {
            return Response.status(Status.UNAUTHORIZED).build();
        }
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
        if (PhoenixApplication.tokenManager.isValidToken(headers))
            return Response.ok().build();
        else
            return Response.status(Status.UNAUTHORIZED).build();
    }

    private String extractUsername(HttpHeaders headers) {
        // extract Value behind Authorization head from request
        String base64String = headers.getRequestHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        // decode base64 string
        base64String = base64String.substring("Basic ".length());
        base64String = Base64.base64Decode(base64String);

        return base64String.substring(0, base64String.indexOf(':'));
    }

}
