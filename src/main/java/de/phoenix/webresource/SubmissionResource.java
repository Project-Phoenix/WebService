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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.sun.jersey.multipart.BodyPart;
import com.sun.jersey.multipart.MultiPart;

import de.phoenix.PhoenixApplication;
import de.phoenix.database.entity.Submission;
import de.phoenix.database.entity.SubmissionFiles;

/**
 * Webresource for uploading and getting submissions from user.
 * 
 */
@Path("/submission")
public class SubmissionResource {

    @Path("/submit/")
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response submit(MultiPart multiPart) {
        List<SubmissionFiles> files = readFiles(multiPart.getBodyParts());

        Session session = PhoenixApplication.databaseManager.openSession();
        Transaction tx = session.beginTransaction();

        Submission sub = new Submission(true);
        int id = (Integer) session.save(sub);
        sub.setId(id);

        for (SubmissionFiles file : files) {
            file.setSubmission(sub);
            session.save(file);
        }

        tx.commit();

        return Response.ok().build();
    }

    private List<SubmissionFiles> readFiles(List<BodyPart> bodyParts) {
        String fileName = "";
        String content = "";
        List<SubmissionFiles> files = new ArrayList<SubmissionFiles>();
        for (BodyPart bodyPart : bodyParts) {
            fileName = bodyPart.getContentDisposition().getFileName();
            File f = bodyPart.getEntityAs(File.class);
            content = readFile(f, (int) bodyPart.getContentDisposition().getSize());
            SubmissionFiles tmp = new SubmissionFiles();
            tmp.setFilename(fileName);
            tmp.setContent(content);

            files.add(tmp);
        }

        return files;
    }

    private String readFile(File file, int size) {
        try {
            StringBuffer content = new StringBuffer(size);
            BufferedReader bReader = new BufferedReader(new FileReader(file));
            String line = "";
            while ((line = bReader.readLine()) != null) {
                content.append(line);
                content.append(System.lineSeparator());
            }

            bReader.close();
            return content.toString();
        } catch (Exception e) {
            return null;
        }
    }
}
