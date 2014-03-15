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

package de.phoenix.database;

import org.junit.ClassRule;
import org.junit.rules.ExternalResource;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.sun.jersey.api.client.Client;

import de.phoenix.TestHttpServer;
import de.phoenix.database.entity.DetailTests;
import de.phoenix.database.entity.LectureGroupTests;
import de.phoenix.database.entity.LectureTests;
import de.phoenix.database.entity.TaskSheetTests;
import de.phoenix.database.entity.TaskTests;
import de.phoenix.rs.PhoenixClient;
import de.phoenix.util.TextFileReader;

@RunWith(Suite.class)
@SuiteClasses({ConnectionTests.class, LectureTests.class, TaskTests.class, TaskSheetTests.class, DetailTests.class, LectureGroupTests.class})
public class EntityTest {

    public final static String BASE_URL = "http://localhost:7766/rest";
    public static Client CLIENT;
    public static TextFileReader READER;

    @ClassRule
    public static ExternalResource CLEANER_RESOURCE = new ExternalResource() {

        private TestHttpServer httpServer;

        @Override
        protected void before() throws Throwable {
            System.out.println("Start HTTP Server");
            httpServer = new TestHttpServer(BASE_URL);
            System.out.println("Whipe database");
            DatabaseCleaner.getInstance().run();

            CLIENT = PhoenixClient.create();
            READER = new TextFileReader();
        };

        @Override
        protected void after() {
            System.out.println("Whipe database");
            DatabaseCleaner.getInstance().run();
            System.out.println("Create test data");
            DatabaseTestData.getInstance().createTestData();
            System.out.println("Stop HTTP Server");
            httpServer.stop();
        };
    };
}
