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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import javax.ws.rs.core.MediaType;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.ClientResponse.Status;
import com.sun.jersey.api.client.WebResource;

import de.phoenix.database.DatabaseManager;
import de.phoenix.junit.OrderedRunner;
import de.phoenix.junit.OrderedRunner.Order;
import de.phoenix.rs.EntityUtil;
import de.phoenix.rs.PhoenixStatusType;
import de.phoenix.rs.key.SelectEntity;
import de.phoenix.security.TokenManager;
import de.phoenix.security.login.LoginAttempt;
import de.phoenix.security.login.PhoenixToken;
import de.phoenix.security.login.PlainLoginAttempt;
import de.phoenix.security.user.CreatePhoenixUser;
import de.phoenix.security.user.PermissionTree;
import de.phoenix.security.user.PhoenixUser;

@RunWith(OrderedRunner.class)
public class UserTests {

    @BeforeClass
    public static void createDefaultUser() {
        Session session = DatabaseManager.getSession();
        try {
            Transaction trans = session.beginTransaction();
            System.out.println("Add default user levels...");
            if (!existsUserLevel("admin", session)) {

                Userlevel adminLevel = new Userlevel();
                adminLevel.setName("admin");
                PermissionTree tree = new PermissionTree();
                tree.addNode("*");
                adminLevel.setPermissionList(tree);

                session.save(adminLevel);
            }
            if (!existsUserLevel("default", session)) {

                Userlevel defaultLevel = new Userlevel();
                defaultLevel.setName("default");
                PermissionTree tree = new PermissionTree();
                defaultLevel.setPermissionList(tree);
                session.save(defaultLevel);
            }

            trans.commit();
        } finally {
            if (session != null)
                session.close();
        }
    }

    private static boolean existsUserLevel(String name, Session session) {
        return session.createCriteria(Userlevel.class).add(Restrictions.eq("name", name)).uniqueResult() != null;
    }

    @Test
    @Order(1)
    public void createUser() {
        PhoenixUser pUser = new PhoenixUser("Kilian", "Gaertner", "Meldanor", "Test@phoenix.de");
        CreatePhoenixUser create = new CreatePhoenixUser("testpassword", pUser);

        WebResource createResource = PhoenixUser.createResource(CLIENT, BASE_URL);
        ClientResponse response = createResource.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, create);
        assertEquals(Status.OK.getStatusCode(), response.getStatus());

        WebResource getResource = PhoenixUser.getResource(CLIENT, BASE_URL);
        SelectEntity<PhoenixUser> selector = new SelectEntity<PhoenixUser>().addKey("username", "Meldanor");
        response = getResource.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, selector);
        assertEquals(Status.OK.getStatusCode(), response.getStatus());

        PhoenixUser pUserFromDatabase = EntityUtil.extractEntity(response);
        assertEquals("Meldanor", pUserFromDatabase.getUsername());
    }

    @Test
    @Order(2)
    public void loginCorrectUser() {

        LoginAttempt loginAttempt = new PlainLoginAttempt("Meldanor", "testpassword");

        WebResource loginResource = PhoenixUser.loginResource(CLIENT, BASE_URL);
        ClientResponse response = loginResource.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, loginAttempt);
        assertEquals(Status.OK.getStatusCode(), response.getStatus());

        PhoenixToken token = response.getEntity(PhoenixToken.class);
        assertNotNull(token);

        assertTrue(TokenManager.INSTANCE.isValid(token));
    }

    @Test
    @Order(3)
    public void loginIncorrectCredentials() {

        LoginAttempt attempt = new PlainLoginAttempt("NichtMeldanor", "testpassword");

        WebResource loginResource = PhoenixUser.loginResource(CLIENT, BASE_URL);
        ClientResponse response = loginResource.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, attempt);
        assertEquals(Status.UNAUTHORIZED.getStatusCode(), response.getStatus());

        attempt = new PlainLoginAttempt("Meldanor", "wrongpassword");
        response = loginResource.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, attempt);
        assertEquals(Status.UNAUTHORIZED.getStatusCode(), response.getStatus());

        attempt = new PlainLoginAttempt("NichtMeldanor", "wrongpassword");
        response = loginResource.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, attempt);
        assertEquals(Status.UNAUTHORIZED.getStatusCode(), response.getStatus());
    }

    @Test
    @Order(4)
    public void loginTwice() {

        // First login
        LoginAttempt attempt = new PlainLoginAttempt("Meldanor", "testpassword");

        WebResource loginResource = PhoenixUser.loginResource(CLIENT, BASE_URL);
        ClientResponse response = loginResource.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, attempt);
        assertEquals(Status.OK.getStatusCode(), response.getStatus());

        PhoenixToken token = response.getEntity(PhoenixToken.class);
        assertNotNull(token);
        assertTrue(TokenManager.INSTANCE.isValid(token));

        // Second login - old token should be deleted on server side
        PhoenixToken firstToken = token;
        response = loginResource.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, attempt);
        assertEquals(Status.OK.getStatusCode(), response.getStatus());

        token = response.getEntity(PhoenixToken.class);
        assertTrue(TokenManager.INSTANCE.isValid(token));
        assertFalse(TokenManager.INSTANCE.isValid(firstToken));

    }

    @Test
    @Order(5)
    public void createDuplicateAccount() {
        // Duplicate Username = Meldanor
        PhoenixUser pUser = new PhoenixUser("TestSurname", "TestName", "Meldanor", "OtherEmail@phoenix.de");
        CreatePhoenixUser create = new CreatePhoenixUser("testpassword", pUser);

        WebResource createResource = PhoenixUser.createResource(CLIENT, BASE_URL);
        ClientResponse response = createResource.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, create);
        assertEquals(PhoenixStatusType.DUPLIATE_ENTITY.getStatusCode(), response.getStatus());
        assertEquals("Username:Meldanor", response.getEntity(String.class));

        // Check if the user was not created
        SelectEntity<PhoenixUser> userSelector = new SelectEntity<PhoenixUser>().addKey("surname", "TestSurname").addKey("name", "TestName");
        WebResource getResource = PhoenixUser.getResource(CLIENT, BASE_URL);
        response = getResource.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, userSelector);
        assertEquals(PhoenixStatusType.NO_ENTITIES.getStatusCode(), response.getStatus());
    }

    @Test
    @Order(6)
    public void createDuplicateEMail() {
        // Duplicate E-Mail: Test@phoenix.de
        PhoenixUser pUser = new PhoenixUser("TestSurname", "TestName", "TestAccount", "Test@phoenix.de");
        CreatePhoenixUser create = new CreatePhoenixUser("testpassword", pUser);

        WebResource createResource = PhoenixUser.createResource(CLIENT, BASE_URL);
        ClientResponse response = createResource.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, create);
        assertEquals(PhoenixStatusType.DUPLIATE_ENTITY.getStatusCode(), response.getStatus());
        assertEquals("Mail:Test@phoenix.de", response.getEntity(String.class));

        // Check if the user was not created
        SelectEntity<PhoenixUser> userSelector = new SelectEntity<PhoenixUser>().addKey("surname", "TestSurname").addKey("name", "TestName");
        WebResource getResource = PhoenixUser.getResource(CLIENT, BASE_URL);
        response = getResource.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, userSelector);
        assertEquals(PhoenixStatusType.NO_ENTITIES.getStatusCode(), response.getStatus());
    }
}
