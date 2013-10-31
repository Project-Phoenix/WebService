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

import de.phoenix.DatabaseCleaner;
import de.phoenix.TestHttpServer;
import de.phoenix.rs.entity.PhoenixTask;
import de.phoenix.rs.entity.PhoenixText;
import de.phoenix.util.Updateable;

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
        DatabaseCleaner.getInstance().run();
    }

    private static String TEST_TITLE = "TestAufgabe";
    private static String TEST_DESCRIPTION = "Schauen Sie aus dem Fenster";

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
        WebResource wr = c.resource(BASE_URI).path(PhoenixTask.WEB_RESOURCE_ROOT).path(PhoenixTask.WEB_RESOURCE_CREATE);
        try {
            PhoenixTask task = new PhoenixTask(TEST_TITLE, TEST_DESCRIPTION, ats, texts);
            ClientResponse post = task.send(wr);
            assertTrue(post.toString(), post.getStatus() == 200);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }

        WebResource wr2 = c.resource(BASE_URI).path(PhoenixTask.WEB_RESOURCE_ROOT).path(PhoenixTask.WEB_RESOURCE_GETALL);
        ClientResponse resp = wr2.type(MediaType.APPLICATION_JSON).get(ClientResponse.class);

        // Ugly constructs to receive lists of generic types - no other way to
        // solve this
        GenericType<List<PhoenixTask>> genericPTask = new GenericType<List<PhoenixTask>>() {
        };

        List<PhoenixTask> tasks = resp.getEntity(genericPTask);

        assertFalse("TaskList is empty!", tasks.isEmpty());
        for (PhoenixTask phoenixTask : tasks) {
            assertTrue(phoenixTask.getTitle(), phoenixTask.getTitle().equals(TEST_TITLE));
            assertTrue(phoenixTask.getDescription(), phoenixTask.getDescription().equals(TEST_DESCRIPTION));
            List<PhoenixText> pattern = phoenixTask.getPattern();
            assertFalse("PatternList is empty!", pattern.isEmpty());
            for (PhoenixText pat : pattern) {
                assertFalse("Patterntext is empty!", pat.getText().isEmpty());
            }
        }

        WebResource wr3 = c.resource(BASE_URI).path(PhoenixTask.WEB_RESOURCE_ROOT).path(PhoenixTask.WEB_RESOURCE_UPDATE);

        try {
            PhoenixTask task = new PhoenixTask("Neuer Title", TEST_DESCRIPTION, ats, texts);
            Updateable<PhoenixTask, String> tmp = new Updateable<PhoenixTask, String>(task, TEST_TITLE);
            ClientResponse post = wr3.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, tmp);
            assertTrue(post.toString(), post.getStatus() == 200);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }
}
