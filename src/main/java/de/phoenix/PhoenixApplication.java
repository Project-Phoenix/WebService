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

import java.sql.Connection;
import java.util.HashSet;
import java.util.Set;

import javax.naming.InitialContext;
import javax.sql.DataSource;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import de.phoenix.security.AccountManager;
import de.phoenix.security.TokenManager;
import de.phoenix.webresource.AccountResource;
import de.phoenix.webresource.SubmissionResource;
import de.phoenix.webresource.TokenResource;

@ApplicationPath("/rest")
public class PhoenixApplication extends Application {

    public static final TokenManager tokenManager = new TokenManager();
    public static final AccountManager accountManager = new AccountManager();

    public static Connection databaseConnection;

    public PhoenixApplication() {
        System.out.println("Hello world...Can you hear me:(");
        initDatabaseConnection();
        // Main Constructor - called once in the application lifecycle
    }

    private void initDatabaseConnection() {

        try {
            InitialContext ctx = new InitialContext();
            DataSource ds = (DataSource) ctx.lookup("PhoenixJDBCResource");
            databaseConnection = ds.getConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // Add all webresource to the set so jersey know what resources are existing
    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> classSet = new HashSet<Class<?>>();

        classSet.add(HelloWorld.class);
        classSet.add(SubmissionResource.class);
        classSet.add(TokenResource.class);
        classSet.add(AccountResource.class);

        return classSet;
    }

}
