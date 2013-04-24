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
import java.sql.Statement;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path("/helloworld")
public class HelloWorld {

    @GET
    @Produces("text/plain")
    public String helloWorld() {
        System.out.println("Hello World!");
        try {
            Connection con = PhoenixApplication.databaseConnection;
            if (con == null) {
                return "ERROR";
            }
            Statement st = con.createStatement();
            st.executeUpdate("INSERT INTO bla (text) VALUES ('hello world');");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Hello World!";
    }

}
