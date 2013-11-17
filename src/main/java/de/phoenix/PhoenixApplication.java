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

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import de.phoenix.rs.JacksonConfigurator;
import de.phoenix.webresource.AccountResource;
import de.phoenix.webresource.LectureGroupResource;
import de.phoenix.webresource.LectureResource;
import de.phoenix.webresource.SubmissionResource;
import de.phoenix.webresource.TaskResource;
import de.phoenix.webresource.TaskSheetResource;
import de.phoenix.webresource.TokenResource;

@ApplicationPath("/rest")
public class PhoenixApplication extends Application {

    // TODO: Is this necessary?

    public PhoenixApplication() {
        super();
        // Main Constructor - called once in the application lifecycle
    }

    // Add all webresource to the set so jersey know what resources are existing
    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> classSet = new HashSet<Class<?>>();

        classSet.add(HelloWorld.class);
        classSet.add(SubmissionResource.class);
        classSet.add(TokenResource.class);
        classSet.add(AccountResource.class);
        classSet.add(TaskResource.class);
        classSet.add(TaskSheetResource.class);
        classSet.add(LectureResource.class);
        classSet.add(LectureGroupResource.class);

        classSet.add(JacksonConfigurator.class);

        return classSet;
    }

}
