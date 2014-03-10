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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;

import javax.ws.rs.core.MediaType;

import org.joda.time.DateTimeConstants;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.joda.time.Period;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.ClientResponse.Status;
import com.sun.jersey.api.client.WebResource;

import de.phoenix.junit.OrderedRunner;
import de.phoenix.junit.OrderedRunner.Order;
import de.phoenix.rs.EntityUtil;
import de.phoenix.rs.entity.PhoenixDetails;
import de.phoenix.rs.entity.PhoenixLecture;
import de.phoenix.rs.entity.PhoenixLectureGroup;
import de.phoenix.rs.key.AddToEntity;
import de.phoenix.rs.key.KeyReader;
import de.phoenix.rs.key.SelectAllEntity;
import de.phoenix.rs.key.SelectEntity;

@RunWith(OrderedRunner.class)
public class LectureTests {

    private final String TEST_LECTURE_TITLE = "Einführung in die Informatik";

    @Test
    @Order(1)
    public void createLecture() throws ParseException {
        WebResource ws = PhoenixLecture.createResource(CLIENT, BASE_URL);

        LocalTime startTime = new LocalTime(11, 15);
        LocalTime endTime = new LocalTime(12, 45);
        LocalDate startDate = new LocalDate(2013, 10, 01);
        LocalDate endDate = new LocalDate(2014, 03, 31);
        PhoenixDetails detail = new PhoenixDetails("G29-336", DateTimeConstants.MONDAY, startTime, endTime, Period.weeks(1), startDate, endDate);

        PhoenixLecture lecture = new PhoenixLecture(TEST_LECTURE_TITLE, Arrays.asList(detail));

        ClientResponse response = ws.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, lecture);
        assertEquals(Status.OK, response.getClientResponseStatus());
    }

    @Test
    @Order(2)
    public void getLectures() {
        WebResource ws = PhoenixLecture.getResource(CLIENT, BASE_URL);
        ClientResponse response = ws.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, new SelectAllEntity<PhoenixLecture>());
        assertEquals(Status.OK, response.getClientResponseStatus());

        List<PhoenixLecture> lectures = EntityUtil.extractEntityList(response);
        assertFalse("Lecture list is empty!", lectures.isEmpty());

        PhoenixLecture lec = lectures.get(0);
        assertEquals(TEST_LECTURE_TITLE, lec.getTitle());
        assertEquals(lec.getLectureDetails().get(0).getInverval(), lec.getLectureDetails().get(0).getInverval());

    }

    private static final String TEST_GROUP_NAME = "Gruppe 2";
    private static final int TEST_GROUP_MAX_SIZE = 22;

    @Test
    @Order(3)
    public void addGroup() {
        WebResource ws = PhoenixLecture.getResource(CLIENT, BASE_URL);

        // Get single lecture
        SelectEntity<PhoenixLecture> selectLecture = new SelectEntity<PhoenixLecture>().addKey("title", TEST_LECTURE_TITLE);
        ClientResponse response = ws.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, selectLecture);
        assertEquals(Status.OK, response.getClientResponseStatus());

        PhoenixLecture lec = EntityUtil.extractEntity(response);

        // Add group to lecture
        WebResource ws2 = PhoenixLecture.addGroupResource(CLIENT, BASE_URL);

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

        response = ws2.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, KeyReader.createAddTo(lec, group));
        assertEquals(Status.OK, response.getClientResponseStatus());

        // Create second group
        group = new PhoenixLectureGroup(TEST_GROUP_NAME + "_Second", TEST_GROUP_MAX_SIZE, DateTimeConstants.MONDAY, new LocalTime(10, 00), Arrays.asList(detail), lec);

        response = ws2.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, KeyReader.createAddTo(lec, group));
        assertEquals(Status.OK, response.getClientResponseStatus());
    }

    @Test
    @Order(4)
    public void addDetail() {
        WebResource getLectureResource = PhoenixLecture.getResource(CLIENT, BASE_URL);

        // Get single lecture
        SelectEntity<PhoenixLecture> selectLecture = new SelectEntity<PhoenixLecture>().addKey("title", TEST_LECTURE_TITLE);
        ClientResponse response = getLectureResource.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, selectLecture);
        assertEquals(Status.OK, response.getClientResponseStatus());

        PhoenixLecture lec = EntityUtil.extractEntity(response);

        // Create information for the group information
        LocalTime startTime = new LocalTime(2, 30);
        LocalTime endTime = new LocalTime(4, 00);
        LocalDate startDate = new LocalDate(2013, 10, 21);
        LocalDate endDate = new LocalDate(2014, 01, 27);

        PhoenixDetails detail = new PhoenixDetails("G29-K058", DateTimeConstants.WEDNESDAY, startTime, endTime, Period.weeks(2), startDate, endDate);

        WebResource addDetailToLectureResource = PhoenixLecture.addDetailResource(CLIENT, BASE_URL);
        response = addDetailToLectureResource.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, KeyReader.createAddTo(lec, detail));
        assertEquals(Status.OK, response.getClientResponseStatus());
    }

    @Test
    @Order(5)
    public void addDetailToNotExistingLecture() {

        // Create information for the group information
        LocalTime startTime = new LocalTime(2, 30);
        LocalTime endTime = new LocalTime(4, 00);
        LocalDate startDate = new LocalDate(2013, 10, 21);
        LocalDate endDate = new LocalDate(2014, 01, 27);

        PhoenixDetails detail = new PhoenixDetails("G29-K058", DateTimeConstants.WEDNESDAY, startTime, endTime, Period.weeks(2), startDate, endDate);

        // Add something to a non existing lecture
        AddToEntity<PhoenixLecture, PhoenixDetails> addDetailToLecture = new AddToEntity<PhoenixLecture, PhoenixDetails>(detail).addKey("title", "troll");

        WebResource addDetailToLectureResource = PhoenixLecture.addDetailResource(CLIENT, BASE_URL);
        ClientResponse response = addDetailToLectureResource.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, addDetailToLecture);
        assertEquals(Status.NOT_FOUND, response.getClientResponseStatus());
    }

    @Test
    @Order(6)
    public void getGroupsForALecture() throws IllegalArgumentException, IOException {

        WebResource ws = PhoenixLectureGroup.getResource(CLIENT, BASE_URL);

        SelectEntity<PhoenixLectureGroup> groupSelector = new SelectEntity<PhoenixLectureGroup>();
        SelectEntity<PhoenixLecture> lectureSelector = new SelectEntity<PhoenixLecture>().addKey("title", TEST_LECTURE_TITLE);

        groupSelector.addKey("lecture", lectureSelector);

        ClientResponse response = ws.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, groupSelector);
        assertEquals(Status.OK, response.getClientResponseStatus());

        List<PhoenixLectureGroup> groups = EntityUtil.extractEntityList(response);
        assertFalse("Lecture list is empty!", groups.isEmpty());
    }

    @Test
    @Order(7)
    public void getNoGroupsForNotALecture() throws IllegalArgumentException, IOException {

        WebResource ws = PhoenixLectureGroup.getResource(CLIENT, BASE_URL);

        SelectEntity<PhoenixLectureGroup> groupSelector = new SelectEntity<PhoenixLectureGroup>();
        // Not existing lecture
        SelectEntity<PhoenixLecture> lectureSelector = new SelectEntity<PhoenixLecture>().addKey("title", "NichtExistent");

        groupSelector.addKey("lecture", lectureSelector);

        ClientResponse response = ws.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, groupSelector);
        assertEquals(Status.NOT_FOUND, response.getClientResponseStatus());

        List<PhoenixLectureGroup> groups = EntityUtil.extractEntityList(response);
        assertNull("Elements in group", groups);
    }

    @Test
    @Order(8)
    public void deleteLectureGroup() {
        WebResource ws = PhoenixLectureGroup.deleteResource(CLIENT, BASE_URL);

        // Create Lecture Group Selector
        SelectEntity<PhoenixLectureGroup> groupSelector = new SelectEntity<PhoenixLectureGroup>();
        // Add Lecture Group Key - the name
        groupSelector.addKey("name", TEST_GROUP_NAME);

        // Create Lecture Selector
        SelectEntity<PhoenixLecture> lectureSelector = new SelectEntity<PhoenixLecture>();
        // Add Lecture Key - the title
        lectureSelector.addKey("title", TEST_LECTURE_TITLE);
        // Add lecture selector to group selector (only groups from this lecture
        // are selected)
        groupSelector.addKey("lecture", lectureSelector);

        // Send delete response
        ClientResponse response = ws.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, groupSelector);
        assertEquals(Status.OK, response.getClientResponseStatus());
    }
}
