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
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.hibernate.Session;
import org.hibernate.Transaction;

import de.phoenix.database.DatabaseManager;
import de.phoenix.database.entity.Attachment;
import de.phoenix.database.entity.Task;
import de.phoenix.database.entity.TaskSubmission;
import de.phoenix.database.entity.Text;
import de.phoenix.rs.entity.PhoenixAttachment;
import de.phoenix.rs.entity.PhoenixSubmission;
import de.phoenix.rs.entity.PhoenixTask;
import de.phoenix.rs.entity.PhoenixText;

/**
 * Webresource for uploading and getting submissions from user.
 * 
 */
@Path("/" + PhoenixSubmission.WEB_RESOURCE_ROOT)
public class SubmissionResource {

    @Path("/" + PhoenixSubmission.WEB_RESOURCE_SUBMIT)
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response submit(PhoenixSubmission phoenixSubmission) {

        Session session = DatabaseManager.getSession();

        // Get task from the database
        // TODO: Require, that task must be unique!
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
        TaskSubmission submission = new TaskSubmission(task, attachments, texts);
        // Save it
        session.save(submission);

        // Close connection
        trans.commit();
        session.disconnect();

        return Response.ok().build();
    }

    @Path("/" + PhoenixSubmission.WEB_RESOURCE_GET_TASK_SUBMISSIONS)
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getSubmissionsForTask(PhoenixTask phoenixTask) {

        Session session = DatabaseManager.getSession();
        
        // Get task from database
        Task task = (Task) session.getNamedQuery("Task.findByTitle").setString("title", phoenixTask.getTitle()).uniqueResult();
        if (task == null)
            return Response.notModified().entity("No entity found by this title!").build();

        // Get all submissions for this task
        List<TaskSubmission> submissions = task.getTaskSubmissions();

        // List containing the result
        List<PhoenixSubmission> result = new ArrayList<PhoenixSubmission>();

        // Convert the submission to phoenix submission
        for (TaskSubmission submission : submissions) {
            List<PhoenixAttachment> attachmentList = new ArrayList<PhoenixAttachment>();
            for (Attachment attachment : submission.getAttachments()) {
                attachmentList.add(attachment.convert());
            }
            List<PhoenixText> texts = new ArrayList<PhoenixText>();
            for (Text text : submission.getTexts()) {
                texts.add(text.convert());
            }
            result.add(new PhoenixSubmission(submission.getDate(), phoenixTask, attachmentList, texts));
        }

        // Encapsulate the list to transform it via JXR-RS
        final GenericEntity<List<PhoenixSubmission>> entity = new GenericEntity<List<PhoenixSubmission>>(result) {
        };

        return Response.ok(entity, MediaType.APPLICATION_JSON).build();
    }
}
