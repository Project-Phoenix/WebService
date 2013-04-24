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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.sun.jersey.core.util.Base64;

import de.phoenix.PhoenixApplication;

@Path("/account")
public class AccountResource {

    @Deprecated
    @GET
    @Path("/create/{username}/{password}")
    public Response createAccountOld(@PathParam("username") String username, @PathParam("password") String password) {
        PhoenixApplication.accountManager.createUser(username, password);
        return Response.ok().build();
    }

    @GET
    @Path("/create")
    public Response createAccount(@Context HttpHeaders headers) {
        String[] info = extractInformation(headers);
        if (info == null)
            return Response.status(Status.BAD_REQUEST).build();

        PhoenixApplication.accountManager.createUser(info[0], info[1]);
        return Response.ok().build();
    }

    private String[] extractInformation(HttpHeaders headers) {
        String[] res = new String[2];
        String base64String = headers.getRequestHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        // Not authorization head in the request
        if (base64String == null)
            return null;

        // decode base64 string
        base64String = base64String.substring("Basic ".length());
        base64String = Base64.base64Decode(base64String);
        // Split at ':' as defined for HTTPBasicAuthentification
        int pos = base64String.indexOf(':');
        // Not found
        if (pos == -1)
            return null;;
        // extract values
        res[0] = base64String.substring(0, pos);
        res[1] = base64String.substring(pos + 1);
        return res;
    }

}
