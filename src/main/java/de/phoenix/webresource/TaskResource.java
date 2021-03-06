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
import de.phoenix.database.entity.Task;
import de.phoenix.database.entity.TaskSubmission;
import de.phoenix.database.entity.criteria.TaskCriteriaFactory;
import de.phoenix.rs.entity.PhoenixSubmission;
import de.phoenix.rs.entity.PhoenixSubmissionResult;
import de.phoenix.rs.entity.PhoenixSubmissionResult.SubmissionStatus;
import de.phoenix.rs.entity.PhoenixTask;
import de.phoenix.rs.key.AddToEntity;
import de.phoenix.rs.key.SelectEntity;
import de.phoenix.rs.key.UpdateEntity;
import de.phoenix.submission.DefaultSubmissionController;
import de.phoenix.submission.SubmissionController;
import de.phoenix.webresource.util.AbstractPhoenixResource;

@Path(PhoenixTask.WEB_RESOURCE_ROOT)
public class TaskResource extends AbstractPhoenixResource<Task, PhoenixTask> {

    private final static SubmissionController CONTROLLER = new DefaultSubmissionController();

    public TaskResource() {
        super(TaskCriteriaFactory.getInstance());
    }

    @Path(PhoenixTask.WEB_RESOURCE_CREATE)
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createTask(PhoenixTask phoenixTask) {
        return onCreate(phoenixTask, new EntityCreator<Task, PhoenixTask>() {
            public Task create(PhoenixTask phoenixEntity) {
                return new Task(phoenixEntity);
            }
        });
    }

    @SuppressWarnings("unchecked")
    @Path(PhoenixTask.WEB_RESOURCE_GETALL_TITLES)
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllTitles() {

        Session session = DatabaseManager.getSession();

        try {
            List<String> result = session.createCriteria(Task.class).setProjection(Projections.property("title")).list();

            return Response.ok(result).build();

        } finally {
            if (session != null)
                session.close();
        }
    }

    @Path(PhoenixTask.WEB_RESOURCE_GET)
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getTask(SelectEntity<PhoenixTask> selectEntity) {
        return onGet(selectEntity);
    }

    @Path(PhoenixTask.WEB_RESOURCE_UPDATE)
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(UpdateEntity<PhoenixTask> updateEntity) {
        return onUpdate(updateEntity);
    }

    @Path(PhoenixTask.WEB_RESOURCE_DELETE)
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response delete(SelectEntity<PhoenixTask> selectEntity) {

        return onDelete(selectEntity);
    }

    @Path(PhoenixTask.WEB_RESOURCE_ADD_SUBMISSION)
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addSubmission(AddToEntity<PhoenixTask, PhoenixSubmission> addToEntity) {
        List<PhoenixSubmission> phoenixSubmissions = addToEntity.getAttachedEntities();

        // Disallow multiple submissions
        if (phoenixSubmissions.size() != 1) {
            return Response.status(Status.BAD_REQUEST).entity("Only one submission is allowed!").build();
        }

        Session session = DatabaseManager.getSession();
        try {
            List<Task> tasks = searchEntity(addToEntity, session);
            checkOnlyOne(tasks);

            Task task = tasks.get(0);

            Transaction trans = session.beginTransaction();
            PhoenixSubmission phoenixSubmission = phoenixSubmissions.get(0);
            TaskSubmission taskSubmission = new TaskSubmission(task, phoenixSubmission);

            PhoenixSubmissionResult result = new PhoenixSubmissionResult(SubmissionStatus.SUBMITTED, "");
            if (task.isAutomaticTest()) {
                result = CONTROLLER.controllSolution(taskSubmission);
            }
            taskSubmission.setSubmissionResult(result);

            session.save(taskSubmission);

            trans.commit();

            return Response.ok(result, MediaType.APPLICATION_JSON).build();
        } finally {
            if (session != null)
                session.close();
        }
    }
}
