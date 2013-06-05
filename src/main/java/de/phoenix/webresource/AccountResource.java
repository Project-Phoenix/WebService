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
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.hibernate.Session;
import org.hibernate.Transaction;

import de.phoenix.database.DatabaseManager;
import de.phoenix.database.entity.Role;
import de.phoenix.database.entity.User;
import de.phoenix.security.Encrypter;
import de.phoenix.security.SaltedPassword;

@Path("/account")
public class AccountResource {

    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_XML)
    public Response register(User user) {

        // User cannot be null
        if (user == null) {
            return Response.status(Status.BAD_REQUEST).build();
        }

        String password = user.getPassword();
        // Invalid password
        if (password == null) {
            return Response.status(Status.BAD_REQUEST).build();
        }
        // Salt the password
        SaltedPassword pw = Encrypter.getInstance().encryptPassword(password);
        // Set password data
        user.setPassword(pw.getHash());
        user.setSalt(pw.getSalt());

        // Set automatic values manually - override things set by client
        user.setRegdate(new Date());
        user.setId(null);
        user.setIsActive(true);
        // Save user in database
        Session session = DatabaseManager.getInstance().openSession();
        Transaction transaction = session.beginTransaction();

        // Check if username is duplicate
        @SuppressWarnings("rawtypes")
        List res = session.getNamedQuery("User.findByUsername").setString("username", user.getUsername()).list();
        if (!res.isEmpty()) {
            return Response.status(Status.BAD_REQUEST).entity("Duplicate username").build();
        }
        // TODO: Get standard role from database
        Role role = (Role) session.getNamedQuery("Role.findById").setInteger("id", 1).uniqueResult();
        user.setRole(role);

        session.save(user);
        transaction.commit();

        return Response.ok().build();
    }

}
