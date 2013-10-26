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

package de.phoenix.security.permission;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

/**
 * A normal role with a permission tree. This role has no restricted permission
 * and have only the permission that has been granted
 * 
 * @author Meldanor
 * 
 */
public class BasicRoleTest {

    private List<String> readAllLines(File f) throws Exception {
        List<String> lines = new ArrayList<String>();
        BufferedReader bReader = new BufferedReader(new FileReader(f));
        String line = "";
        while ((line = bReader.readLine()) != null)
            lines.add(line);
        bReader.close();
        return lines;
    }

    @Test
    public void createBigRole() {
        try {
            File f = new File("src/test/resources/permissions.txt");
            List<String> lines = readAllLines(f);

            Role adminRole = new Role("Admin");
            for (String line : lines) {
                adminRole.grantPermission(line);
            }

            for (String line : lines) {
                assertTrue(adminRole.hasPermission(line));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void inheritanceRole() {
        Role userRole = new Role("user");
        userRole.grantPermission("threads.read");
        userRole.grantPermission("threads.answer");

        assertTrue(userRole.hasPermission("threads.read"));
        assertFalse(userRole.hasPermission("threads.open"));

        Role modRole = new Role("mod", userRole);
        modRole.grantPermission("threads.open");

        assertTrue(modRole.hasPermission("threads.read"));
        assertTrue(modRole.hasPermission("threads.open"));
        assertFalse(userRole.hasPermission("threads.open"));

        Role adminRole = new Role("admin", modRole);
        adminRole.grantPermission("user.delete");

        assertTrue(adminRole.hasPermission("threads.read"));
        assertTrue(adminRole.hasPermission("threads.open"));
        assertTrue(adminRole.hasPermission("user.delete"));

        // Inheritated role shouldn't have the new permissions
        assertFalse(modRole.hasPermission("user.delete"));
        assertFalse(userRole.hasPermission("user.delete"));
    }

}
