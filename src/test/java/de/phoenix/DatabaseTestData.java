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

package de.phoenix;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.MediaType;

import org.joda.time.DateTimeConstants;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.joda.time.Period;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import de.phoenix.rs.PhoenixClient;
import de.phoenix.rs.entity.PhoenixAttachment;
import de.phoenix.rs.entity.PhoenixDetails;
import de.phoenix.rs.entity.PhoenixLecture;
import de.phoenix.rs.entity.PhoenixLectureGroup;
import de.phoenix.rs.entity.PhoenixSubmission;
import de.phoenix.rs.entity.PhoenixTask;
import de.phoenix.rs.entity.PhoenixText;
import de.phoenix.rs.key.AddToEntity;
import de.phoenix.rs.key.KeyReader;

public class DatabaseTestData {

    private final static DatabaseTestData instance = new DatabaseTestData();

    private final static String BASE_URI = "http://localhost:7766/rest";

    private PhoenixTask specialNumbersTask = null;
    private PhoenixTask ternarySearchTask = null;

    private PhoenixLecture audLecture = null;
    private PhoenixLecture einfLecture = null;

    private DatabaseTestData() {
    }

    public static DatabaseTestData getInstance() {
        return instance;
    }

    public void createTestData() {
        System.out.println("Creating test data...");
        TestHttpServer httpServer = null;
        try {
            httpServer = new TestHttpServer(BASE_URI);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        Client client = PhoenixClient.create();

        try {
            createTasks(client);
            createLectures(client);

            System.out.println("Finished creating test data!");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Something went wrong!");
        }

        httpServer.stop();
    }

    private void createTasks(Client c) throws Exception {

        WebResource createTaskResource = PhoenixTask.createResource(c, BASE_URI);
        WebResource submitSolutionResource = PhoenixTask.submitResource(c, BASE_URI);

        createSpecialNumberTask(createTaskResource);
        solveSpecialNumbers(submitSolutionResource);

        createTernarySearchTask(createTaskResource);
        solveTernarySearch(submitSolutionResource);

    }

    // Create a task named SpecialNumbers
    private void createSpecialNumberTask(WebResource resource) throws Exception {

        String title = "Befreundete Zahlen";
        File descriptionFile = new File("src/test/resources/task/specialNumbers/TaskDescription.html");
        File attachmentFile = new File("src/test/resources/task/specialNumbers/FirstNumbers.pdf");
        File patternFile = new File("src/test/resources/task/specialNumbers/TaskPattern.java");

        List<PhoenixText> texts = new ArrayList<PhoenixText>();
        PhoenixText textFile = new PhoenixText(patternFile, patternFile.getName());
        texts.add(textFile);

        List<PhoenixAttachment> attachments = new ArrayList<PhoenixAttachment>();
        PhoenixAttachment binaryFile = new PhoenixAttachment(attachmentFile, attachmentFile.getName());
        attachments.add(binaryFile);

        String description = getText(descriptionFile);

        PhoenixTask task = new PhoenixTask(attachments, texts, description, title);
        ClientResponse post = resource.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, task);
        if (post.getStatus() != 200)
            throw new Exception("Status is not 200!");
        specialNumbersTask = task;
    }

    // Create a task named Ternary Search
    private void createTernarySearchTask(WebResource resource) throws Exception {

        String title = "Ternaere Suche";
        File descriptionFile = new File("src/test/resources/task/ternarySearch/TaskDescription.html");
        File patternFile = new File("src/test/resources/task/ternarySearch/TaskPattern.java");

        List<PhoenixText> texts = new ArrayList<PhoenixText>();
        PhoenixText textFile = new PhoenixText(patternFile, patternFile.getName());
        texts.add(textFile);

        List<PhoenixAttachment> attachments = new ArrayList<PhoenixAttachment>();

        String description = getText(descriptionFile);

        PhoenixTask task = new PhoenixTask(attachments, texts, description, title);
        ClientResponse post = resource.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, task);
        if (post.getStatus() != 200)
            throw new Exception("Status is not 200!");
        ternarySearchTask = task;
    }

    private void solveSpecialNumbers(WebResource resource) throws Exception {

        // Empty attachment list
        List<File> attachments = new ArrayList<File>();

        // Add single solution to the text file list
        List<File> textFiles = new ArrayList<File>();
        textFiles.add(new File("src/test/resources/task/specialNumbers/SpecialNumbers.java"));

        PhoenixSubmission sub = new PhoenixSubmission(attachments, textFiles);

        ClientResponse post = resource.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, KeyReader.createAddTo(this.specialNumbersTask, sub));
        if (post.getStatus() != 200)
            throw new Exception("Status is not 200!");
    }

    private void solveTernarySearch(WebResource resource) throws Exception {

        // Empty attachment list
        List<File> attachments = new ArrayList<File>();

        // Add single solution to the text file list
        List<File> textFiles = new ArrayList<File>();
        textFiles.add(new File("src/test/resources/task/ternarySearch/TernarySearch.java"));

        PhoenixSubmission sub = new PhoenixSubmission(attachments, textFiles);
        ClientResponse post = resource.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, KeyReader.createAddTo(this.ternarySearchTask, sub));
        if (post.getStatus() != 200)
            throw new Exception("Status is not 200!");
    }

    private String getText(File file) {
        StringBuilder sBuilder = new StringBuilder((int) file.length());
        try {
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
            byte[] buffer = new byte[2048];
            int read = 0;
            while ((read = bis.read(buffer)) != -1) {
                sBuilder.append(new String(buffer, 0, read, Charset.forName("UTF-8")));
            }

            bis.close();
            return sBuilder.toString();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void createLectures(Client c) throws Exception {

        WebResource createLectureResource = PhoenixLecture.createResource(c, BASE_URI);
        WebResource createLectureGroupResource = PhoenixLecture.addGroupResource(c, BASE_URI);

        // Create einf and its groups
        createEinfLecture(createLectureResource);
        createEinfGroups(createLectureGroupResource);

        // Create aud and its groups
        createAuDLecture(createLectureResource);
        createAuDGroups(createLectureGroupResource);
    }

    private void createEinfLecture(WebResource resource) throws Exception {

        List<PhoenixDetails> details = new ArrayList<PhoenixDetails>();

        // First date of Einf - The monday every week
        LocalTime startTime = new LocalTime(11, 15);
        LocalTime endTime = new LocalTime(12, 45);
        LocalDate startDate = new LocalDate(2013, 10, 14);
        LocalDate endDate = new LocalDate(2014, 1, 31);
        PhoenixDetails detail = new PhoenixDetails("G16-H5", DateTimeConstants.MONDAY, startTime, endTime, Period.weeks(1), startDate, endDate);
        details.add(detail);

        // Second date of Einf - The thursday every two weeks
        startTime = new LocalTime(11, 15);
        endTime = new LocalTime(12, 45);
        startDate = new LocalDate(2013, 10, 24);
        endDate = new LocalDate(2014, 1, 31);
        detail = new PhoenixDetails("G16-H5", DateTimeConstants.THURSDAY, startTime, endTime, Period.weeks(2), startDate, endDate);
        details.add(detail);

        PhoenixLecture lecture = new PhoenixLecture("Einf", details);

        ClientResponse response = resource.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, lecture);

        if (response.getStatus() != 200)
            throw new Exception("Status is not 200! " + response);
        this.einfLecture = lecture;
    }

    private void createEinfGroups(WebResource resource) throws Exception {

        List<PhoenixDetails> details = new ArrayList<PhoenixDetails>();

        // First group
        LocalTime startTime = new LocalTime(15, 00);
        LocalTime endTime = new LocalTime(16, 30);
        LocalDate startDate = new LocalDate(2013, 10, 21);
        LocalDate endDate = new LocalDate(2014, 01, 27);

        PhoenixDetails detail = new PhoenixDetails("G29-K058", DateTimeConstants.MONDAY, startTime, endTime, Period.weeks(1), startDate, endDate);
        details.add(detail);

        // Create a test group with
        // name = Gruppe 2
        // members = 22
        // default submission day is Monday
        // default submission time on Monday is 10 o'clock
        // In the room G29-k058 and other details described above
        // and the assigned lecture
        PhoenixLectureGroup group = new PhoenixLectureGroup("Gruppe 2", 22, DateTimeConstants.MONDAY, new LocalTime(10, 00), details, einfLecture);

        AddToEntity<PhoenixLecture, PhoenixLectureGroup> addGroupToLecture = KeyReader.createAddTo(einfLecture, group);
        ClientResponse response = resource.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, addGroupToLecture);
        if (response.getStatus() != 200)
            throw new Exception("Status is not 200! " + response);

        // Second group
        details = new ArrayList<PhoenixDetails>();

        startTime = new LocalTime(15, 15);
        endTime = new LocalTime(16, 45);
        startDate = new LocalDate(2013, 10, 23);
        endDate = new LocalDate(2014, 01, 29);

        detail = new PhoenixDetails("G29-K058", DateTimeConstants.WEDNESDAY, startTime, endTime, Period.weeks(1), startDate, endDate);
        details.add(detail);

        // Create a test group with
        // name = Gruppe 7
        // members = 22
        // default submission day is Monday
        // default submission time on Monday is 10 o'clock
        // In the room G29-k058 and other details described above
        // and the assigned lecture
        group = new PhoenixLectureGroup("Gruppe 7", 22, DateTimeConstants.MONDAY, new LocalTime(10, 00), details, einfLecture);

        addGroupToLecture = KeyReader.createAddTo(einfLecture, group);
        response = resource.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, addGroupToLecture);
        if (response.getStatus() != 200)
            throw new Exception("Status is not 200! " + response);
    }

    private void createAuDLecture(WebResource resource) throws Exception {
        List<PhoenixDetails> details = new ArrayList<PhoenixDetails>();

        // First date of AuD - The monday every week
        LocalTime startTime = new LocalTime(11, 15);
        LocalTime endTime = new LocalTime(12, 45);
        LocalDate startDate = new LocalDate(2013, 4, 8);
        LocalDate endDate = new LocalDate(2014, 7, 8);
        PhoenixDetails detail = new PhoenixDetails("G50-H3", DateTimeConstants.TUESDAY, startTime, endTime, Period.weeks(1), startDate, endDate);
        details.add(detail);

        // Second date of AuD - The thursday every two weeks
        startTime = new LocalTime(11, 15);
        endTime = new LocalTime(12, 45);
        startDate = new LocalDate(2014, 4, 10);
        endDate = new LocalDate(2014, 7, 8);
        detail = new PhoenixDetails("G50-H3", DateTimeConstants.THURSDAY, startTime, endTime, Period.weeks(2), startDate, endDate);
        details.add(detail);

        PhoenixLecture lecture = new PhoenixLecture("AuD", details);

        ClientResponse response = resource.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, lecture);

        if (response.getStatus() != 200)
            throw new Exception("Status is not 200! " + response);
        this.audLecture = lecture;
    }

    private void createAuDGroups(WebResource resource) throws Exception {

        List<PhoenixDetails> details = new ArrayList<PhoenixDetails>();

        // First group
        LocalTime startTime = new LocalTime(9, 15);
        LocalTime endTime = new LocalTime(10, 45);
        LocalDate startDate = new LocalDate(2014, 4, 5);
        LocalDate endDate = new LocalDate(2014, 7, 8);

        PhoenixDetails detail = new PhoenixDetails("G29-E037", DateTimeConstants.MONDAY, startTime, endTime, Period.weeks(1), startDate, endDate);
        details.add(detail);

        // Create a test group with
        // name = Gruppe 1
        // members = 22
        // default submission day is Monday
        // default submission time on Monday is 10 o'clock
        // In the room G29-E037 and other details described above
        // and the assigned lecture
        PhoenixLectureGroup group = new PhoenixLectureGroup("Gruppe 1", 23, DateTimeConstants.MONDAY, new LocalTime(10, 00), details, audLecture);

        AddToEntity<PhoenixLecture, PhoenixLectureGroup> addGroupToLecture = KeyReader.createAddTo(audLecture, group);
        ClientResponse response = resource.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, addGroupToLecture);
        if (response.getStatus() != 200)
            throw new Exception("Status is not 200! " + response);

        // Second group
        details = new ArrayList<PhoenixDetails>();

        startTime = new LocalTime(11, 15);
        endTime = new LocalTime(12, 45);
        startDate = new LocalDate(2014, 4, 15);
        endDate = new LocalDate(2014, 7, 9);

        detail = new PhoenixDetails("G29-K058", DateTimeConstants.WEDNESDAY, startTime, endTime, Period.weeks(1), startDate, endDate);
        details.add(detail);

        // Create a test group with
        // name = Gruppe 7
        // members = 22
        // default submission day is Monday
        // default submission time on Monday is 10 o'clock
        // In the room G29-k058 and other details described above
        // and the assigned lecture
        group = new PhoenixLectureGroup("Gruppe 5", 23, DateTimeConstants.MONDAY, new LocalTime(10, 00), details, audLecture);

        addGroupToLecture = KeyReader.createAddTo(audLecture, group);
        response = resource.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, addGroupToLecture);
        if (response.getStatus() != 200)
            throw new Exception("Status is not 200! " + response);
    }
}
