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

import java.util.List;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;

import de.phoenix.database.DatabaseManager;

public class DatabaseCleaner {

    private static DatabaseCleaner INSTANCE = new DatabaseCleaner();

    public static DatabaseCleaner getInstance() {
        return INSTANCE;
    }

    private DatabaseCleaner() {
    }

    public void run() {
        cleanDatabase();
    }

    @SuppressWarnings("unchecked")
    private void cleanDatabase() {

        Session session = DatabaseManager.getSession();

        session.createSQLQuery("SET FOREIGN_KEY_CHECKS = 0;").executeUpdate();
        SQLQuery query = session.createSQLQuery("SELECT TABLE_NAME FROM information_schema.TABLES WHERE TABLE_SCHEMA=DATABASE();");

        List<String> tableNames = query.list();
        Transaction transaction = session.beginTransaction();
        for (String table : tableNames) {
            session.createSQLQuery("DELETE FROM " + table + ";").executeUpdate();
        }
        transaction.commit();
        session.createSQLQuery("SET FOREIGN_KEY_CHECKS = 1;").executeUpdate();

        session.close();
    }

}
