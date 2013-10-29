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
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.hibernate.Session;
import org.hibernate.Transaction;

import de.phoenix.database.DatabaseManager;
import de.phoenix.database.entity.Attachment;
import de.phoenix.database.entity.Task;
import de.phoenix.database.entity.Text;
import de.phoenix.rs.entity.PhoenixAttachment;
import de.phoenix.rs.entity.PhoenixTask;
import de.phoenix.rs.entity.PhoenixText;

@Path("/task")
public class TaskResource {

    @Path("/create")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(PhoenixTask phoenixTask) {

        Session session = DatabaseManager.getSession();
        Transaction trans = session.beginTransaction();

        List<Attachment> attachments = new ArrayList<Attachment>(phoenixTask.getAttachmentsSize());
        for (PhoenixAttachment attachment : phoenixTask.getAttachments()) {
            Attachment at = new Attachment(attachment);
            Integer id = (Integer) session.save(at);
            at.setId(id);

            attachments.add(at);
        }

        List<Text> texts = new ArrayList<Text>(phoenixTask.getPatternSize());
        for (PhoenixText attachment : phoenixTask.getPattern()) {
            Text text = new Text(attachment);
            Integer id = (Integer) session.save(text);
            text.setId(id);

            texts.add(text);
        }

        Task task = new Task(phoenixTask.getTitle(), phoenixTask.getDescription(), attachments, texts);
        session.save(task);

        trans.commit();
        session.disconnect();

        return Response.ok().build();
    }

    @SuppressWarnings("unchecked")
    @Path("/getAll")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() throws SQLException {

        Session session = DatabaseManager.getSession();
        List<Task> tasks = session.getNamedQuery("Task.findAll").list();
        session.disconnect();

        List<PhoenixTask> result = new ArrayList<PhoenixTask>(tasks.size());

        for (Task task : tasks) {
            result.add(getPhoenixTask(task));
        }

        // Encapsulate the list to transform it via JXR-RS
        final GenericEntity<List<PhoenixTask>> entity = new GenericEntity<List<PhoenixTask>>(result) {
        };

        return Response.ok(entity, MediaType.APPLICATION_JSON).build();
    }

    @SuppressWarnings("unchecked")
    @Path("/getByTitle")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response getByTitle(String title) throws SQLException {

        Session session = DatabaseManager.getSession();

        List<Task> tasks = session.getNamedQuery("Task.findByTitle").list();
        session.disconnect();

        List<PhoenixTask> result = new ArrayList<PhoenixTask>(tasks.size());

        for (Task task : tasks) {
            result.add(getPhoenixTask(task));
        }

        // Encapsulate the list to transform it via JXR-RS
        final GenericEntity<List<PhoenixTask>> entity = new GenericEntity<List<PhoenixTask>>(result) {
        };

        return Response.ok(entity, MediaType.APPLICATION_JSON).build();
    }

    private PhoenixTask getPhoenixTask(Task task) throws SQLException {
        List<Attachment> attachments = task.getAttachments();
        List<PhoenixAttachment> pAttachments = new ArrayList<PhoenixAttachment>(attachments.size());
        for (Attachment at : attachments) {
            pAttachments.add(new PhoenixAttachment(at.getFile().getBytes(1, (int) at.getFile().length()), at.getCreationDate(), at.getName(), at.getName()));
        }

        List<Text> texts = task.getTexts();
        List<PhoenixText> pTexts = new ArrayList<PhoenixText>(texts.size());
        for (Text tx : texts) {
            pTexts.add(new PhoenixText(tx.getContent(), tx.getCreationDate(), tx.getName(), tx.getType()));
        }

        return new PhoenixTask(pAttachments, pTexts, task.getDescription(), task.getTitle());
    }
}