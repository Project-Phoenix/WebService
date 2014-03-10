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

package de.phoenix.task;

import static de.phoenix.database.EntityTest.BASE_URL;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.ws.rs.core.MediaType;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.ClientResponse.Status;
import com.sun.jersey.api.client.WebResource;

import de.phoenix.junit.OrderedRunner;
import de.phoenix.junit.OrderedRunner.Order;
import de.phoenix.rs.EntityUtil;
import de.phoenix.rs.PhoenixClient;
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
public class TaskTest {

    private final static String TEST_TITLE = "Befreundete Zahlen";
    private final static File TEST_DESCRIPTION_FILE = new File("src/test/resources/task/specialNumbers/TaskDescription.html");
    private final static File TEST_BINARY_FILE = new File("src/test/resources/task/specialNumbers/FirstNumbers.pdf");
    private final static File TEST_PATTERN_FILE = new File("src/test/resources/task/specialNumbers/TaskPattern.java");

    @Test
    @Order(1)
    public void createTask() {

        if (!TEST_BINARY_FILE.exists()) {
            fail("Binary file does not exists!");
        }

        if (!TEST_PATTERN_FILE.exists()) {
            fail("Text file does not exists!");
        }

        if (!TEST_DESCRIPTION_FILE.exists()) {
            fail("Task Description File does not exists!");
        }

        // Create client
        Client c = PhoenixClient.create();
        // Get webresource
        WebResource wr = PhoenixTask.createResource(c, BASE_URL);
        try {

            List<PhoenixText> texts = new ArrayList<PhoenixText>();
            PhoenixText textFile = new PhoenixText(TEST_PATTERN_FILE, TEST_PATTERN_FILE.getName());
            texts.add(textFile);

            List<PhoenixAttachment> attachments = new ArrayList<PhoenixAttachment>();
            PhoenixAttachment binaryFile = new PhoenixAttachment(TEST_BINARY_FILE, TEST_BINARY_FILE.getName());
            attachments.add(binaryFile);

            String description = getText(TEST_DESCRIPTION_FILE);

            PhoenixTask task = new PhoenixTask(attachments, texts, description, TEST_TITLE);
            ClientResponse post = wr.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, task);
            assertTrue(post.toString(), post.getStatus() == 200);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }

    }

    private String getText(File file) {
        StringBuilder sBuilder = new StringBuilder((int) file.length());
        try {
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
            byte[] buffer = new byte[2048];
            int read = 0;
            while ((read = bis.read(buffer)) != -1) {
                sBuilder.append(new String(buffer, 0, read));
            }

            bis.close();
            return sBuilder.toString();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Test
    @Order(2)
    public void getAllTasks() {
        Client c = PhoenixClient.create();
        WebResource wr = c.resource(BASE_URL).path(PhoenixTask.WEB_RESOURCE_ROOT).path(PhoenixTask.WEB_RESOURCE_GET);
        ClientResponse resp = wr.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, new SelectAllEntity<PhoenixTask>());

        List<PhoenixTask> tasks = EntityUtil.extractEntityList(resp);

        assertFalse("TaskList is empty!", tasks.isEmpty());
        for (PhoenixTask phoenixTask : tasks) {
            assertTrue(phoenixTask.getTitle(), phoenixTask.getTitle().equals(TEST_TITLE));
            assertFalse(phoenixTask.getDescription(), phoenixTask.getDescription().isEmpty());
            List<PhoenixText> pattern = phoenixTask.getPattern();
            assertFalse("PatternList is empty!", pattern.isEmpty());
            for (PhoenixText pat : pattern) {
                assertFalse("Patterntext is empty!", pat.getText().isEmpty());
            }
        }
    }

    @Test
    @Order(3)
    public void getTaskByTitle() {

        Client c = PhoenixClient.create();
        WebResource wr = PhoenixTask.getResource(c, BASE_URL);
        SelectEntity<PhoenixTask> selectByTitle = new SelectEntity<PhoenixTask>().addKey("title", TEST_TITLE);

        ClientResponse post = wr.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, selectByTitle);
        assertTrue(post.toString(), post.getStatus() == 200);

        List<PhoenixTask> list = EntityUtil.extractEntityList(post);
        assertFalse("List is empty!", list.isEmpty());
        assertTrue("List contains not only one task!", list.size() == 1);

        PhoenixTask task = list.get(0);
        assertFalse("No task found!", task == null);
        assertTrue("Task title wrong!", task.getTitle().equals(TEST_TITLE));
    }

    private final static File TEST_SUBMISSION_FILE = new File("src/test/resources/task/specialNumbers/SpecialNumbers.java");

    @Test
    @Order(4)
    public void submitSolutionForManuelTask() {

        if (!TEST_SUBMISSION_FILE.exists()) {
            fail("Submission File does not exists!");
        }

        Client c = PhoenixClient.create();
        WebResource wr = PhoenixTask.getResource(c, BASE_URL);
        SelectEntity<PhoenixTask> selectByTitle = new SelectEntity<PhoenixTask>().addKey("title", TEST_TITLE);

        ClientResponse post = wr.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, selectByTitle);
        assertTrue(post.toString(), post.getStatus() == 200);

        PhoenixTask task = EntityUtil.extractEntity(post);

        try {
            PhoenixSubmission sub = new PhoenixSubmission(new ArrayList<File>(), Arrays.asList(TEST_SUBMISSION_FILE));
            wr = PhoenixTask.submitResource(c, BASE_URL);
            post = wr.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, KeyReader.createAddTo(task, sub));

            assertTrue(post.toString(), post.getStatus() == 200);
            PhoenixSubmissionResult res = post.getEntity(PhoenixSubmissionResult.class);
            assertTrue(res.getStatus().equals(SubmissionStatus.SUBMITTED));
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    @Order(5)
    public void getSubmissionForTask() throws IOException {

        Client c = PhoenixClient.create();

        SelectEntity<PhoenixTask> taskSelector = new SelectEntity<PhoenixTask>();
        taskSelector.addKey("title", TEST_TITLE);

        WebResource wrGetSubmissions = PhoenixSubmission.getResource(c, BASE_URL);

        SelectEntity<PhoenixSubmission> submissionSelector = new SelectEntity<PhoenixSubmission>();
        submissionSelector.addKey("task", taskSelector);

        ClientResponse post = wrGetSubmissions.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, submissionSelector);
        assertTrue(post.toString(), post.getStatus() == 200);

        List<PhoenixSubmission> submissions = EntityUtil.extractEntityList(post);
        assertFalse("Result is empty!", submissions.isEmpty());
        for (PhoenixSubmission phoenixSubmission : submissions) {
            assertTrue(phoenixSubmission.getAttachments().size() + "", phoenixSubmission.getAttachments().size() == 0);
            assertTrue(phoenixSubmission.getTexts().size() + "", phoenixSubmission.getTexts().size() == 1);
            PhoenixText t = phoenixSubmission.getTexts().get(0);
            assertTrue((t.getName() + "." + t.getType()) + " not equals" + TEST_SUBMISSION_FILE.getName(), (t.getName() + "." + t.getType()).equals(TEST_SUBMISSION_FILE.getName()));

        }

    }

    @Test
    @Order(6)
    public void getAllTitles() {
        Client c = PhoenixClient.create();
        WebResource wr = PhoenixTask.getAllTitlesResource(c, BASE_URL);

        ClientResponse post = wr.type(MediaType.APPLICATION_JSON).get(ClientResponse.class);
        assertTrue(post.toString(), post.getStatus() == 200);

        List<String> titles = EntityUtil.extractEntityList(post);

        assertFalse("Title list is empty!", titles.isEmpty());
        assertTrue("Title list contain more than 0 elements , " + titles.size(), titles.size() == 1);
        assertTrue(titles.get(0) + " is not " + TEST_TITLE, titles.get(0).equals(TEST_TITLE));

    }

    @Test
    @Order(7)
    public void createDuplicateTask() {
        // Create client
        Client c = PhoenixClient.create();
        // Get webresource
        WebResource wr = PhoenixTask.createResource(c, BASE_URL);
        try {

            // Empty lists - we have not interest in lists for this test
            List<PhoenixText> texts = new ArrayList<PhoenixText>();
            List<PhoenixAttachment> attachments = new ArrayList<PhoenixAttachment>();

            // No interest in description
            String description = "";

            PhoenixTask task = new PhoenixTask(attachments, texts, description, TEST_TITLE);
            ClientResponse post = wr.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, task);

            // Because we want to create a title with same title, the system
            // throws an exception
            assertTrue(post.toString(), post.getStatus() == 400 && post.getEntity(String.class).equals("Duplicate task title!"));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    private final static String AUTOMATIC_TEST_TITLE = "TernarySearch";

    @Test
    @Order(8)
    public void createAutomaticTask() {
        // Create client
        Client c = PhoenixClient.create();
        // Get webresource
        WebResource wr = PhoenixTask.createResource(c, BASE_URL);
        try {

            // The pattern for the submission
            List<PhoenixText> pattern = new ArrayList<PhoenixText>();
            pattern.add(new PhoenixText(new File("src/test/resources/task/ternarySearch/TernarySearch.java"), "TernarySearch.java"));

            // No attachments
            List<PhoenixAttachment> attachments = new ArrayList<PhoenixAttachment>();

            // No interest in description
            String description = "";

            PhoenixTaskTest pTest = new PhoenixTaskTest(new PhoenixText(new File("src/test/resources/task/ternarySearch/TernarySearchTest.java"), "TernarySearchTest.java"));

            PhoenixTask task = new PhoenixAutomaticTask(attachments, pattern, description, AUTOMATIC_TEST_TITLE, "java", Arrays.asList(pTest));
            DisallowedContent disallowedContent = new DisallowedContent().disallow("java.io").disallow("java.nio");
            task.setDisallowedContent(disallowedContent);
            ClientResponse post = wr.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, task);

            assertEquals(ClientResponse.Status.OK, post.getClientResponseStatus());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    @Order(9)
    public void submitSolutionForAutoTask() {

        Client c = PhoenixClient.create();
        WebResource wr = PhoenixTask.getResource(c, BASE_URL);
        SelectEntity<PhoenixTask> selectByTitle = new SelectEntity<PhoenixTask>().addKey("title", AUTOMATIC_TEST_TITLE);

        ClientResponse post = wr.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, selectByTitle);
        assertEquals(ClientResponse.Status.OK, post.getClientResponseStatus());

        PhoenixTask task = EntityUtil.extractEntity(post);

        try {
            PhoenixSubmission sub = new PhoenixSubmission(new ArrayList<File>(), Arrays.asList(new File("src/test/resources/task/ternarySearch/MyTernarySearch.java")));
            wr = PhoenixTask.submitResource(c, BASE_URL);
            post = wr.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, KeyReader.createAddTo(task, sub));

            assertEquals(ClientResponse.Status.OK, post.getClientResponseStatus());
            PhoenixSubmissionResult res = post.getEntity(PhoenixSubmissionResult.class);

            assertEquals(ClientResponse.Status.OK, post.getClientResponseStatus());
            assertEquals(SubmissionStatus.OK, res.getStatus());
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    @Order(10)
    public void submitSolutionWithInvalidContent() {
        Client c = PhoenixClient.create();
        WebResource wr = PhoenixTask.getResource(c, BASE_URL);
        SelectEntity<PhoenixTask> selectByTitle = new SelectEntity<PhoenixTask>().addKey("title", AUTOMATIC_TEST_TITLE);

        ClientResponse post = wr.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, selectByTitle);
        assertEquals(ClientResponse.Status.OK, post.getClientResponseStatus());

        PhoenixTask task = EntityUtil.extractEntity(post);

        try {
            PhoenixSubmission sub = new PhoenixSubmission(new ArrayList<File>(), Arrays.asList(new File("src/test/resources/task/ternarySearch/MyMaliciousTernarySearch.java")));
            wr = PhoenixTask.submitResource(c, BASE_URL);
            post = wr.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, KeyReader.createAddTo(task, sub));

            assertEquals(ClientResponse.Status.OK, post.getClientResponseStatus());
            PhoenixSubmissionResult res = post.getEntity(PhoenixSubmissionResult.class);
            assertEquals(SubmissionStatus.ERROR, res.getStatus());
            assertEquals("Code can not use java.io", res.getStatusText());
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    @Order(11)
    public void searchNonExistingTask() {
        Client c = PhoenixClient.create();
        WebResource getAllTasksResource = PhoenixTask.getResource(c, BASE_URL);
        ClientResponse response = getAllTasksResource.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, new SelectEntity<PhoenixTask>().addKey("title", "troll"));
        assertEquals(Status.NOT_FOUND, response.getClientResponseStatus());
    }
}
