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

package de.phoenix.webresource;

import static org.junit.Assert.assertFalse;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.test.framework.AppDescriptor;
import com.sun.jersey.test.framework.JerseyTest;
import com.sun.jersey.test.framework.WebAppDescriptor;

import de.phoenix.junit.OrderedRunner;
import de.phoenix.junit.OrderedRunner.Order;

@RunWith(OrderedRunner.class)
public class DebugTest extends JerseyTest {

    @Override
    protected AppDescriptor configure() {
        return new WebAppDescriptor.Builder("de.phoenix").build();
    }

    @Test
    @Order(1)
    public void testDebug() {
        WebResource web = resource().path("debug");
        String res = web.get(String.class);
        assertFalse(res.isEmpty());
    }
}
