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

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;

import de.phoenix.DatabaseCleaner;
import de.phoenix.TestHttpServer;
import de.phoenix.junit.OrderedRunner;
import de.phoenix.junit.OrderedRunner.Order;
import de.phoenix.rs.entity.PhoenixSubmission;
import de.phoenix.rs.entity.PhoenixTask;
import de.phoenix.rs.entity.PhoenixText;
import de.phoenix.util.Updateable;

@RunWith(OrderedRunner.class)
public class TaskTest {

    private final static String BASE_URI = "http://localhost:7766/rest";

    private static TestHttpServer httpServer;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        // Start Http Server
        httpServer = new TestHttpServer(BASE_URI);
        cleanupDatabase();
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        httpServer.stop();
    }

    private static void cleanupDatabase() {
        DatabaseCleaner.getInstance().run();
    }

    private static String TEST_TITLE = "Befreundete Zahlen";
    private static File TEST_DESCRIPTION_FILE = new File("src/test/resources/task/TaskDescription.html");
    private static File TEST_BINARY_FILE = new File("src/test/resources/task/FirstNumbers.pdf");
    private static File TEST_PATTERN_FILE = new File("src/test/resources/task/TaskPattern.java");

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

        // Lists for the task
        List<File> ats = new ArrayList<File>();
        List<File> texts = new ArrayList<File>();

        // Add elements for the task
        ats.add(TEST_BINARY_FILE);
        texts.add(TEST_PATTERN_FILE);

        // Create client
        Client c = Client.create();
        // Get webresource
        WebResource wr = c.resource(BASE_URI).path(PhoenixTask.WEB_RESOURCE_ROOT).path(PhoenixTask.WEB_RESOURCE_CREATE);
        try {
            PhoenixTask task = new PhoenixTask(TEST_TITLE, getText(TEST_DESCRIPTION_FILE), ats, texts);
            ClientResponse post = task.send(wr);
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
        Client c = Client.create();
        WebResource wr = c.resource(BASE_URI).path(PhoenixTask.WEB_RESOURCE_ROOT).path(PhoenixTask.WEB_RESOURCE_GETALL);
        ClientResponse resp = wr.type(MediaType.APPLICATION_JSON).get(ClientResponse.class);

        // Ugly constructs to receive lists of generic types - no other way to
        // solve this
        GenericType<List<PhoenixTask>> genericPTask = new GenericType<List<PhoenixTask>>() {
        };

        List<PhoenixTask> tasks = resp.getEntity(genericPTask);

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

        Client c = Client.create();
        WebResource wr = c.resource(BASE_URI).path(PhoenixTask.WEB_RESOURCE_ROOT).path(PhoenixTask.WEB_RESOURCE_GETBYTITLE);
        ClientResponse post = wr.post(ClientResponse.class, TEST_TITLE);
        assertTrue(post.toString(), post.getStatus() == 200);

        // Ugly constructs to receive lists of generic types - no other way to
        // solve this
        GenericType<List<PhoenixTask>> genericPTask = new GenericType<List<PhoenixTask>>() {
        };

        List<PhoenixTask> tasks = post.getEntity(genericPTask);
        assertFalse("Tasks are empty!", tasks.isEmpty());
        assertTrue(Integer.toString(tasks.size()), tasks.size() == 1);

        PhoenixTask task = tasks.get(0);
        assertTrue("Task does not exists!", task != null);
        assertTrue("Task title wrong!", task.getTitle().equals(TEST_TITLE));

    }

    @Test
    @Order(4)
    public void updateTask() {

        if (!TEST_BINARY_FILE.exists()) {
            fail("Binary file does not exists!");
        }

        if (!TEST_PATTERN_FILE.exists()) {
            fail("Text file does not exists!");
        }

        // Lists for the task
        List<File> ats = new ArrayList<File>();
        List<File> texts = new ArrayList<File>();

        // Add elements for the task
        ats.add(TEST_BINARY_FILE);
        texts.add(TEST_PATTERN_FILE);
        Client c = Client.create();
        WebResource wr = c.resource(BASE_URI).path(PhoenixTask.WEB_RESOURCE_ROOT).path(PhoenixTask.WEB_RESOURCE_UPDATE);

        try {
            PhoenixTask task = new PhoenixTask("Neuer Title", getText(TEST_DESCRIPTION_FILE), ats, texts);
            Updateable<PhoenixTask, String> tmp = new Updateable<PhoenixTask, String>(task, TEST_TITLE);
            ClientResponse post = wr.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, tmp);
            assertTrue(post.toString(), post.getStatus() == 200);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    private static File TEST_SUBMISSION_FILE = new File("src/test/resources/task/TaskSubmission.java");

    @Test
    @Order(5)
    public void submitSolution() {

        if (!TEST_SUBMISSION_FILE.exists()) {
            fail("Submission File does not exists!");
        }

        Client c = Client.create();
        WebResource wr = c.resource(BASE_URI).path(PhoenixTask.WEB_RESOURCE_ROOT).path(PhoenixTask.WEB_RESOURCE_GETBYTITLE);
        ClientResponse post = wr.post(ClientResponse.class, TEST_TITLE);
        assertTrue(post.toString(), post.getStatus() == 200);

        // Ugly constructs to receive lists of generic types - no other way to
        // solve this
        GenericType<List<PhoenixTask>> genericPTask = new GenericType<List<PhoenixTask>>() {
        };

        List<PhoenixTask> tasks = post.getEntity(genericPTask);
        assertFalse("Tasks are empty!", tasks.isEmpty());
        assertTrue(Integer.toString(tasks.size()), tasks.size() == 1);

        PhoenixTask task = tasks.get(0);

        try {
            PhoenixSubmission sub = new PhoenixSubmission(task, Collections.<File> emptyList(), Collections.singletonList(TEST_SUBMISSION_FILE));
            wr = c.resource(BASE_URI).path(PhoenixSubmission.WEB_RESOURCE_ROOT).path(PhoenixSubmission.WEB_RESOURCE_SUBMIT);
            post = sub.send(wr);
            assertTrue(post.toString(), post.getStatus() == 200);
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    @Order(6)
    public void getSubmissionForTask() throws IOException {

        Client c = Client.create();
        WebResource wr = c.resource(BASE_URI).path(PhoenixSubmission.WEB_RESOURCE_ROOT).path(PhoenixSubmission.WEB_RESOURCE_GET_TASK_SUBMISSIONS);
        PhoenixTask phoenixTask = new PhoenixTask(TEST_TITLE, getText(TEST_DESCRIPTION_FILE), Collections.singletonList(TEST_BINARY_FILE), Collections.singletonList(TEST_PATTERN_FILE));

        ClientResponse post = wr.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, phoenixTask);
        assertTrue(post.toString(), post.getStatus() == 200);

        // Ugly constructs to receive lists of generic types - no other way to
        // solve this
        GenericType<List<PhoenixSubmission>> genericPTask = new GenericType<List<PhoenixSubmission>>() {
        };

        List<PhoenixSubmission> submissions = post.getEntity(genericPTask);
        assertFalse("Result is empty!", submissions.isEmpty());
        for (PhoenixSubmission phoenixSubmission : submissions) {
            assertTrue(phoenixSubmission.getAttachmentsSize() + "", phoenixSubmission.getAttachmentsSize() == 0);
            assertTrue(phoenixSubmission.getTextsSize() + "", phoenixSubmission.getTextsSize() == 1);
            PhoenixText t = phoenixSubmission.getTexts().get(0);
            assertTrue((t.getName() + "." + t.getType()) + " not equals" + TEST_SUBMISSION_FILE.getName(), (t.getName() + "." + t.getType()).equals(TEST_SUBMISSION_FILE.getName()));
        }

    }
}
