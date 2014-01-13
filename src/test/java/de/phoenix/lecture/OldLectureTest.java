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

import java.io.File;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;

import javax.ws.rs.core.MediaType;

import org.joda.time.DateTimeConstants;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.joda.time.Period;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import de.phoenix.DatabaseCleaner;
import de.phoenix.DatabaseTestData;
import de.phoenix.TestHttpServer;
import de.phoenix.rs.PhoenixClient;
import de.phoenix.rs.entity.PhoenixDetails;
import de.phoenix.rs.entity.PhoenixLecture;
import de.phoenix.rs.entity.PhoenixLectureGroup;
import de.phoenix.rs.key.SelectEntity;

// This lecture test is based on the old api and should not used for an API quickblick
@Deprecated
public class OldLectureTest {

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

    private final String TEST_LECTURE_TITLE = "Einf";

    public void createLecture() throws ParseException {
        Client c = PhoenixClient.create();
        WebResource ws = c.resource(BASE_URI).path(PhoenixLecture.WEB_RESOURCE_ROOT).path(PhoenixLecture.WEB_RESOURCE_CREATE);

        LocalTime startTime = new LocalTime(11, 15);
        LocalTime endTime = new LocalTime(12, 45);
        LocalDate startDate = new LocalDate(2013, 10, 01);
        LocalDate endDate = new LocalDate(2014, 03, 31);
        PhoenixDetails detail = new PhoenixDetails("G29-336", DateTimeConstants.MONDAY, startTime, endTime, Period.weeks(1), startDate, endDate);

        PhoenixLecture lecture = new PhoenixLecture(TEST_LECTURE_TITLE, Arrays.asList(detail));

        ClientResponse response = ws.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, lecture);

        assertTrue(response.toString(), response.getStatus() == 200);
    }

    public void getLectures() {
        Client c = PhoenixClient.create();
        WebResource ws = c.resource(BASE_URI).path(PhoenixLecture.WEB_RESOURCE_ROOT).path(PhoenixLecture.WEB_RESOURCE_GETALL);

        ClientResponse response = ws.type(MediaType.APPLICATION_JSON).get(ClientResponse.class);
        assertTrue(response.toString(), response.getStatus() == 200);

        List<PhoenixLecture> lectures = PhoenixLecture.fromSendableList(response);
        assertFalse("Lecture list is empty!", lectures.isEmpty());
        PhoenixLecture lec = lectures.get(0);
        assertTrue("LectureName was " + lec.getTitle() + ", but should be" + TEST_LECTURE_TITLE, lec.getTitle().equals(TEST_LECTURE_TITLE));
        assertTrue("Intervall is not 1 weeks, but " + lec.getLectureDetails().get(0).getInverval(), lec.getLectureDetails().get(0).getInverval().getWeeks() == 1);

    }

    private static final String TEST_GROUP_NAME = "Gruppe 2";
    private static final int TEST_GROUP_MAX_SIZE = 22;

    public void addGroup() {
        Client c = PhoenixClient.create();
        WebResource ws = c.resource(BASE_URI).path(PhoenixLecture.WEB_RESOURCE_ROOT).path(PhoenixLecture.WEB_RESOURCE_GETALL);

        // Get created test lecture to assign to lecture group
        ClientResponse response = ws.type(MediaType.APPLICATION_JSON).get(ClientResponse.class);
        assertTrue(response.toString(), response.getStatus() == 200);
        List<PhoenixLecture> lectures = PhoenixLecture.fromSendableList(response);
        PhoenixLecture lec = lectures.get(0);

        WebResource ws2 = c.resource(BASE_URI).path(PhoenixLectureGroup.WEB_RESOURCE_ROOT).path(PhoenixLectureGroup.WEB_RESOURCE_CREATE);

        // Create information for the group information
        LocalTime startTime = new LocalTime(15, 00);
        LocalTime endTime = new LocalTime(16, 30);
        LocalDate startDate = new LocalDate(2013, 10, 21);
        LocalDate endDate = new LocalDate(2014, 01, 27);

        PhoenixDetails detail = new PhoenixDetails("G29-K058", DateTimeConstants.MONDAY, startTime, endTime, Period.weeks(1), startDate, endDate);

        // Create a test group with
        // name = Gruppe 2
        // members = 22
        // default submission day is Monday
        // default submission time on Monday is 10 o'clock
        // In the room G29-k058 and other details described above
        // and the assigned lecture
        PhoenixLectureGroup group = new PhoenixLectureGroup(TEST_GROUP_NAME, TEST_GROUP_MAX_SIZE, DateTimeConstants.MONDAY, new LocalTime(10, 00), Arrays.asList(detail), lec);

        response = ws2.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, group);

        assertTrue(response.toString(), response.getStatus() == 200);
    }

    public void getLecture() {
        Client c = PhoenixClient.create();
        WebResource get = c.resource(BASE_URI).path(PhoenixLecture.WEB_RESOURCE_ROOT).path(PhoenixLecture.WEB_RESOURCE_GET);

        // Create select object from the phoenix lecture
        SelectEntity<PhoenixLecture> selectObject = new SelectEntity<PhoenixLecture>();
        selectObject.addKey("title", TEST_LECTURE_TITLE);

        // Search for the object - can match many
        ClientResponse response = get.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, selectObject);
        assertTrue(response.toString(), response.getStatus() == 200);
        List<PhoenixLecture> lectures = PhoenixLecture.fromSendableList(response);
        PhoenixLecture lec = lectures.get(0);
        assertTrue(lec.getTitle() + " is not " + TEST_LECTURE_TITLE, lec.getTitle().equals(TEST_LECTURE_TITLE));
    }
}
