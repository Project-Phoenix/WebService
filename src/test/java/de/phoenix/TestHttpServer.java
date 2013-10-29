package de.phoenix;
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

import java.io.IOException;
import java.net.URI;

import com.sun.jersey.api.container.httpserver.HttpServerFactory;
import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.api.core.ResourceConfig;
import com.sun.net.httpserver.HttpServer;

public class TestHttpServer {

    private HttpServer httpServer;

    public TestHttpServer(String baseURI) throws IllegalArgumentException, IOException {
        this(baseURI, "de.phoenix");
    }

    public TestHttpServer(String baseURI, String... packages) throws IllegalArgumentException, IOException {
        ResourceConfig rc = new PackagesResourceConfig(packages);
        httpServer = HttpServerFactory.create(URI.create(baseURI), rc);
        httpServer.start();
    }

    public void stop() {
        httpServer.stop(0);
    }
}
