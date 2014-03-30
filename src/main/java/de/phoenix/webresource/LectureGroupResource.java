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
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import de.phoenix.database.entity.LectureGroup;
import de.phoenix.database.entity.criteria.LectureGroupCriteriaFactory;
import de.phoenix.rs.entity.PhoenixLectureGroup;
import de.phoenix.rs.key.SelectEntity;
import de.phoenix.rs.key.UpdateEntity;
import de.phoenix.webresource.util.AbstractPhoenixResource;

@Path(PhoenixLectureGroup.WEB_RESOURCE_ROOT)
public class LectureGroupResource extends AbstractPhoenixResource<LectureGroup, PhoenixLectureGroup> {

    public LectureGroupResource() {
        super(LectureGroupCriteriaFactory.getInstance());
    }

    @Path(PhoenixLectureGroup.WEB_RESOURCE_GET)
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getLectureGroup(SelectEntity<PhoenixLectureGroup> selectEntity) {
        return onGet(selectEntity);
    }

    @Path(PhoenixLectureGroup.WEB_RESOURCE_UPDATE)
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateLectureGroup(UpdateEntity<PhoenixLectureGroup> updatedEntity) {
        return onUpdate(updatedEntity);
    }

    @Path(PhoenixLectureGroup.WEB_RESOURCE_DELETE)
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteLectureGroup(SelectEntity<PhoenixLectureGroup> selectEntity) {
        return onDelete(selectEntity);
    }
}
