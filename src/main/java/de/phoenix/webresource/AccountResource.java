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

import java.util.Date;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.hibernate.Session;
import org.hibernate.Transaction;

import de.phoenix.PhoenixApplication;
import de.phoenix.database.entity.Role;
import de.phoenix.database.entity.User;
import de.phoenix.security.Encrypter;
import de.phoenix.security.LoginFilter;
import de.phoenix.security.SaltedPassword;

@Path("/account")
public class AccountResource {

    @Deprecated
    @GET
    @Path("/create/{username}/{password}")
    public Response createAccountOld(@PathParam("username") String username, @PathParam("password") String password) {
//        PhoenixApplication.accountManager.createUser(username, password);
        return Response.ok().build();
    }

    // TODO: Depecrated after 31.05.2013
    // Just a method for simple tests
    @GET
    @Path("/create")
    public Response createAccount(@Context HttpHeaders headers) {

        // Extract username and sha512 encoded password from head
        MultivaluedMap<String, String> requestHeaders = headers.getRequestHeaders();
        String username = requestHeaders.getFirst(LoginFilter.NAME_HEAD);
        String password = requestHeaders.getFirst(LoginFilter.PASS_HEAD);
        // Heads are missing - invalid request
        if (username == null || password == null)
            return Response.status(Status.BAD_REQUEST).build();

        // Persist user in database
        Session session = PhoenixApplication.databaseManager.openSession();
        Transaction transaction = session.beginTransaction();
        // Generate salted password
        SaltedPassword pw = Encrypter.getInstance().encryptPassword(password);
        User user = new User();

        // TODO: Outdated code after 31.05.2013
        user.setUsername(username);
        user.setPassword(pw.getHash());
        user.setSalt(pw.getSalt());
        user.setSurname("Hans");
        user.setName("Maier");
        user.setTitle("Herr");
        user.setEmail("Test@lol.de");
        user.setRegdate(new Date());
        user.setIsActive(true);
        // TODO: / Outdated code after 31.05.2013

        Role role = (Role) session.getNamedQuery("Role.findById").setInteger("id", 1).uniqueResult();
        user.setRole(role);

        session.save(user);
        transaction.commit();

        return Response.ok().build();
    }
}
