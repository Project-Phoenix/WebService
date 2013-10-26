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

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.sun.jersey.multipart.FormDataBodyPart;
import com.sun.jersey.multipart.FormDataMultiPart;

import de.phoenix.database.DatabaseManager;
import de.phoenix.database.entity.Attachment;
import de.phoenix.database.entity.Task;
import de.phoenix.database.entity.Text;

//import java.util.Collections;
//import java.util.List;
//
//import javax.ws.rs.Consumes;
//import javax.ws.rs.GET;
//import javax.ws.rs.POST;
//import javax.ws.rs.Path;
//import javax.ws.rs.Produces;
//import javax.ws.rs.core.GenericEntity;
//import javax.ws.rs.core.MediaType;
//import javax.ws.rs.core.Response;
//
//import org.hibernate.Query;
//import org.hibernate.Session;
//import org.hibernate.Transaction;
//
//import de.phoenix.database.DatabaseManager;
//import de.phoenix.database.entity.AutomaticTask;
//import de.phoenix.database.entity.Tag;
//import de.phoenix.database.entity.Task;
//import de.phoenix.database.entity.TaskPool;

@Path("/task")
public class TaskResource {

    @Path("/create")
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response create(FormDataMultiPart multiPart) {

        Session session = DatabaseManager.getInstance().openSession();
        List<Attachment> attachments = createAttachments(multiPart.getFields("attachment"), session);
        List<Text> pattern = createPattern(multiPart.getFields("pattern"), session);
        Task task = new Task();
        task.setAttachments(attachments);

        task.setTexts(pattern);
        task.setDescription(multiPart.getField("description").getValue());

        Transaction beginTransaction = session.beginTransaction();
        session.save(task);
        beginTransaction.commit();

        return Response.ok().build();
    }

    private List<Attachment> createAttachments(List<FormDataBodyPart> list, Session session) {
        List<Attachment> result = new ArrayList<Attachment>();

        for (FormDataBodyPart bodyPart : list) {

            Transaction trans = session.beginTransaction();

            try {
                File file = bodyPart.getEntityAs(File.class);

                FileInputStream fin = new FileInputStream(file);
                Blob blob = Hibernate.createBlob(fin);

                String fileName = bodyPart.getContentDisposition().getFileName();
                int fileSeperator = fileName.lastIndexOf('.');
                String fileType = fileName.substring(fileSeperator + 1);
                fileName = fileName.substring(0, fileSeperator);
                Attachment at = new Attachment(blob, new Date(), fileName, fileType);

                Integer id = (Integer) session.save(at);
                at.setId(id);

                result.add(at);
            } catch (Exception e) {
                e.printStackTrace();
            }
            trans.commit();
        }

        return result;
    }

    private List<Text> createPattern(List<FormDataBodyPart> list, Session session) {
        List<Text> result = new ArrayList<Text>();

        for (FormDataBodyPart bodyPart : list) {
            Transaction trans = session.beginTransaction();

            try {
                File file = bodyPart.getEntityAs(File.class);

                String context = new String(Files.readAllBytes(file.toPath()));

                String fileName = bodyPart.getContentDisposition().getFileName();
                int fileSeperator = fileName.lastIndexOf('.');
                String fileType = fileName.substring(fileSeperator + 1);
                fileName = fileName.substring(0, fileSeperator);
                Text text = new Text(context, new Date(), fileName, fileType);

                Integer id = (Integer) session.save(text);
                text.setId(id);

                result.add(text);
            } catch (Exception e) {
                e.printStackTrace();

            }
            trans.commit();
        }

        return result;
    }
//    @Path("/create")
//    @POST
//    @Consumes(MediaType.APPLICATION_JSON)
//    public Response createTask(TaskPool task) {
//
//        // Open instance for the database
//        Session session = DatabaseManager.getInstance().openSession();
//        Transaction trans = session.beginTransaction();
//
//        // Store tags in database
//        for (Tag tag : task.getTags()) {
//            // Check if task is already existing (avoid duplicate tags in
//            // database)
//            Query q = session.getNamedQuery("Tag.findByTag").setString("tag", tag.getTag());
//            Object c = q.uniqueResult();
//            // Is existing
//            if (c == null) {
//                // Store new task in database and assign local object to created
//                // in database
//                tag.setId((Integer) session.save(tag));
//            } else {
//                // use existing object in database
//                Tag tmp = (Tag) c;
//                tag.setId(tmp.getId());
//            }
//        }
//        // Save task in database
//        session.save(task);
//
//        // Close transaction
//        trans.commit();
//        session.close();
//
//        return Response.ok().build();
//    }
//
//    @SuppressWarnings("unchecked")
//    @Path("/getAll")
//    @GET
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response getAll() {
//
//        // Open instance for the database
//        Session session = DatabaseManager.getInstance().openSession();
//        // Get all tasks from database
//        List<TaskPool> result = session.getNamedQuery("TaskPool.findAll").list();
//
//        for (TaskPool taskPool : result) {
//            taskPool.setAutomaticTaskList(Collections.<AutomaticTask>emptyList());
//            taskPool.setTasks(Collections.<Task>emptyList());
//        }
//        
//        // Encapsulate the list to transform it via JXR-RS
//        final GenericEntity<List<TaskPool>> entity = new GenericEntity<List<TaskPool>>(result) {
//        };
//
//        return Response.ok(entity).build();
//    }
}
