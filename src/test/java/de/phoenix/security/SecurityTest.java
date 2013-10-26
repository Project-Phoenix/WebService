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

package de.phoenix.security;

//import static org.junit.Assert.assertTrue;
//
//import javax.ws.rs.core.MediaType;
//
//import org.hibernate.Query;
//import org.hibernate.Session;
//import org.hibernate.Transaction;
//import org.junit.AfterClass;
//import org.junit.BeforeClass;
//import org.junit.Test;
//
//import com.sun.jersey.api.client.Client;
//import com.sun.jersey.api.client.ClientResponse;
//import com.sun.jersey.api.client.ClientResponse.Status;
//import com.sun.jersey.api.client.WebResource;
//import com.sun.jersey.api.container.httpserver.HttpServerFactory;
//import com.sun.net.httpserver.HttpServer;
//
//import de.phoenix.database.DatabaseManager;
//import de.phoenix.database.entity.User;

public class SecurityTest {

//    private final static String BASE_URL = "http://localhost:7766/rest";
//
//    private static HttpServer httpServer;
//
//    @BeforeClass
//    public static void setUpBeforeClass() throws Exception {
//
//        Session session = DatabaseManager.getInstance().openSession();
//        Query query = session.getNamedQuery("User.findByUsername").setString("username", "Phoenix");
//        
//        User u = (User) query.uniqueResult();
//        if (u != null) {
//            Transaction trans = session.beginTransaction();
//            session.delete(u);
//            trans.commit();
//        }
//        
//        session.close();
//
//        httpServer = HttpServerFactory.create(BASE_URL);
//        httpServer.start();
//    }
//
//    @AfterClass
//    public static void tearDownAfterClass() throws Exception {
//        httpServer.stop(0);
//    }
//
//    @Test
//    public void registerAccount() {
//
//        String password = "TestPassword";
//
//        // Create user container
//        User user = new User(password);
//        // Set values
//        user.setUsername("Phoenix");
//        user.setName("Gaertner");
//        user.setSurname("Kilian");
//        user.setEmail("Test@phoenix.de");
//        user.setTitle("Herr");
//
//        // Create client to connect to jersey webservice
//        Client client = Client.create();
//        // Get resource for registering account
//        WebResource wr = client.resource(BASE_URL).path("account").path("register");
//
//        // Call webresource and upload user information
//        ClientResponse response = wr.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, user);
//        // True when the response was OK ( ResponseCode OK: 200)
//        assertTrue(response.toString(), response.getStatus() == 200);
//
//        // Request a token - also check if the account is valid
//        WebResource requestTokenRes = client.resource(BASE_URL).path("token").path("request");
//        requestTokenRes.addFilter(new LoginFilter(user.getUsername(), password));
//        response = requestTokenRes.get(ClientResponse.class);
//
//        assertTrue(response.toString(), response.getClientResponseStatus().equals(Status.OK));
//        Token token = response.getEntity(Token.class);
//
//        // Check if token is valid
//        WebResource validateTokenRes = client.resource(BASE_URL).path("token").path("validate");
//        client.addFilter(new TokenFilter(token));
//
//        response = validateTokenRes.get(ClientResponse.class);
//
//        assertTrue(response.toString(), response.getClientResponseStatus().equals(Status.OK));
//    }
}
