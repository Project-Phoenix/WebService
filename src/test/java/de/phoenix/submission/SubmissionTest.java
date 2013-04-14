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

package de.phoenix.submission;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import javax.ws.rs.core.MediaType;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.container.httpserver.HttpServerFactory;
import com.sun.jersey.multipart.FormDataMultiPart;
import com.sun.jersey.multipart.file.FileDataBodyPart;
import com.sun.net.httpserver.HttpServer;

public class SubmissionTest {

    private final static String BASE_URL = "http://localhost:8080/rest";

    private static HttpServer httpServer;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        // Start Http Server
        httpServer = HttpServerFactory.create(BASE_URL);
        httpServer.start();

        // Create temponary file
        File f = new File("test.txt");
        try {
            BufferedWriter bWriter = new BufferedWriter(new FileWriter(f));
            bWriter.write("Erste Zeile");
            bWriter.newLine();
            bWriter.write("Zweite Zeile");
            bWriter.newLine();
            bWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        httpServer.stop(0);
        // Delete temponary file
        File f = new File("test.txt");
        f.delete();
    }

    @Test
    public void uploadTest() {
        File fileToUpload = new File("test.txt");
        String author = "Meldanor";

        // Create the websresource
        Client client = Client.create();
        WebResource wr = client.resource(BASE_URL).path("/submission").path("/upload").path(author);

        // Create file packet
        FormDataMultiPart multiPart = new FormDataMultiPart();
        if (fileToUpload != null) {
            multiPart.bodyPart(new FileDataBodyPart("file", fileToUpload, MediaType.APPLICATION_OCTET_STREAM_TYPE));
        }

        // Send file to server
        ClientResponse clientResp = wr.type(MediaType.MULTIPART_FORM_DATA_TYPE).put(ClientResponse.class, multiPart);
        System.out.println("Response: " + clientResp.getClientResponseStatus());
    }

}
