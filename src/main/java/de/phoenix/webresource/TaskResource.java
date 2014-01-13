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

import java.io.IOException;
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

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.exception.ConstraintViolationException;

import de.phoenix.database.DatabaseManager;
import de.phoenix.database.entity.Attachment;
import de.phoenix.database.entity.Task;
import de.phoenix.database.entity.Text;
import de.phoenix.database.entity.util.ConverterUtil;
import de.phoenix.rs.entity.PhoenixAttachment;
import de.phoenix.rs.entity.PhoenixAutomaticTask;
import de.phoenix.rs.entity.PhoenixTask;
import de.phoenix.rs.entity.PhoenixText;
import de.phoenix.rs.key.SelectEntity;
import de.phoenix.rs.key.UpdateEntity;
import de.phoenix.util.RSLists;
import de.phoenix.webresource.util.AbstractPhoenixResource;

@Path("/" + PhoenixTask.WEB_RESOURCE_ROOT)
public class TaskResource extends AbstractPhoenixResource<Task, PhoenixTask> {

    public TaskResource() {
        super(Task.class);
    }

    // TOOO: Remove it , because the Select and Update mechanism is better
    @SuppressWarnings("unchecked")
    @Path("/" + PhoenixTask.WEB_RESOURCE_GETALL)
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Deprecated
    public Response getAll() throws SQLException {

        Session session = DatabaseManager.getSession();
        try {
            List<Task> tasks = session.getNamedQuery("Task.findAll").list();

            List<PhoenixTask> result = ConverterUtil.convert(tasks);

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
    @Deprecated
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

    @Path("/" + PhoenixTask.WEB_RESOURCE_CREATE)
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createTask(PhoenixTask phoenixTask) {
        try {
            return onCreate(phoenixTask);
        } catch (ConstraintViolationException e) {
            if (isDuplicateEntryError(e)) {
                return Response.status(Status.BAD_REQUEST).entity("Duplicate task title!").build();
            } else {
                throw e;
            }
        }
    }

    private static final String DUPLICATE_SQL_STATE = "23000";
    private static final int DUPLICATE_SQL_ERROR = 1062;

    private boolean isDuplicateEntryError(ConstraintViolationException e) {
        return e.getErrorCode() == DUPLICATE_SQL_ERROR && e.getSQLState().equals(DUPLICATE_SQL_STATE);
    }

    @Override
    protected Task create(PhoenixTask phoenixTask, Session session) {
        boolean isAutomaticTask = (phoenixTask instanceof PhoenixAutomaticTask);

        List<Attachment> attachments = new ArrayList<Attachment>();
        for (PhoenixAttachment attachment : phoenixTask.getAttachments()) {
            Attachment at;
            try {
                at = new Attachment(attachment);
            } catch (IOException e) {
                return null;
            }
            attachments.add(at);
        }

        List<Text> texts = new ArrayList<Text>();
        for (PhoenixText text : phoenixTask.getPattern()) {
            Text te = new Text(text);
            texts.add(te);
        }

        Task task = new Task(phoenixTask.getTitle(), phoenixTask.getDescription(), attachments, texts);
        if (isAutomaticTask) {
            PhoenixAutomaticTask autoTask = (PhoenixAutomaticTask) phoenixTask;
            task.setBackend(autoTask.getBackend());
            task.setAutomaticTest(true);

            List<Text> tests = new ArrayList<Text>();
            for (PhoenixText test : autoTask.getTests()) {
                Text te = new Text(test);
                tests.add(te);
            }
            task.setTests(tests);
        }

        return task;
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

    @Path("/" + PhoenixTask.WEB_RESOURCE_GET)
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getTask(SelectEntity<PhoenixTask> selectEntity) {
        return Response.ok(onGet(selectEntity)).build();
    }

    @Path("/" + PhoenixTask.WEB_RESOURCE_UPDATE)
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(UpdateEntity<PhoenixTask> updateEntity) {
        return onUpdate(updateEntity);
    }

    @Path("/" + PhoenixTask.WEB_RESOURCE_DELETE)
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response delete(SelectEntity<PhoenixTask> selectEntity) {

        return onDelete(selectEntity);
    }

    @Override
    protected void setValues(Task entity, PhoenixTask phoenixEntity) {
        entity.setTitle(phoenixEntity.getTitle());
        entity.setDescription(phoenixEntity.getDescription());

        if (phoenixEntity instanceof PhoenixAutomaticTask) {
            PhoenixAutomaticTask tmp = (PhoenixAutomaticTask) phoenixEntity;
            entity.setBackend(tmp.getBackend());
            entity.setAutomaticTest(true);
        }
    }

    @Override
    protected void setCriteria(SelectEntity<PhoenixTask> selectEntity, Criteria criteria) {
        addParameter(selectEntity, "title", String.class, "title", criteria);
        addParameter(selectEntity, "description", String.class, "description", criteria);

        // Is instance of automatic task
        if (selectEntity.get("backend", String.class) != null) {
            addParameter(selectEntity, "backend", String.class, "backend", criteria);
            criteria.add(Restrictions.eq("backend", true));
        }
    }
}
