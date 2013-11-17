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

package de.phoenix.lecture;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;
import java.util.Collections;
import java.util.List;

import javax.ws.rs.core.MediaType;

import org.joda.time.DateTimeConstants;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import de.phoenix.DatabaseCleaner;
import de.phoenix.TestHttpServer;
import de.phoenix.junit.OrderedRunner;
import de.phoenix.junit.OrderedRunner.Order;
import de.phoenix.rs.PhoenixClient;
import de.phoenix.rs.entity.PhoenixDetails;
import de.phoenix.rs.entity.PhoenixLecture;

@RunWith(OrderedRunner.class)
public class LectureTest {

    private final static String BASE_URI = "http://localhost:7766/rest";

    private static TestHttpServer httpServer;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        // Start Http Server
        httpServer = new TestHttpServer(BASE_URI);
        DatabaseCleaner.getInstance().run();
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        httpServer.stop();
    }

    private final String TEST_LECTURE_TITLE = "Einf";

    @Test
    @Order(1)
    public void createLecture() throws ParseException {
        Client c = PhoenixClient.create();
        WebResource ws = c.resource(BASE_URI).path(PhoenixLecture.WEB_RESOURCE_ROOT).path(PhoenixLecture.WEB_RESOURCE_CREATE);

        LocalTime startTime = new LocalTime(11, 15);
        LocalTime endTime = new LocalTime(12, 45);
        LocalDate startDate = new LocalDate(2013, 10, 01);
        LocalDate endDate = new LocalDate(2014, 03, 31);
        PhoenixDetails detail = new PhoenixDetails("G29-336", DateTimeConstants.MONDAY, startTime, endTime, "weekly", startDate, endDate);

        PhoenixLecture lecture = new PhoenixLecture(TEST_LECTURE_TITLE, Collections.singletonList(detail));

        ClientResponse response = ws.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, lecture);

        assertTrue(response.toString(), response.getStatus() == 200);
    }

    @Test
    @Order(2)
    public void getLectures() {
        Client c = PhoenixClient.create();
        WebResource ws = c.resource(BASE_URI).path(PhoenixLecture.WEB_RESOURCE_ROOT).path(PhoenixLecture.WEB_RESOURCE_GETALL);

        ClientResponse response = ws.type(MediaType.APPLICATION_JSON).get(ClientResponse.class);
        assertTrue(response.toString(), response.getStatus() == 200);

        List<PhoenixLecture> lectures = PhoenixLecture.fromSendableList(response);
        assertFalse("Lecture list is empty!", lectures.isEmpty());
        PhoenixLecture lec = lectures.get(0);
        assertTrue("LectureName was " + lec.getTitle() + ", but should be" + TEST_LECTURE_TITLE, lec.getTitle().equals(TEST_LECTURE_TITLE));

    }
}
