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

import java.util.Arrays;
import java.util.List;

import javax.ws.rs.core.MediaType;

import org.joda.time.DateTime;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.ClientResponse.Status;
import com.sun.jersey.api.client.WebResource;

import de.phoenix.junit.OrderedRunner;
import de.phoenix.junit.OrderedRunner.Order;
import de.phoenix.rs.EntityUtil;
import de.phoenix.rs.PhoenixStatusType;
import de.phoenix.rs.entity.PhoenixLectureGroup;
import de.phoenix.rs.entity.PhoenixLectureGroupTaskSheet;
import de.phoenix.rs.entity.PhoenixTask;
import de.phoenix.rs.entity.PhoenixTaskSheet;
import de.phoenix.rs.entity.PhoenixTaskSubmissionDates;
import de.phoenix.rs.entity.connection.LectureGroupTaskSheetConnection;
import de.phoenix.rs.entity.connection.TaskSheetConnection;
import de.phoenix.rs.entity.connection.TaskSubmissionDatesConnection;
import de.phoenix.rs.entity.disconnection.DisconnectTaskTaskSheet;
import de.phoenix.rs.key.ConnectionEntity;
import de.phoenix.rs.key.KeyReader;
import de.phoenix.rs.key.SelectAllEntity;
import de.phoenix.rs.key.SelectEntity;

@RunWith(OrderedRunner.class)
public class TaskSheetTests {

    private static final String TASK_SHEET_TITLE = "Testblatt";

    private static List<PhoenixLectureGroup> groupList;

    @BeforeClass
    public static void beforeClass() {
        WebResource getAllGroupsResource = PhoenixLectureGroup.getResource(CLIENT, BASE_URL);
        ClientResponse response = getAllGroupsResource.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, new SelectAllEntity<PhoenixLectureGroup>());
        groupList = EntityUtil.extractEntityList(response);
    }

    @Test
    @Order(1)
    public void createTaskSheet() {

        // Get some tasks to assign to database
        WebResource getAllTasksResource = PhoenixTask.getResource(CLIENT, BASE_URL);
        ClientResponse response = getAllTasksResource.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, new SelectAllEntity<PhoenixTask>());
        List<PhoenixTask> tasks = EntityUtil.extractEntityList(response);

        WebResource connectTasksheetWithTasksResource = PhoenixTaskSheet.connectTaskSheetWithTaskResource(CLIENT, BASE_URL);

        // Create tasksheet with help of ConnectionEntity
        ConnectionEntity bundleTasksToTaskSheet = new TaskSheetConnection(TASK_SHEET_TITLE, tasks);

        // Send construct to server and create so a new task sheet
        response = connectTasksheetWithTasksResource.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, bundleTasksToTaskSheet);
        assertEquals(Status.OK, response.getClientResponseStatus());
    }

    private static String removedTaskTitle;

    @Test
    @Order(2)
    public void removeOneTask() {

        // Request all task sheet - is only one
        WebResource getAllTaskSheetsRes = PhoenixTaskSheet.getResource(CLIENT, BASE_URL);
        ClientResponse response = getAllTaskSheetsRes.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, new SelectAllEntity<PhoenixTaskSheet>());

        // Check that the created task sheet has two tasks
        PhoenixTaskSheet taskSheet = EntityUtil.extractEntity(response);
        List<PhoenixTask> tasks = taskSheet.getTasks();
        assertEquals(2, tasks.size());

        removedTaskTitle = tasks.get(0).getTitle();
        // extract one task from the task sheet to remove it and create a task
        // selector from it
        SelectEntity<PhoenixTask> taskSelector = new SelectEntity<PhoenixTask>().addKey("title", removedTaskTitle);

        // Create task sheet selector
        SelectEntity<PhoenixTaskSheet> taskSheetSelector = new SelectEntity<PhoenixTaskSheet>().addKey("title", taskSheet.getTitle());

        // Remove the task from the task sheet
        DisconnectTaskTaskSheet deleteEntity = new DisconnectTaskTaskSheet(taskSelector, taskSheetSelector);
        WebResource removeTaskFromTaskSheetResource = PhoenixTaskSheet.removeTaskFromTaskSheetResource(CLIENT, BASE_URL);
        response = removeTaskFromTaskSheetResource.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, deleteEntity);

        assertEquals(200, response.getStatus());

        // Check if the task was removed from tasksheet
        // Request all task sheet - is only one
        response = getAllTaskSheetsRes.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, new SelectAllEntity<PhoenixTaskSheet>());

        // Check that the created task sheet has two tasks
        taskSheet = EntityUtil.extractEntity(response);
        assertEquals(1, taskSheet.getTasks().size());
    }

    @Test
    @Order(3)
    public void addSingleTaskToTaskSheet() {
        // Request all task sheet - is only one
        WebResource getAllTaskSheetsRes = PhoenixTaskSheet.getResource(CLIENT, BASE_URL);
        ClientResponse response = getAllTaskSheetsRes.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, new SelectAllEntity<PhoenixTaskSheet>());

        // Check that the created task sheet has two tasks
        PhoenixTaskSheet taskSheet = EntityUtil.extractEntity(response);
        List<PhoenixTask> tasks = taskSheet.getTasks();
        assertEquals(1, tasks.size());

        SelectEntity<PhoenixTask> taskSelector = new SelectEntity<PhoenixTask>().addKey("title", removedTaskTitle);
        // Create connector to connect the task and the tasksheet (but not
        // create the task sheet!)
        @SuppressWarnings("unchecked")
        TaskSheetConnection connection = new TaskSheetConnection(Arrays.asList(taskSelector), TASK_SHEET_TITLE);

        WebResource addTaskToTaskSheetRes = PhoenixTaskSheet.addTaskToTaskSheet(CLIENT, BASE_URL);
        response = addTaskToTaskSheetRes.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, connection);
        assertEquals(200, response.getStatus());

        response = getAllTaskSheetsRes.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, new SelectAllEntity<PhoenixTaskSheet>());

        // Check that the created task sheet has now two tasks
        taskSheet = EntityUtil.extractEntity(response);
        tasks = taskSheet.getTasks();
        assertEquals(2, tasks.size());
    }

    @Test
    @Order(4)
    public void createDuplicateTaskSheet() {
        // Do the same as createTaskSheet

        WebResource getAllTasksResource = PhoenixTask.getResource(CLIENT, BASE_URL);
        ClientResponse response = getAllTasksResource.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, new SelectAllEntity<PhoenixTask>());
        List<PhoenixTask> tasks = EntityUtil.extractEntityList(response);

        WebResource connectTasksheetWithTasksResource = PhoenixTaskSheet.connectTaskSheetWithTaskResource(CLIENT, BASE_URL);

        ConnectionEntity bundleTasksToTaskSheet = new TaskSheetConnection(TASK_SHEET_TITLE, tasks);

        response = connectTasksheetWithTasksResource.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, bundleTasksToTaskSheet);
        // Check if duplicate was detected
        assertEquals(PhoenixStatusType.DUPLIATE_ENTITY.getStatusCode(), response.getStatus());
    }

    @Test
    @Order(5)
    public void noCurrentTaskSheet() {

        WebResource getCurrentTaskSheet = PhoenixLectureGroupTaskSheet.currentTaskSheet(CLIENT, BASE_URL);

        SelectEntity<PhoenixLectureGroup> groupSelector = KeyReader.createSelect(groupList.get(0));

        ClientResponse response = getCurrentTaskSheet.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, groupSelector);
        assertEquals(PhoenixStatusType.NO_CURRENT_TASKSHEET.getStatusCode(), response.getStatus());
    }

    @Test
    @Order(6)
    public void createLectureGroupTaskSheets() {

        WebResource getTaskSheetResource = PhoenixTaskSheet.getResource(CLIENT, BASE_URL);
        ClientResponse response = getTaskSheetResource.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, new SelectEntity<PhoenixTaskSheet>().addKey("title", TASK_SHEET_TITLE));
        PhoenixTaskSheet taskSheet = EntityUtil.extractEntity(response);

        ConnectionEntity connectionEntity = new LectureGroupTaskSheetConnection(DateTime.now().plusWeeks(1), DateTime.now(), taskSheet, groupList);

        WebResource createLectureGroupTaskSheet = PhoenixLectureGroupTaskSheet.createResource(CLIENT, BASE_URL);
        response = createLectureGroupTaskSheet.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, connectionEntity);
        assertEquals(Status.OK, response.getClientResponseStatus());
    }

    @Test
    @Order(7)
    public void currentTaskSheet() {
        WebResource getCurrentTaskSheet = PhoenixLectureGroupTaskSheet.currentTaskSheet(CLIENT, BASE_URL);

        SelectEntity<PhoenixLectureGroup> groupSelector = KeyReader.createSelect(groupList.get(0));

        ClientResponse response = getCurrentTaskSheet.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, groupSelector);
        assertEquals(Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    @Ignore("Must implement this!")
    @Order(8)
    public void createDuplicateLectureGroupTaskSheets() {

        WebResource getTaskSheetResource = PhoenixTaskSheet.getResource(CLIENT, BASE_URL);
        ClientResponse response = getTaskSheetResource.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, new SelectEntity<PhoenixTaskSheet>().addKey("title", TASK_SHEET_TITLE));
        PhoenixTaskSheet taskSheet = EntityUtil.extractEntity(response);

        ConnectionEntity connectionEntity = new LectureGroupTaskSheetConnection(DateTime.now(), DateTime.now().plusWeeks(1), taskSheet, groupList);

        WebResource createLectureGroupTaskSheet = PhoenixLectureGroupTaskSheet.createResource(CLIENT, BASE_URL);
        response = createLectureGroupTaskSheet.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, connectionEntity);
        // Check if duplicate was detected
        assertEquals(PhoenixStatusType.DUPLIATE_ENTITY.getStatusCode(), response.getStatus());
    }

    @Test
    @Order(9)
    public void setSubmissionDateForTask() {

        PhoenixLectureGroup group = groupList.get(0);

        WebResource getTaskSheetResource = PhoenixTaskSheet.getResource(CLIENT, BASE_URL);
        ClientResponse response = getTaskSheetResource.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, new SelectEntity<PhoenixTaskSheet>().addKey("title", TASK_SHEET_TITLE));
        PhoenixTaskSheet taskSheet = EntityUtil.extractEntity(response);

        SelectEntity<PhoenixLectureGroupTaskSheet> selectEntity = new SelectEntity<PhoenixLectureGroupTaskSheet>();
        selectEntity.addKey("lectureGroup", group);
        selectEntity.addKey("taskSheet", taskSheet);

        WebResource getLectureGroupTaskSheetResource = PhoenixLectureGroupTaskSheet.getResource(CLIENT, BASE_URL);
        response = getLectureGroupTaskSheetResource.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, selectEntity);

        PhoenixLectureGroupTaskSheet groupTaskSheet = EntityUtil.extractEntity(response);

        PhoenixTask task = groupTaskSheet.getTasks().get(0).getTask();
        ConnectionEntity connectionEntity = new TaskSubmissionDatesConnection(DateTime.now().plusDays(3), DateTime.now(), groupTaskSheet, task);

        WebResource createTaskSubmissionDateResource = PhoenixTaskSubmissionDates.createResource(CLIENT, BASE_URL);
        response = createTaskSubmissionDateResource.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, connectionEntity);
        assertEquals(Status.OK, response.getClientResponseStatus());
    }
}
