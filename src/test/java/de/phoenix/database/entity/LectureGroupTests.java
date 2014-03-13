/*
 * Copyright (C) 2014 Project-Phoenix
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
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Arrays;

import javax.ws.rs.core.MediaType;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.joda.time.Period;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.ClientResponse.Status;
import com.sun.jersey.api.client.WebResource;

import de.phoenix.date.Weekday;
import de.phoenix.junit.OrderedRunner;
import de.phoenix.rs.EntityUtil;
import de.phoenix.rs.entity.PhoenixDetails;
import de.phoenix.rs.entity.PhoenixLecture;
import de.phoenix.rs.entity.PhoenixLectureGroup;
import de.phoenix.rs.key.AddToEntity;
import de.phoenix.rs.key.KeyReader;
import de.phoenix.rs.key.SelectEntity;
import de.phoenix.rs.key.UpdateEntity;

@RunWith(OrderedRunner.class)
public class LectureGroupTests {

    private static LocalTime nowTime;
    private static LocalDate nowDate;

    private static PhoenixLectureGroup pGroup;
    private static PhoenixLecture pLecture;

    @BeforeClass
    public static void beforeClass() {
        nowTime = LocalTime.now();
        nowDate = LocalDate.now();

        String groupTitle = "testGroup";

        // simple lecture
        PhoenixLecture pLecture = new PhoenixLecture("TestTitle", new ArrayList<PhoenixDetails>());
        WebResource res = PhoenixLecture.createResource(CLIENT, BASE_URL);
        ClientResponse response  = res.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, pLecture);
        assertEquals(Status.OK, response.getClientResponseStatus());

        // Sample Detail
        PhoenixDetails pDetail = new PhoenixDetails("testRoom", Weekday.MONDAY, nowTime, nowTime.plusHours(1), Period.days(1), nowDate, nowDate.plusDays(1));
        pGroup = new PhoenixLectureGroup(groupTitle, 1, Weekday.MONDAY, nowTime, Arrays.asList(pDetail));

        // Create group
        AddToEntity<PhoenixLecture, PhoenixLectureGroup> addToEntity = KeyReader.createAddTo(pLecture, Arrays.asList(pGroup));
        res = PhoenixLecture.addGroupResource(CLIENT, BASE_URL);
        response = res.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, addToEntity);
        assertEquals(Status.OK, response.getClientResponseStatus());

        // get group from database - just to be sure
        res = PhoenixLectureGroup.getResource(CLIENT, BASE_URL);
        SelectEntity<PhoenixLectureGroup> selectEntity = KeyReader.createSelect(pGroup);
        response = res.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, selectEntity);
        assertEquals(Status.OK, response.getClientResponseStatus());
        pGroup = EntityUtil.extractEntity(response);
        assertNotNull(pGroup);
    }

    @Test
    public void updateGroup() {
        // Change the weekday to Friday
        PhoenixLectureGroup pNewGroup = new PhoenixLectureGroup(pGroup.getName(), pGroup.getMaxMember(), Weekday.FRIDAY, pGroup.getSubmissionDeadlineTime(), pGroup.getDetails());

        UpdateEntity<PhoenixLectureGroup> updateEntity = KeyReader.createUpdate(pGroup, pNewGroup);

        WebResource resource = PhoenixLectureGroup.updateResource(CLIENT, BASE_URL);
        ClientResponse response = resource.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, updateEntity);
        assertEquals(Status.OK, response.getClientResponseStatus());

        SelectEntity<PhoenixLectureGroup> lectureGroupSelector = new SelectEntity<PhoenixLectureGroup>();
        lectureGroupSelector.addKey("name", pNewGroup.getName());
        lectureGroupSelector.addKey("lecture", pLecture);
        resource = PhoenixLectureGroup.getResource(CLIENT, BASE_URL);
        response = resource.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, lectureGroupSelector);

        PhoenixLectureGroup selectedGroup = EntityUtil.extractEntity(response);
        assertEquals(pNewGroup.getName(), selectedGroup.getName());
        assertEquals(Weekday.FRIDAY, selectedGroup.getSubmissionDeadlineWeekday());
    }
}
