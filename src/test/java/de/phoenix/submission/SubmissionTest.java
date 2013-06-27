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

package de.phoenix.submission;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.container.httpserver.HttpServerFactory;
import com.sun.net.httpserver.HttpServer;

import de.phoenix.database.entity.Submission;
import de.phoenix.database.entity.SubmissionFiles;
import de.phoenix.util.UploadHelper;

public class SubmissionTest {

    private final static String BASE_URL = "http://localhost:7766/rest";

    private static HttpServer httpServer;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        // Start Http Server
        httpServer = HttpServerFactory.create(BASE_URL);
        httpServer.start();
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        httpServer.stop(0);
    }

    @Test
    public void uploadSubmission() {

        File[] files = {new File(".classpath"), new File("pom.xml")};

        Client c = Client.create();
        WebResource resource = c.resource(BASE_URL).path("/submission").path("/submit");
        ClientResponse response = UploadHelper.uploadFile(resource, files);
        assertTrue(response.getStatus() == 200);
    }

    @Test
    public void getSubmission() {

        Client c = Client.create();
        // Webresource to get all submission
        WebResource submissionResource = c.resource(BASE_URL).path("/submission").path("/getAll");

        // Ugly constructs to receive lists of generic types - no other way to
        // solve this
        GenericType<List<Submission>> submissionType = new GenericType<List<Submission>>() {
        };

        List<Submission> result = submissionResource.get(submissionType);

        for (Submission submission : result) {

            assertFalse(submission.getControllMessage().isEmpty());
            assertTrue(submission.getSubmissionDate() != null);
            for (SubmissionFiles file : submission.getSubmissionFilesList()) {
                assertFalse(file.getFilename().isEmpty());
                assertFalse(file.getContent().isEmpty());
            }
        }

    }
}
