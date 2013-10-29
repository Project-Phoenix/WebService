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

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.MediaType;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;

import de.phoenix.TestHttpServer;
import de.phoenix.rs.entity.PhoenixTask;
import de.phoenix.rs.entity.PhoenixText;

public class TaskTest {

    private final static String BASE_URI = "http://localhost:7766/rest";

    private static TestHttpServer httpServer;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        // Start Http Server
        httpServer = new TestHttpServer(BASE_URI);
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        httpServer.stop();
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

        List<File> ats = new ArrayList<File>();
        List<File> texts = new ArrayList<File>();

        File logo = new File("src/test/resources/PhoenixLogo2013.png");
        if (!logo.exists())
            fail("Logo does not exists");

        ats.add(logo);

        File permissionstxt = new File("src/test/resources/permissions.txt");
        texts.add(permissionstxt);

        // Create client
        Client c = Client.create();
        // Get webresource
        WebResource wr = c.resource(BASE_URI).path("task").path("create");
        try {
            PhoenixTask task = new PhoenixTask("TestAufgabe", ats, texts);
            ClientResponse post = task.send(wr);
            assertTrue(post.toString(), post.getStatus() == 200);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }

        WebResource wr2 = c.resource(BASE_URI).path("task").path("getAll");
        ClientResponse resp = wr2.type(MediaType.APPLICATION_JSON).get(ClientResponse.class);

        // Ugly constructs to receive lists of generic types - no other way to
        // solve this
        GenericType<List<PhoenixTask>> genericPTask = new GenericType<List<PhoenixTask>>() {
        };

        List<PhoenixTask> tasks = resp.getEntity(genericPTask);

        for (PhoenixTask phoenixTask : tasks) {
            System.out.println(phoenixTask.getDescription());
            List<PhoenixText> pattern = phoenixTask.getPattern();
            for (PhoenixText pat : pattern) {
                System.out.println(pat.getText());
            }
        }

    }

}
