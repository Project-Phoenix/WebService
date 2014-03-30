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

import static de.phoenix.database.EntityTest.CLIENT;
import static de.phoenix.database.EntityTest.BASE_URL;
import static org.junit.Assert.assertEquals;

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

import de.phoenix.date.Weekday;
import de.phoenix.junit.OrderedRunner;
import de.phoenix.rs.EntityUtil;
import de.phoenix.rs.entity.PhoenixDetails;
import de.phoenix.rs.entity.PhoenixLecture;
import de.phoenix.rs.key.KeyReader;
import de.phoenix.rs.key.SelectEntity;
import de.phoenix.rs.key.UpdateEntity;

@RunWith(OrderedRunner.class)
public class DetailTests {

    private static LocalTime nowTime;
    private static LocalDate nowDate;

    private static PhoenixLecture pLecture;

    @BeforeClass
    public static void beforeClass() {
        nowTime = new LocalTime(9, 0);
        nowDate = new LocalDate(1992, 3, 24);

        // Create sample detail
        PhoenixDetails pDetail1 = new PhoenixDetails("room1", Weekday.MONDAY, nowTime, nowTime.plusHours(1), Period.days(1), nowDate, nowDate.plusDays(1));

        // Create a lecture
        String title = "TestLecture";
        PhoenixLecture newLecture = new PhoenixLecture(title, Arrays.asList(pDetail1));
        ClientResponse response = PhoenixLecture.createResource(CLIENT, BASE_URL).type(MediaType.APPLICATION_JSON_TYPE).post(ClientResponse.class, newLecture);
        assertEquals(Status.OK, response.getClientResponseStatus());

        // Get lecture from the web service (just to be sure)
        response = PhoenixLecture.getResource(CLIENT, BASE_URL).type(MediaType.APPLICATION_JSON).post(ClientResponse.class, new SelectEntity<PhoenixLecture>().addKey("title", title));
        assertEquals(Status.OK, response.getClientResponseStatus());
        pLecture = EntityUtil.extractEntity(response);
    }

    @Test
    public void updateDetail() {
        // The lecture is now 2 hours long
        PhoenixDetails updatedDetail = new PhoenixDetails("room1", Weekday.MONDAY, nowTime, nowTime.plusHours(2), Period.days(1), nowDate, nowDate.plusWeeks(2));
        PhoenixDetails oldDetail = pLecture.getLectureDetails().get(0);

        // Update the detail
        UpdateEntity<PhoenixDetails> updateEntity = KeyReader.createUpdate(oldDetail, updatedDetail);
        ClientResponse response = PhoenixDetails.updateResource(CLIENT, BASE_URL).type(MediaType.APPLICATION_JSON_TYPE).post(ClientResponse.class, updateEntity);
        assertEquals(Status.OK, response.getClientResponseStatus());

        // Get the new lecture
        response = PhoenixLecture.getResource(CLIENT, BASE_URL).type(MediaType.APPLICATION_JSON).post(ClientResponse.class, new SelectEntity<PhoenixLecture>().addKey("title", "TestLecture"));
        assertEquals(Status.OK, response.getClientResponseStatus());
        pLecture = EntityUtil.extractEntity(response);
        PhoenixDetails newDetail = pLecture.getLectureDetails().get(0);

        // Compare the detail
        assertEquals(updatedDetail.getRoom(), newDetail.getRoom());
        assertEquals(updatedDetail.getWeekday(), newDetail.getWeekday());

        assertEquals(updatedDetail.getStartTime(), newDetail.getStartTime());
        assertEquals(updatedDetail.getEndTime(), newDetail.getEndTime());

        assertEquals(updatedDetail.getInterval(), newDetail.getInterval());

        assertEquals(updatedDetail.getStartDate(), newDetail.getStartDate());
        assertEquals(updatedDetail.getEndDate(), newDetail.getEndDate());
    }

}
