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

import static org.junit.Assert.fail;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.container.httpserver.HttpServerFactory;
import com.sun.net.httpserver.HttpServer;

import de.phoenix.rs.entity.PhoenixTask;
//import de.phoenix.database.entity.Tag;
//import de.phoenix.database.entity.TaskPool;

public class TaskTest {

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
        cleanupDatabase();
    }

    private static void cleanupDatabase() {
//        Session session = DatabaseManager.getInstance().openSession();
//        Transaction trans = session.beginTransaction();
//
//        Query q1 = session.getNamedQuery("TaskPool.findByName").setString("name", "Test Task");
//        TaskPool t = (TaskPool) q1.uniqueResult();
//        if (t == null)
//            fail("Object not inserted in database!");
//        List<Tag> tags = t.getTags();
//        for (Tag tag : tags) {
//            session.delete(tag);
//        }
//        session.delete(t);
//        trans.commit();
    }

    @Test
    public void createTask() {

        // Create client
        Client c = Client.create();
        // Get webresource
        WebResource wr = c.resource(BASE_URL).path("task").path("create");

        List<File> ats = new ArrayList<File>();
        List<File> texts = new ArrayList<File>();

//        ats.add(new File("src/test/resources/Pho"))
        File logo = new File("src/test/resources/PhoenixLogo2013.png");
        if (!logo.exists())
            fail("Logo does not exists");

        ats.add(logo);

        File permissionstxt = new File("src/test/resources/permissions.txt");
        texts.add(permissionstxt);

        PhoenixTask task = new PhoenixTask("TestAufgabe", ats, texts);
        
//        ClientResponse post = wr.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, task);
        ClientResponse post = task.send(wr);
        
        System.out.println(post);

//        // Create test task
//        TaskPool testTask = new TaskPool("Test Task", "This is a test description");
//
//        // Create test tags for the task
//        List<Tag> testTags = Arrays.asList(new Tag("Dev"), new Tag("Test"));
//        // Assign the tags to the task
//        testTask.setTags(testTags);
//
//        // Call Resource to store the task
//        ClientResponse cr = wr.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, testTask);
//
//        // TEST: DO NOT COPY
//        // Must be 200 when tasks are stored
//        assertTrue(cr.toString(), cr.getStatus() == 200);

    }

    @Test
    public void getTasks() {
//        // Create client
//        Client c = Client.create();
//        // Get webresource
//        WebResource wr = c.resource(BASE_URL).path("task").path("getAll");
//
//        // Call Resource to store the task
//        ClientResponse cr = wr.type(MediaType.APPLICATION_JSON).get(ClientResponse.class);
//
//        assertTrue(cr.toString(), cr.getStatus() == 200);
//
//        // Ugly constructs to receive lists of generic types - no other way to
//        // solve this
//        GenericType<List<TaskPool>> submissionType = new GenericType<List<TaskPool>>() {
//        };
//
//        List<TaskPool> result = cr.getEntity(submissionType);
//
//        for (TaskPool task : result) {
//
//            assertFalse(task.getName().isEmpty());
//            assertFalse(task.getDescription().isEmpty());
//
//            for (Tag tag : task.getTags()) {
//                assertFalse(tag.getTag().isEmpty());
//            }
//
//        }
    }

}
