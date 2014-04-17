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

package de.phoenix.debug;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.joda.time.DateTime;

import de.phoenix.database.DatabaseManager;
import de.phoenix.database.entity.DebugLog;

@Provider
public class MyExceptionMapper implements ExceptionMapper<Throwable> {

    @Override
    public Response toResponse(Throwable exception) {

        StringWriter sw = new StringWriter();
        PrintWriter ps = new PrintWriter(sw);
        exception.printStackTrace(ps);
        String message = sw.toString();
        ps.close();

        Session session = DatabaseManager.getSession();
        try {
            DebugLog log = new DebugLog(message, DateTime.now());
            Transaction trans = session.beginTransaction();
            session.save(log);
            trans.commit();
        } catch (Exception e) {
        } finally {
            if (session != null)
                session.close();
        }

        if (exception instanceof WebApplicationException)
            return ((WebApplicationException) exception).getResponse();
        else {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(message).build();
        }
    }

}
