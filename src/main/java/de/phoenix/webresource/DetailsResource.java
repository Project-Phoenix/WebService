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

package de.phoenix.webresource;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.hibernate.Session;

import de.phoenix.database.DatabaseManager;
import de.phoenix.rs.entity.PhoenixDetails;
import de.phoenix.rs.key.SelectEntity;
import de.phoenix.rs.key.UpdateEntity;

@Path("/" + PhoenixDetails.WEB_RESOURCE_ROOT)
public class DetailsResource {

    @Path("/" + PhoenixDetails.WEB_RESOURCE_UPDATE)
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateDetails(UpdateEntity<PhoenixDetails> updatedDetails) {
        Session session = DatabaseManager.getSession();
        try {
            // TODO: Implement update single details
            return Response.ok().build();
        } finally {
            if (session != null)
                session.close();
        }
    }

    @Path("/" + PhoenixDetails.WEB_RESOURCE_DELETE)
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteDetails(SelectEntity<PhoenixDetails> selectDetails) {
        Session session = DatabaseManager.getSession();
        try {
            // TODO: Implement delete single details
            return Response.ok().build();
        } finally {
            if (session != null)
                session.close();
        }
    }
}
