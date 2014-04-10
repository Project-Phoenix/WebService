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

package de.phoenix.database.entity;

import static de.phoenix.database.EntityTest.BASE_URL;
import static de.phoenix.database.EntityTest.CLIENT;
import static de.phoenix.database.EntityTest.READER;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.CharacterCodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.ws.rs.core.MediaType;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.ClientResponse.Status;
import com.sun.jersey.api.client.WebResource;

import de.phoenix.junit.OrderedRunner;
import de.phoenix.junit.OrderedRunner.Order;
import de.phoenix.rs.EntityUtil;
import de.phoenix.rs.PhoenixStatusType;
import de.phoenix.rs.entity.PhoenixAttachment;
import de.phoenix.rs.entity.PhoenixAutomaticTask;
import de.phoenix.rs.entity.PhoenixSubmission;
import de.phoenix.rs.entity.PhoenixSubmissionResult;
import de.phoenix.rs.entity.PhoenixSubmissionResult.SubmissionStatus;
import de.phoenix.rs.entity.PhoenixTask;
import de.phoenix.rs.entity.PhoenixTaskTest;
import de.phoenix.rs.entity.PhoenixText;
import de.phoenix.rs.key.KeyReader;
import de.phoenix.rs.key.SelectAllEntity;
import de.phoenix.rs.key.SelectEntity;
import de.phoenix.submission.DisallowedContent;

@RunWith(OrderedRunner.class)
public class TaskTests {

    private final static String TEST_TITLE = "Befreundete Zahlen";
    private final static File TEST_DESCRIPTION_FILE = new File("src/test/resources/task/specialNumbers/TaskDescription.html");
    private final static File TEST_BINARY_FILE = new File("src/test/resources/task/specialNumbers/FirstNumbers.pdf");
    private final static File TEST_PATTERN_FILE = new File("src/test/resources/task/specialNumbers/TaskPattern.java");

    @Test
    @Order(1)
    public void createTask() throws IOException {

        // Get webresource
        WebResource wr = PhoenixTask.createResource(CLIENT, BASE_URL);

        List<PhoenixText> texts = new ArrayList<PhoenixText>();
        PhoenixText textFile = new PhoenixText(TEST_PATTERN_FILE, TEST_PATTERN_FILE.getName());
        texts.add(textFile);

        List<PhoenixAttachment> attachments = new ArrayList<PhoenixAttachment>();
        PhoenixAttachment binaryFile = new PhoenixAttachment(TEST_BINARY_FILE, TEST_BINARY_FILE.getName());
        attachments.add(binaryFile);

        String description = READER.read(TEST_DESCRIPTION_FILE);
        PhoenixTask task = new PhoenixTask(attachments, texts, description, TEST_TITLE);
        ClientResponse response = wr.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, task);
        assertEquals(Status.OK, response.getClientResponseStatus());
    }

    @Test
    @Order(2)
    public void getAllTasks() throws FileNotFoundException, CharacterCodingException {
        WebResource wr = CLIENT.resource(BASE_URL).path(PhoenixTask.WEB_RESOURCE_ROOT).path(PhoenixTask.WEB_RESOURCE_GET);
        ClientResponse response = wr.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, new SelectAllEntity<PhoenixTask>());
        assertEquals(Status.OK, response.getClientResponseStatus());

        List<PhoenixTask> tasks = EntityUtil.extractEntityList(response);
        assertEquals(1, tasks.size());

        PhoenixTask task = tasks.get(0);
        assertEquals(TEST_TITLE, task.getTitle());
        assertEquals(READER.read(TEST_DESCRIPTION_FILE), task.getDescription());

        List<PhoenixText> texts = task.getPattern();
        assertEquals(1, texts.size());

        PhoenixText text = texts.get(0);
        assertEquals(READER.read(TEST_PATTERN_FILE), text.getText());
    }

    @Test
    @Order(3)
    public void getTaskByTitle() {
        WebResource wr = PhoenixTask.getResource(CLIENT, BASE_URL);
        SelectEntity<PhoenixTask> selectByTitle = new SelectEntity<PhoenixTask>().addKey("title", TEST_TITLE);

        ClientResponse response = wr.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, selectByTitle);
        assertEquals(Status.OK, response.getClientResponseStatus());

        List<PhoenixTask> list = EntityUtil.extractEntityList(response);
        assertEquals(1, list.size());

        PhoenixTask task = list.get(0);
        assertNotNull(task);
        assertEquals(TEST_TITLE, task.getTitle());
    }

    private final static File TEST_SUBMISSION_FILE = new File("src/test/resources/task/specialNumbers/SpecialNumbers.java");

    @Test
    @Order(4)
    public void submitSolutionForManuelTask() throws IOException {
        WebResource wr = PhoenixTask.getResource(CLIENT, BASE_URL);
        SelectEntity<PhoenixTask> selectByTitle = new SelectEntity<PhoenixTask>().addKey("title", TEST_TITLE);

        ClientResponse response = wr.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, selectByTitle);
        assertEquals(Status.OK, response.getClientResponseStatus());

        PhoenixTask task = EntityUtil.extractEntity(response);

        PhoenixSubmission sub = new PhoenixSubmission(new ArrayList<File>(), Arrays.asList(TEST_SUBMISSION_FILE));
        wr = PhoenixTask.submitResource(CLIENT, BASE_URL);

        response = wr.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, KeyReader.createAddTo(task, Arrays.asList(sub)));
        assertEquals(Status.OK, response.getClientResponseStatus());

        PhoenixSubmissionResult result = response.getEntity(PhoenixSubmissionResult.class);
        assertEquals(SubmissionStatus.SUBMITTED, result.getStatus());
    }

    @Test
    @Order(5)
    public void getSubmissionForTask() throws IOException {

        SelectEntity<PhoenixTask> taskSelector = new SelectEntity<PhoenixTask>();
        taskSelector.addKey("title", TEST_TITLE);

        WebResource wrGetSubmissions = PhoenixSubmission.getResource(CLIENT, BASE_URL);

        SelectEntity<PhoenixSubmission> submissionSelector = new SelectEntity<PhoenixSubmission>();
        submissionSelector.addKey("task", taskSelector);

        ClientResponse response = wrGetSubmissions.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, submissionSelector);
        assertEquals(Status.OK, response.getClientResponseStatus());

        List<PhoenixSubmission> submissions = EntityUtil.extractEntityList(response);
        assertEquals(1, submissions.size());

        PhoenixSubmission phoenixSubmission = submissions.get(0);
        assertEquals(0, phoenixSubmission.getAttachments().size());
        assertEquals(1, phoenixSubmission.getTexts().size());
        assertEquals(TEST_SUBMISSION_FILE.getName(), phoenixSubmission.getTexts().get(0).getFullname());
    }

    @Test
    @Order(6)
    public void getAllTitles() {
        WebResource wr = PhoenixTask.getAllTitlesResource(CLIENT, BASE_URL);

        ClientResponse response = wr.type(MediaType.APPLICATION_JSON).get(ClientResponse.class);
        assertEquals(Status.OK, response.getClientResponseStatus());

        List<String> titles = EntityUtil.extractEntityList(response);

        assertEquals(1, titles.size());
        assertEquals(TEST_TITLE, titles.get(0));
    }

    @Test
    @Order(7)
    public void createDuplicateTask() {
        // Get webresource
        WebResource wr = PhoenixTask.createResource(CLIENT, BASE_URL);

        // Empty lists - we have not interest in lists for this test
        List<PhoenixText> texts = new ArrayList<PhoenixText>();
        List<PhoenixAttachment> attachments = new ArrayList<PhoenixAttachment>();

        // No interest in description
        String description = "";

        PhoenixTask task = new PhoenixTask(attachments, texts, description, TEST_TITLE);
        ClientResponse response = wr.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, task);

        // Because we want to create a title with same title, the system
        // throws an exception
        assertEquals(PhoenixStatusType.DUPLIATE_ENTITY.getStatusCode(), response.getStatus());
    }

    private final static String AUTOMATIC_TEST_TITLE = "TernarySearch";

    @Test
    @Order(8)
    public void createAutomaticTask() throws IOException {
        // Get webresource
        WebResource wr = PhoenixTask.createResource(CLIENT, BASE_URL);

        // The pattern for the submission
        List<PhoenixText> pattern = new ArrayList<PhoenixText>();
        pattern.add(new PhoenixText(new File("src/test/resources/task/ternarySearch/TernarySearch.java"), "TernarySearch.java"));

        // No attachments
        List<PhoenixAttachment> attachments = new ArrayList<PhoenixAttachment>();

        // No interest in description
        String description = "";

        // Create test information
        PhoenixTaskTest pTest = new PhoenixTaskTest(new PhoenixText(new File("src/test/resources/task/ternarySearch/TernarySearchTest.java"), "TernarySearchTest.java"));
        pTest.setTimeout(10);

        // Create automatic test
        PhoenixTask task = new PhoenixAutomaticTask(attachments, pattern, description, AUTOMATIC_TEST_TITLE, "java", Arrays.asList(pTest));

        // Disallow IO packages
        DisallowedContent disallowedContent = new DisallowedContent().disallow("java.io").disallow("java.nio");
        task.setDisallowedContent(disallowedContent);

        ClientResponse response = wr.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, task);
        assertEquals(ClientResponse.Status.OK, response.getClientResponseStatus());

    }

    @Test
    @Order(9)
    public void submitSolutionForAutoTask() throws IOException {

        WebResource wr = PhoenixTask.getResource(CLIENT, BASE_URL);
        SelectEntity<PhoenixTask> selectByTitle = new SelectEntity<PhoenixTask>().addKey("title", AUTOMATIC_TEST_TITLE);

        ClientResponse response = wr.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, selectByTitle);
        assertEquals(ClientResponse.Status.OK, response.getClientResponseStatus());

        PhoenixTask task = EntityUtil.extractEntity(response);

        PhoenixSubmission sub = new PhoenixSubmission(new ArrayList<File>(), Arrays.asList(new File("src/test/resources/task/ternarySearch/goodImpl/TernarySearch.java")));
        wr = PhoenixTask.submitResource(CLIENT, BASE_URL);
        response = wr.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, KeyReader.createAddTo(task, Arrays.asList(sub)));
        assertEquals(ClientResponse.Status.OK, response.getClientResponseStatus());

        PhoenixSubmissionResult res = response.getEntity(PhoenixSubmissionResult.class);

        assertEquals(ClientResponse.Status.OK, response.getClientResponseStatus());
        assertEquals(SubmissionStatus.OK, res.getStatus());

    }

    @Test
    @Order(10)
    public void submitSolutionWithInvalidContent() throws IOException {
        WebResource wr = PhoenixTask.getResource(CLIENT, BASE_URL);
        SelectEntity<PhoenixTask> selectByTitle = new SelectEntity<PhoenixTask>().addKey("title", AUTOMATIC_TEST_TITLE);

        ClientResponse response = wr.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, selectByTitle);
        assertEquals(ClientResponse.Status.OK, response.getClientResponseStatus());

        PhoenixTask task = EntityUtil.extractEntity(response);

        PhoenixSubmission sub = new PhoenixSubmission(new ArrayList<File>(), Arrays.asList(new File("src/test/resources/task/ternarySearch/badImpl/TernarySearch.java")));
        wr = PhoenixTask.submitResource(CLIENT, BASE_URL);

        response = wr.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, KeyReader.createAddTo(task, Arrays.asList(sub)));
        assertEquals(ClientResponse.Status.OK, response.getClientResponseStatus());

        PhoenixSubmissionResult res = response.getEntity(PhoenixSubmissionResult.class);
        assertEquals(SubmissionStatus.ERROR, res.getStatus());
        assertEquals("Code can not use java.io", res.getStatusText());

    }

    @Test
    @Order(11)
    public void searchNonExistingTask() {
        WebResource getAllTasksResource = PhoenixTask.getResource(CLIENT, BASE_URL);
        ClientResponse response = getAllTasksResource.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, new SelectEntity<PhoenixTask>().addKey("title", "troll"));
        assertEquals(Status.NOT_FOUND, response.getClientResponseStatus());
    }
}
