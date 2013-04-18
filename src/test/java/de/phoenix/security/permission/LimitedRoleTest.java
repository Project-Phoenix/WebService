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

import static org.junit.Assert.*;

import org.junit.Test;

public class LimitedRoleTest {

    @Test
    public void test() {
        Role admin = new Role("admin");
        admin.grantPermission("user.*");
        admin.grantPermission("threads.*");

        assertTrue(admin.hasPermission("user.toAdmin"));

        LimitedRole supAdmin = new LimitedRole("supAdmin", admin);
        supAdmin.disallowPermission("user.toAdmin");

        assertTrue(admin.hasPermission("user.toAdmin"));

        assertFalse(supAdmin.hasPermission("user.toAdmin"));
        assertTrue(supAdmin.hasPermission("user.*"));
    }

}
