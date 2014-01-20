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

package de.phoenix.tasksheet;

import static de.phoenix.database.EntityTest.BASE_URL;
import static org.junit.Assert.assertEquals;

import java.util.List;

import javax.ws.rs.core.MediaType;

import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.ClientResponse.Status;
import com.sun.jersey.api.client.WebResource;

import de.phoenix.junit.OrderedRunner;
import de.phoenix.junit.OrderedRunner.Order;
import de.phoenix.rs.EntityUtil;
import de.phoenix.rs.PhoenixClient;
import de.phoenix.rs.entity.PhoenixLectureGroup;
import de.phoenix.rs.entity.PhoenixLectureGroupTaskSheet;
import de.phoenix.rs.entity.PhoenixTask;
import de.phoenix.rs.entity.PhoenixTaskSheet;
import de.phoenix.rs.entity.PhoenixTaskSubmissionDates;
import de.phoenix.rs.entity.connection.LectureGroupTaskSheetConnection;
import de.phoenix.rs.entity.connection.TaskSheetConnection;
import de.phoenix.rs.entity.connection.TaskSubmissionDatesConnection;
import de.phoenix.rs.key.ConnectionEntity;
import de.phoenix.rs.key.SelectAllEntity;
import de.phoenix.rs.key.SelectEntity;

@RunWith(OrderedRunner.class)
public class TaskSheetTest {

    private static final String TASK_SHEET_TITLE = "Testblatt";

    @Test
    @Order(1)
    public void createTaskSheet() {
        // Create client
        Client c = PhoenixClient.create();

        // Get some tasks to assign to database
        WebResource getAllTasksResource = PhoenixTask.getResource(c, BASE_URL);
        ClientResponse response = getAllTasksResource.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, new SelectAllEntity<PhoenixTask>());
        List<PhoenixTask> tasks = EntityUtil.extractEntityList(response);

        WebResource connectTasksheetWithTasksResource = PhoenixTaskSheet.connectTaskSheetWithTaskResource(c, BASE_URL);

        // Create tasksheet with help of ConnectionEntity
        ConnectionEntity bundleTasksToTaskSheet = new TaskSheetConnection(TASK_SHEET_TITLE, tasks);

        // Send construct to server and create so a new task sheet
        response = connectTasksheetWithTasksResource.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, bundleTasksToTaskSheet);
        assertEquals(response.getStatus(), 200);
    }

    @Test
    @Order(2)
    public void createLectureGroupTaskSheets() {
        Client c = PhoenixClient.create();

        WebResource getAllGroupsResource = PhoenixLectureGroup.getResource(c, BASE_URL);
        ClientResponse response = getAllGroupsResource.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, new SelectAllEntity<PhoenixLectureGroup>());
        List<PhoenixLectureGroup> groups = EntityUtil.extractEntityList(response);

        WebResource getTaskSheetResource = PhoenixTaskSheet.getResource(c, BASE_URL);
        response = getTaskSheetResource.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, new SelectEntity<PhoenixTaskSheet>().addKey("title", TASK_SHEET_TITLE));
        PhoenixTaskSheet taskSheet = EntityUtil.extractEntity(response);

        ConnectionEntity connectionEntity = new LectureGroupTaskSheetConnection(DateTime.now(), DateTime.now().plusWeeks(1), taskSheet, groups);

        WebResource createLectureGroupTaskSheet = PhoenixLectureGroupTaskSheet.createResource(c, BASE_URL);
        response = createLectureGroupTaskSheet.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, connectionEntity);
        assertEquals(Status.OK, response.getClientResponseStatus());
    }

    @Test
    @Order(3)
    public void setSubmissionDateForTask() {
        Client c = PhoenixClient.create();

        WebResource getAllGroupsResource = PhoenixLectureGroup.getResource(c, BASE_URL);
        ClientResponse response = getAllGroupsResource.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, new SelectAllEntity<PhoenixLectureGroup>());

        PhoenixLectureGroup group = EntityUtil.extractEntity(response);

        WebResource getTaskSheetResource = PhoenixTaskSheet.getResource(c, BASE_URL);
        response = getTaskSheetResource.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, new SelectEntity<PhoenixTaskSheet>().addKey("title", TASK_SHEET_TITLE));
        PhoenixTaskSheet taskSheet = EntityUtil.extractEntity(response);

        SelectEntity<PhoenixLectureGroupTaskSheet> selectEntity = new SelectEntity<PhoenixLectureGroupTaskSheet>();
        selectEntity.addKey("lectureGroup", group);
        selectEntity.addKey("taskSheet", taskSheet);

        WebResource getLectureGroupTaskSheetResource = PhoenixLectureGroupTaskSheet.getResource(c, BASE_URL);
        response = getLectureGroupTaskSheetResource.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, selectEntity);

        PhoenixLectureGroupTaskSheet groupTaskSheet = EntityUtil.extractEntity(response);

        PhoenixTask task = groupTaskSheet.getTaskSheet().getTasks().get(0);
        ConnectionEntity connectionEntity = new TaskSubmissionDatesConnection(DateTime.now().plusDays(3), DateTime.now(), groupTaskSheet, task);

        WebResource createTaskSubmissionDateResource = PhoenixTaskSubmissionDates.createResource(c, BASE_URL);
        response = createTaskSubmissionDateResource.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, connectionEntity);
        assertEquals(Status.OK, response.getClientResponseStatus());
    }

}
