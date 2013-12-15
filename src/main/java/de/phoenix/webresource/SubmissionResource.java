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
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.hibernate.Session;
import org.hibernate.Transaction;

import de.phoenix.database.DatabaseManager;
import de.phoenix.database.entity.Attachment;
import de.phoenix.database.entity.Task;
import de.phoenix.database.entity.TaskSubmission;
import de.phoenix.database.entity.Text;
import de.phoenix.database.entity.util.ConverterArrayList;
import de.phoenix.rs.entity.PhoenixAttachment;
import de.phoenix.rs.entity.PhoenixSubmission;
import de.phoenix.rs.entity.PhoenixSubmissionResult;
import de.phoenix.rs.entity.PhoenixSubmissionResult.SubmissionStatus;
import de.phoenix.rs.entity.PhoenixTask;
import de.phoenix.rs.entity.PhoenixText;
import de.phoenix.submission.DefaultSubmissionController;
import de.phoenix.submission.SubmissionResult;
import de.phoenix.submission.SubmissionController;

/**
 * Webresource for uploading and getting submissions from user.
 * 
 */
@Path("/" + PhoenixSubmission.WEB_RESOURCE_ROOT)
public class SubmissionResource {

    private final static SubmissionController CONTROLLER = new DefaultSubmissionController();

    @Path("/" + PhoenixSubmission.WEB_RESOURCE_SUBMIT)
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response submit(PhoenixSubmission phoenixSubmission) {

        Session session = DatabaseManager.getSession();

        // Get task from the database
        try {
            Task task = (Task) session.getNamedQuery("Task.findByTitle").setString("title", phoenixSubmission.getTask().getTitle()).uniqueResult();
            if (task == null)
                return Response.notModified().entity("No entity found by this title!").build();

            Transaction trans = session.beginTransaction();
            // Store attachments
            List<Attachment> attachments = new ArrayList<Attachment>(phoenixSubmission.getAttachmentsSize());
            for (PhoenixAttachment attachment : phoenixSubmission.getAttachments()) {
                Attachment at = new Attachment(attachment);
                Integer id = (Integer) session.save(at);
                at.setId(id);

                attachments.add(at);
            }

            // Store texts
            List<Text> texts = new ArrayList<Text>(phoenixSubmission.getTextsSize());
            for (PhoenixText text : phoenixSubmission.getTexts()) {
                Text te = new Text(text);
                Integer id = (Integer) session.save(te);
                te.setId(id);

                texts.add(te);
            }

            // Store the submission itself
            TaskSubmission submission = new TaskSubmission(0, "Bestanden", task, attachments, texts);
            SubmissionResult result = null;
            if (task.isAutomaticTest()) {
                result = CONTROLLER.controllSolution(submission);
            } else {
                result = new SubmissionResult(SubmissionStatus.SUBMITTED, "");
            }

            submission.setStatus(result.getStatus().ordinal());
            submission.setStatusText(result.getStatusText());

            // Save it
            session.save(submission);

            // Close connection
            trans.commit();

            return Response.ok((PhoenixSubmissionResult) result).build();

        } finally {
            if (session != null)
                session.close();
        }
    }

    @Path("/" + PhoenixSubmission.WEB_RESOURCE_GET_TASK_SUBMISSIONS)
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSubmissionsForTask(PhoenixTask phoenixTask) {

        Session session = DatabaseManager.getSession();

        // Get task from database
        try {
            Task task = (Task) session.getNamedQuery("Task.findByTitle").setString("title", phoenixTask.getTitle()).uniqueResult();
            if (task == null)
                return Response.notModified().entity("No entity found by this title!").build();

            // Get all submissions for this task
            List<TaskSubmission> submissions = task.getTaskSubmissions();

            // List containing the result
            List<PhoenixSubmission> result = new ConverterArrayList<PhoenixSubmission>(submissions);

            return Response.ok(PhoenixSubmission.toSendableList(result)).build();

        } finally {
            if (session != null)
                session.close();

        }

    }
}
