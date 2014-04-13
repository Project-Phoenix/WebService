/*
 * Copyright (C) 2014 Project-Phoenix
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

package de.phoenix.database.entity;

import static de.phoenix.database.EntityTest.BASE_URL;
import static de.phoenix.database.EntityTest.CLIENT;
import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.ws.rs.core.MediaType;

import org.junit.BeforeClass;
import org.junit.Test;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import de.phoenix.rs.entity.PhoenixAttachment;
import de.phoenix.rs.entity.PhoenixAutomaticTask;
import de.phoenix.rs.entity.PhoenixSubmission;
import de.phoenix.rs.entity.PhoenixSubmissionResult;
import de.phoenix.rs.entity.PhoenixTask;
import de.phoenix.rs.entity.PhoenixTaskTest;
import de.phoenix.rs.entity.PhoenixText;
import de.phoenix.rs.key.KeyReader;

public class MultiFileTaskTests {

    private static final File resourceFolder = new File("src/test/resources/task/filterList");

    private static PhoenixTask task;

    // Create the automatic task
    @BeforeClass
    public static void beforeClass() throws IOException {
        WebResource createTask = PhoenixTask.createResource(CLIENT, BASE_URL);

        // No interest in description
        String description = "";
        // Empty attachment list
        List<PhoenixAttachment> attachments = new ArrayList<PhoenixAttachment>();

        // Pattern
        List<PhoenixText> answerPattern = new ArrayList<PhoenixText>();
        answerPattern.add(new PhoenixText(new File(resourceFolder, "pattern/FilterList.java"), "FilterList.java"));
        answerPattern.add(new PhoenixText(new File(resourceFolder, "pattern/EvenNumberPredicate.java"), "EvenNumberPredicate.java"));
        answerPattern.add(new PhoenixText(new File(resourceFolder, "pattern/Predicate.java"), "Predicate.java"));

        // Test
        List<PhoenixTaskTest> tests = new ArrayList<PhoenixTaskTest>();
        tests.add(new PhoenixTaskTest(new PhoenixText(new File(resourceFolder, "FilterListTest.java"), "FilterListTest.java")));

        // create task and send to web service
        PhoenixAutomaticTask filterTestTask = new PhoenixAutomaticTask(attachments, answerPattern, description, "filterlist", "jav", tests);
        ClientResponse response = createTask.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, filterTestTask);
        // Check, if everything went right
        assertEquals(200, response.getStatus());

        task = filterTestTask;
    }

    @Test
    public void submitCorrectImplementation() throws IOException {

        // Classes to submit
        List<PhoenixText> classes = new ArrayList<PhoenixText>();
        classes.add(new PhoenixText(new File(resourceFolder, "goodImpl/FilterList.java"), "FilterList.java"));
        classes.add(new PhoenixText(new File(resourceFolder, "goodImpl/EvenNumberPredicate.java"), "EvenNumberPredicate.java"));
        classes.add(new PhoenixText(new File(resourceFolder, "goodImpl/Predicate.java"), "Predicate.java"));

        // Empty attachment list
        List<PhoenixAttachment> attachments = new ArrayList<PhoenixAttachment>();

        PhoenixSubmission submission = new PhoenixSubmission(attachments, classes);

        // Send submission to server
        WebResource submitResource = PhoenixTask.submitResource(CLIENT, BASE_URL);
        ClientResponse response = submitResource.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, KeyReader.createAddTo(task, Arrays.asList(submission)));

        assertEquals(200, response.getStatus());

        PhoenixSubmissionResult result = response.getEntity(PhoenixSubmissionResult.class);
        assertEquals(PhoenixSubmissionResult.SubmissionStatus.OK, result.getStatus());
    }

    @Test
    public void submitWrongImplementation() throws IOException {

        // Classes to submit
        List<PhoenixText> classes = new ArrayList<PhoenixText>();
        classes.add(new PhoenixText(new File(resourceFolder, "wrongImpl/FilterList.java"), "FilterList.java"));
        classes.add(new PhoenixText(new File(resourceFolder, "wrongImpl/EvenNumberPredicate.java"), "EvenNumberPredicate.java"));
        classes.add(new PhoenixText(new File(resourceFolder, "wrongImpl/Predicate.java"), "Predicate.java"));

        // Empty attachment list
        List<PhoenixAttachment> attachments = new ArrayList<PhoenixAttachment>();

        PhoenixSubmission submission = new PhoenixSubmission(attachments, classes);

        // Send submission to server
        WebResource submitResource = PhoenixTask.submitResource(CLIENT, BASE_URL);
        ClientResponse response = submitResource.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, KeyReader.createAddTo(task, Arrays.asList(submission)));

        assertEquals(200, response.getStatus());

        PhoenixSubmissionResult result = response.getEntity(PhoenixSubmissionResult.class);
        assertEquals(PhoenixSubmissionResult.SubmissionStatus.TEST_FAILED, result.getStatus());
    }
    
    @Test
    public void submitIncompleteImpementation() throws IOException {
        // Classes to submit
        List<PhoenixText> classes = new ArrayList<PhoenixText>();
        classes.add(new PhoenixText(new File(resourceFolder, "goodImpl/FilterList.java"), "FilterList.java"));
        classes.add(new PhoenixText(new File(resourceFolder, "goodImpl/Predicate.java"), "Predicate.java"));

        // Empty attachment list
        List<PhoenixAttachment> attachments = new ArrayList<PhoenixAttachment>();

        PhoenixSubmission submission = new PhoenixSubmission(attachments, classes);

        // Send submission to server
        WebResource submitResource = PhoenixTask.submitResource(CLIENT, BASE_URL);
        ClientResponse response = submitResource.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, KeyReader.createAddTo(task, Arrays.asList(submission)));

        assertEquals(200, response.getStatus());

        PhoenixSubmissionResult result = response.getEntity(PhoenixSubmissionResult.class);
        assertEquals(PhoenixSubmissionResult.SubmissionStatus.MISSING_FILES, result.getStatus());
        assertEquals("Missing classes to implement/submit. Maybe you wrote the name of the class wrong? Missing Classes:\r\n[EvenNumberPredicate]", result.getStatusText());
    }
    
    @Test
    public void submitWrongNameImpementation() throws IOException {
        // Classes to submit
        List<PhoenixText> classes = new ArrayList<PhoenixText>();
        classes.add(new PhoenixText(new File(resourceFolder, "wrongNameImpl/MyFilterList.java"), "MyFilterList.java"));
        classes.add(new PhoenixText(new File(resourceFolder, "wrongNameImpl/EvenNumberPredicate.java"), "EvenNumberPredicate.java"));
        classes.add(new PhoenixText(new File(resourceFolder, "wrongNameImpl/Predicate.java"), "Predicate.java"));

        // Empty attachment list
        List<PhoenixAttachment> attachments = new ArrayList<PhoenixAttachment>();

        PhoenixSubmission submission = new PhoenixSubmission(attachments, classes);

        // Send submission to server
        WebResource submitResource = PhoenixTask.submitResource(CLIENT, BASE_URL);
        ClientResponse response = submitResource.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, KeyReader.createAddTo(task, Arrays.asList(submission)));

        assertEquals(200, response.getStatus());

        PhoenixSubmissionResult result = response.getEntity(PhoenixSubmissionResult.class);
        assertEquals(PhoenixSubmissionResult.SubmissionStatus.MISSING_FILES, result.getStatus());
        assertEquals("Missing classes to implement/submit. Maybe you wrote the name of the class wrong? Missing Classes:\r\n[FilterList]", result.getStatusText());
    }
}
