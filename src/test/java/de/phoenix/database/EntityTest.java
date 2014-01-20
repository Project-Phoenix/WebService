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

import java.io.File;

import org.junit.ClassRule;
import org.junit.rules.ExternalResource;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import de.phoenix.DatabaseCleaner;
import de.phoenix.DatabaseTestData;
import de.phoenix.TestHttpServer;
import de.phoenix.lecture.LectureTest;
import de.phoenix.task.TaskTest;
import de.phoenix.tasksheet.TaskSheetTest;

@RunWith(Suite.class)
@SuiteClasses({LectureTest.class, TaskTest.class, TaskSheetTest.class})
public class EntityTest {

    public final static String BASE_URL = "http://localhost:7766/rest";

    @ClassRule
    public static ExternalResource CLEANER_RESOURCE = new ExternalResource() {

        private TestHttpServer httpServer;

        @Override
        protected void before() throws Throwable {
            System.out.println("Start HTTP Server");
            httpServer = new TestHttpServer(BASE_URL);
            System.out.println("Whipe database");
            DatabaseCleaner.getInstance().run();
        };

        @Override
        protected void after() {
            System.out.println("Whipe database");
            DatabaseCleaner.getInstance().run();
            System.out.println("Create test data");
            DatabaseTestData.getInstance().createTestData();
            System.out.println("Delete all generated class files");
            deleteAllClassFiles();
            System.out.println("Stop HTTP Server");
            httpServer.stop();
        };

        private void deleteAllClassFiles() {
            File dir = new File(".");
            File[] files = dir.listFiles();
            if (files == null)
                return;
            for (int i = 0; i < files.length; ++i) {
                File file = files[i];
                if (file.getName().endsWith(".class"))
                    file.delete();
            }
        }
    };
}
