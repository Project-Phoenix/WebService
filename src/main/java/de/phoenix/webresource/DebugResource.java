/*
 * Copyright (C) 2014 Project-Phoenix
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

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;

import de.phoenix.database.DatabaseManager;
import de.phoenix.database.entity.DebugLog;

@Path("debug")
public class DebugResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getLog() {

        Session session = DatabaseManager.getSession();
        @SuppressWarnings("unchecked")
        List<Object> list = session.createCriteria(DebugLog.class).addOrder(Order.desc("date")).list();

        StringBuilder sBuilder = new StringBuilder();
        for (Object object : list) {
            sBuilder.append(object.toString());
            sBuilder.append('\n');
        }

        session.close();

        return Response.ok(sBuilder.toString()).build();
    }

    @Path("delete")
    @GET
    public Response delete() {

        Session session = DatabaseManager.getSession();
        Transaction trans = session.beginTransaction();
        session.createSQLQuery("DELETE FROM debugLog").executeUpdate();
        trans.commit();
        session.close();
        return Response.ok().build();
    }
}
