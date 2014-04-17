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

package de.phoenix.debug;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import javax.ws.rs.core.MediaType;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import de.phoenix.rs.EntityUtil;
import de.phoenix.rs.PhoenixClient;
import de.phoenix.rs.entity.PhoenixAttachment;
import de.phoenix.rs.entity.PhoenixSubmission;
import de.phoenix.rs.entity.PhoenixSubmissionResult;
import de.phoenix.rs.entity.PhoenixTask;
import de.phoenix.rs.entity.PhoenixText;
import de.phoenix.rs.key.AddToEntity;
import de.phoenix.rs.key.SelectEntity;

public class Submitter {

    private static Client c = PhoenixClient.create();
    private static String URL = "http://meldanor.dyndns.org:8080/PhoenixWebService/rest";

    public static void main(String[] args) throws IOException {
        WebResource resource = PhoenixTask.getAllTitlesResource(c, URL);
        ClientResponse response = resource.get(ClientResponse.class);
        if (response.getStatus() != 200) {
            System.err.println("Error: " + response);
            System.exit(1);
        }
        List<String> titles = EntityUtil.extractEntityList(response);
        int i = 1;
        for (String title : titles) {
            System.out.println("(" + i++ + ") - " + title);
        }

        Scanner scanner = new Scanner(System.in);

        System.out.print("TaskIndex: ");
        int taskIndex = scanner.nextInt() - 1;
        scanner.nextLine();
        String title = titles.get(taskIndex);
        System.out.println("Task is " + title);

        List<PhoenixAttachment> attachments = new ArrayList<PhoenixAttachment>();
        System.out.println("Attachments(empty for nothing, seperated by ';' )");
        String line = scanner.nextLine();
        if (line != null && !line.trim().isEmpty()) {
            String[] split = line.split(";");
            for (int j = 0; j < split.length; j++) {
                File f = new File(split[j]);
                attachments.add(new PhoenixAttachment(f, f.getName()));
            }
        }

        List<PhoenixText> texts = new ArrayList<PhoenixText>();
        System.out.println("Source Files(empty for nothing, seperated by ';' )");
        line = scanner.nextLine();
        if (line != null && !line.trim().isEmpty()) {
            String[] split = line.split(";");
            for (int j = 0; j < split.length; j++) {
                File f = new File(split[j]);
                texts.add(new PhoenixText(f, f.getName()));
            }
        }

        PhoenixSubmission sub = new PhoenixSubmission(attachments, texts);
        SelectEntity<PhoenixTask> addToEntity = new AddToEntity<PhoenixTask, PhoenixSubmission>(Arrays.asList(sub)).addKey("title", title);
        response = PhoenixTask.submitResource(c, URL).type(MediaType.APPLICATION_JSON).post(ClientResponse.class, addToEntity);
        if (response.getStatus() != 200) {
            System.err.println("Error: " + response);
            System.exit(1);
        }

        System.out.println("Submitted");
        System.out.println("Result: " + response.getEntity(PhoenixSubmissionResult.class));
        scanner.close();

    }
}
