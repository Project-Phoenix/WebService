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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.ws.rs.core.MediaType;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import de.phoenix.DatabaseCleaner;
import de.phoenix.DatabaseTestData;
import de.phoenix.TestHttpServer;
import de.phoenix.rs.PhoenixClient;
import de.phoenix.rs.entity.PhoenixAttachment;
import de.phoenix.rs.entity.PhoenixAutomaticTask;
import de.phoenix.rs.entity.PhoenixSubmission;
import de.phoenix.rs.entity.PhoenixSubmissionResult;
import de.phoenix.rs.entity.PhoenixSubmissionResult.SubmissionStatus;
import de.phoenix.rs.entity.PhoenixTask;
import de.phoenix.rs.entity.PhoenixText;
import de.phoenix.util.RSLists;

//This lecture test is based on the old api and should not used for an API quickblick
@Deprecated
public class OldTaskTest {

    private final static String BASE_URI = "http://localhost:7766/rest";

    private static TestHttpServer httpServer;

    public static void setUpBeforeClass() throws Exception {
        DatabaseCleaner.getInstance().run();
        // Start Http Server
        httpServer = new TestHttpServer(BASE_URI);
    }

    public static void tearDownAfterClass() throws Exception {
        httpServer.stop();

        DatabaseCleaner.getInstance().run();
        DatabaseTestData.getInstance().createTestData();
        deleteAllClassFiles();
    }

    private static void deleteAllClassFiles() {
        File dir = new File(".");
        File[] files = dir.listFiles();
        if (files == null)
            return;
        for (int i = 0; i < files.length; ++i) {
            File file = files[i];
            if (file.getName().endsWith(".class"))
                file.delete();
        }
    }

    private final static String TEST_TITLE = "Befreundete Zahlen";
    private final static File TEST_DESCRIPTION_FILE = new File("src/test/resources/task/specialNumbers/TaskDescription.html");
    private final static File TEST_BINARY_FILE = new File("src/test/resources/task/specialNumbers/FirstNumbers.pdf");
    private final static File TEST_PATTERN_FILE = new File("src/test/resources/task/specialNumbers/TaskPattern.java");

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
        WebResource wr = c.resource(BASE_URI).path(PhoenixTask.WEB_RESOURCE_ROOT).path(PhoenixTask.WEB_RESOURCE_CREATE);
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

    public void getAllTasks() {
        Client c = PhoenixClient.create();
        WebResource wr = c.resource(BASE_URI).path(PhoenixTask.WEB_RESOURCE_ROOT).path(PhoenixTask.WEB_RESOURCE_GETALL);
        ClientResponse resp = wr.type(MediaType.APPLICATION_JSON).get(ClientResponse.class);

        List<PhoenixTask> tasks = PhoenixTask.fromSendableList(resp);

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

    public void getTaskByTitle() {

        Client c = PhoenixClient.create();
        WebResource wr = c.resource(BASE_URI).path(PhoenixTask.WEB_RESOURCE_ROOT).path(PhoenixTask.WEB_RESOURCE_GETBYTITLE);
        ClientResponse post = wr.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, TEST_TITLE);
        assertTrue(post.toString(), post.getStatus() == 200);

        PhoenixTask task = post.getEntity(PhoenixTask.class);
        assertFalse("No task found!", task == null);
        assertTrue("Task title wrong!", task.getTitle().equals(TEST_TITLE));
    }

    private final static File TEST_SUBMISSION_FILE = new File("src/test/resources/task/specialNumbers/SpecialNumbers.java");

    public void submitSolution() {

        if (!TEST_SUBMISSION_FILE.exists()) {
            fail("Submission File does not exists!");
        }

        Client c = PhoenixClient.create();
        WebResource wr = c.resource(BASE_URI).path(PhoenixTask.WEB_RESOURCE_ROOT).path(PhoenixTask.WEB_RESOURCE_GETBYTITLE);
        ClientResponse post = wr.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, TEST_TITLE);
        assertTrue(post.toString(), post.getStatus() == 200);

        PhoenixTask task = post.getEntity(PhoenixTask.class);
        assertFalse("No task found!", task == null);

        try {
            PhoenixSubmission sub = new PhoenixSubmission(task, Collections.<File> emptyList(), Collections.singletonList(TEST_SUBMISSION_FILE));
            wr = c.resource(BASE_URI).path(PhoenixSubmission.WEB_RESOURCE_ROOT).path(PhoenixSubmission.WEB_RESOURCE_SUBMIT);
            post = wr.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, sub);
            assertTrue(post.toString(), post.getStatus() == 200);
            PhoenixSubmissionResult res = post.getEntity(PhoenixSubmissionResult.class);
            assertTrue(res.getStatus().equals(SubmissionStatus.SUBMITTED));
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }
    }

    public void getSubmissionForTask() throws IOException {

        Client c = PhoenixClient.create();
        WebResource wrGetTask = c.resource(BASE_URI).path(PhoenixTask.WEB_RESOURCE_ROOT).path(PhoenixTask.WEB_RESOURCE_GETBYTITLE);

        ClientResponse post = wrGetTask.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, TEST_TITLE);
        PhoenixTask phoenixTask = post.getEntity(PhoenixTask.class);

        WebResource wrGetSubmissions = c.resource(BASE_URI).path(PhoenixSubmission.WEB_RESOURCE_ROOT).path(PhoenixSubmission.WEB_RESOURCE_GET_TASK_SUBMISSIONS);

        post = wrGetSubmissions.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, phoenixTask);
        assertTrue(post.toString(), post.getStatus() == 200);

        List<PhoenixSubmission> submissions = PhoenixSubmission.fromSendableList(post);
        assertFalse("Result is empty!", submissions.isEmpty());
        for (PhoenixSubmission phoenixSubmission : submissions) {
            assertTrue(phoenixSubmission.getAttachments().size() + "", phoenixSubmission.getAttachments().size() == 0);
            assertTrue(phoenixSubmission.getTexts().size() + "", phoenixSubmission.getTexts().size() == 1);
            PhoenixText t = phoenixSubmission.getTexts().get(0);
            assertTrue((t.getName() + "." + t.getType()) + " not equals" + TEST_SUBMISSION_FILE.getName(), (t.getName() + "." + t.getType()).equals(TEST_SUBMISSION_FILE.getName()));

        }

    }

    public void getAllTitles() {
        Client c = PhoenixClient.create();
        WebResource wr = c.resource(BASE_URI).path(PhoenixTask.WEB_RESOURCE_ROOT).path(PhoenixTask.WEB_RESOURCE_GETALL_TITLES);

        ClientResponse post = wr.type(MediaType.APPLICATION_JSON).get(ClientResponse.class);
        assertTrue(post.toString(), post.getStatus() == 200);

        List<String> titles = RSLists.fromSendableStringList(post);

        assertFalse("Title list is empty!", titles.isEmpty());
        assertTrue("Title list contain more than 0 elements , " + titles.size(), titles.size() == 1);
        assertTrue(titles.get(0) + " is not " + TEST_TITLE, titles.get(0).equals(TEST_TITLE));

    }

    public void createDuplicateTask() {
        // Create client
        Client c = PhoenixClient.create();
        // Get webresource
        WebResource wr = c.resource(BASE_URI).path(PhoenixTask.WEB_RESOURCE_ROOT).path(PhoenixTask.WEB_RESOURCE_CREATE);
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

    public void createAutomaticTask() {
        // Create client
        Client c = PhoenixClient.create();
        // Get webresource
        WebResource wr = c.resource(BASE_URI).path(PhoenixTask.WEB_RESOURCE_ROOT).path(PhoenixTask.WEB_RESOURCE_CREATE);
        try {

            // Empty lists - we have not interest in lists for this test
            List<PhoenixText> texts = new ArrayList<PhoenixText>();
            List<PhoenixAttachment> attachments = new ArrayList<PhoenixAttachment>();

            // No interest in description
            String description = "";

            PhoenixTask task = new PhoenixAutomaticTask(attachments, texts, description, AUTOMATIC_TEST_TITLE, "java", new ArrayList<PhoenixText>());
            ClientResponse post = wr.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, task);

            assertTrue(post.toString(), post.getStatus() == 200);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }
}