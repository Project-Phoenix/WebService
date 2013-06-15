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
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import de.phoenix.PhoenixApplication;
import de.phoenix.security.Token;

/**
 * A webresource for requesting tokens
 */
@Path("/token")
public class TokenResource {

    @GET
    @Produces(MediaType.APPLICATION_XML)
    @Path("/request/{username}")
    public Token requestToken(@Context HttpHeaders headers, @PathParam("username") String username) {
        // TODO: Validate the user
        return PhoenixApplication.tokenManager.generateToken(username);
    }

}
