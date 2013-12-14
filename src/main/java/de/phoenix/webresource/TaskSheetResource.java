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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import de.phoenix.database.DatabaseManager;
import de.phoenix.database.entity.Task;
import de.phoenix.database.entity.TaskSheet;
import de.phoenix.database.entity.util.ConverterArrayList;
import de.phoenix.rs.entity.PhoenixTask;
import de.phoenix.rs.entity.PhoenixTaskSheet;

@Path("/" + PhoenixTaskSheet.WEB_RESOURCE_ROOT)
public class TaskSheetResource {

    @Path("/" + PhoenixTaskSheet.WEB_RESOURCE_CREATE)
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createTaskSheet(PhoenixTaskSheet phoenixSheet) {

        Session session = DatabaseManager.getSession();

        try {
            Transaction trans = session.beginTransaction();
            List<Task> tasks = new ArrayList<Task>(phoenixSheet.getTasksSize());
            Query findTask = session.getNamedQuery("Task.findByName");

            for (PhoenixTask pTask : phoenixSheet.getTasks()) {
                findTask.setParameter("title", pTask.getTitle()).uniqueResult();
                tasks.add((Task) findTask.uniqueResult());
            }

            TaskSheet taskSheet = new TaskSheet();
            taskSheet.setTasks(tasks);
            taskSheet.setCreationDate(new Date());

            session.save(taskSheet);
            trans.commit();

            return Response.ok().build();

        } finally {
            if (session != null)
                session.close();
        }
    }

    @SuppressWarnings("unchecked")
    @Path("/" + PhoenixTaskSheet.WEB_RESOURCE_GETALL)
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllSheets() {
        Session session = DatabaseManager.getSession();

        try {
            List<TaskSheet> sheets = session.getNamedQuery("TaskSheet.findAll").list();

            List<PhoenixTaskSheet> result = new ConverterArrayList<PhoenixTaskSheet>(sheets);
            session.close();

            return Response.ok(PhoenixTaskSheet.toSendableList(result)).build();

        } finally {
            if (session != null)
                session.close();
        }
    }
}
