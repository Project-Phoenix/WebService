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

package de.phoenix.database;

import static org.junit.Assert.assertTrue;

import org.hibernate.Session;
import org.junit.Test;

public class ConnectionTest {

    @Test
    public void test() {
        // Start hibernate
        DatabaseManager dbManager = new DatabaseManager();
        Session session = dbManager.openSession();

        session.beginTransaction();

        assertTrue(session.isConnected());

        session.close();

        // Finishe hibernate
    }

}
