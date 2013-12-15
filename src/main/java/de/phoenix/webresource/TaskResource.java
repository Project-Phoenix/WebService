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

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Projections;

import de.phoenix.database.DatabaseManager;
import de.phoenix.database.entity.Attachment;
import de.phoenix.database.entity.Task;
import de.phoenix.database.entity.Text;
import de.phoenix.database.entity.util.ConverterArrayList;
import de.phoenix.rs.entity.PhoenixAttachment;
import de.phoenix.rs.entity.PhoenixAutomaticTask;
import de.phoenix.rs.entity.PhoenixTask;
import de.phoenix.rs.entity.PhoenixText;
import de.phoenix.util.RSLists;
import de.phoenix.util.Updateable;

@Path("/" + PhoenixTask.WEB_RESOURCE_ROOT)
public class TaskResource {

    @Path("/" + PhoenixTask.WEB_RESOURCE_CREATE)
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(PhoenixTask phoenixTask) {

        boolean isAutomaticTask = (phoenixTask instanceof PhoenixAutomaticTask);

        Session session = DatabaseManager.getSession();
        try {
            Transaction trans = session.beginTransaction();

            Task tmp = (Task) session.getNamedQuery("Task.findByTitle").setString("title", phoenixTask.getTitle()).uniqueResult();

            // Name already exists - No duplicates allowed
            if (tmp != null) {
                return Response.status(Status.BAD_REQUEST).entity("Duplicate name for " + phoenixTask.getTitle() + "!").build();
            }

            List<Attachment> attachments = new ArrayList<Attachment>(phoenixTask.getAttachmentsSize());
            for (PhoenixAttachment attachment : phoenixTask.getAttachments()) {
                Attachment at = new Attachment(attachment);
                Integer id = (Integer) session.save(at);
                at.setId(id);

                attachments.add(at);
            }

            List<Text> texts = new ArrayList<Text>(phoenixTask.getPatternSize());
            for (PhoenixText text : phoenixTask.getPattern()) {
                Text te = new Text(text);
                Integer id = (Integer) session.save(te);
                te.setId(id);

                texts.add(te);
            }

            Task task = new Task(phoenixTask.getTitle(), phoenixTask.getDescription(), attachments, texts);
            if (isAutomaticTask) {
                PhoenixAutomaticTask autoTask = (PhoenixAutomaticTask) phoenixTask;
                task.setBackend(autoTask.getBackend());
                task.setAutomaticTest(true);

                List<Text> tests = new ArrayList<Text>(autoTask.getTestsSize());
                for (PhoenixText test : autoTask.getTests()) {
                    Text te = new Text(test);
                    Integer id = (Integer) session.save(te);
                    te.setId(id);

                    tests.add(te);
                }
                task.setTests(tests);
            }
            session.save(task);

            trans.commit();
            return Response.ok().build();

        } finally {
            if (session != null)
                session.close();
        }

    }
    @Path("/" + PhoenixTask.WEB_RESOURCE_UPDATE)
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response change(Updateable<PhoenixTask, String> toUpdate) {

        Session session = DatabaseManager.getSession();
        try {

            Task task = (Task) session.getNamedQuery("Task.findByTitle").setString("title", toUpdate.getKey()).uniqueResult();
            if (task == null)
                return Response.notModified().entity("No entity found by this title!").build();

            PhoenixTask phoenixTask = toUpdate.getVal();
            task.setDescription(phoenixTask.getDescription());
            task.setTitle(phoenixTask.getTitle());

            session.update(task);
            return Response.ok().build();

        } finally {
            if (session != null)
                session.close();
        }
    }

    @Path("/" + PhoenixTask.WEB_RESOURCE_DELETE)
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response delete(PhoenixTask phoenixTask) {

        Session session = DatabaseManager.getSession();
        try {

            Task task = (Task) session.getNamedQuery("Task.findByTitle").setString("title", phoenixTask.getTitle()).uniqueResult();
            if (task == null)
                return Response.notModified().entity("No entity found by this title!").build();

            session.delete(task);

            return Response.ok().build();

        } finally {
            if (session != null)
                session.close();
        }
    }

    @SuppressWarnings("unchecked")
    @Path("/" + PhoenixTask.WEB_RESOURCE_GETALL)
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() throws SQLException {

        Session session = DatabaseManager.getSession();
        try {
            List<Task> tasks = session.getNamedQuery("Task.findAll").list();

            List<PhoenixTask> result = new ConverterArrayList<PhoenixTask>(tasks);

            // Encapsulate the list to transform it via JXR-RS
            return Response.ok(PhoenixTask.toSendableList(result)).build();

        } finally {
            if (session != null)
                session.close();
        }
    }

    @Path("/" + PhoenixTask.WEB_RESOURCE_GETBYTITLE)
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getByTitle(String title) {

        Session session = DatabaseManager.getSession();

        try {
            Task task = (Task) session.getNamedQuery("Task.findByTitle").setString("title", title).uniqueResult();
            if (task == null)
                return Response.status(Status.NO_CONTENT).build();
            // Always convert before closing the session
            PhoenixTask result = task.convert();

            return Response.ok(result, MediaType.APPLICATION_JSON_TYPE).build();

        } finally {
            if (session != null)
                session.close();
        }
    }

    @SuppressWarnings("unchecked")
    @Path("/" + PhoenixTask.WEB_RESOURCE_GETALL_TITLES)
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllTitles() {

        Session session = DatabaseManager.getSession();

        try {
            List<String> result = session.createCriteria(Task.class).setProjection(Projections.property("title")).list();

            return Response.ok(RSLists.toSendableStringList(result)).build();

        } finally {
            if (session != null)
                session.close();
        }
    }
}
