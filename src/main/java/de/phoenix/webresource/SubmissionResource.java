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

import de.phoenix.database.entity.TaskSubmission;
import de.phoenix.database.entity.criteria.TaskSubmissionCriteriaFactory;
import de.phoenix.rs.entity.PhoenixSubmission;
import de.phoenix.rs.key.SelectEntity;
import de.phoenix.webresource.util.AbstractPhoenixResource;

/**
 * Webresource for uploading and getting submissions from user.
 * 
 */
@Path(PhoenixSubmission.WEB_RESOURCE_ROOT)
public class SubmissionResource extends AbstractPhoenixResource<TaskSubmission, PhoenixSubmission> {

    public SubmissionResource() {
        super(TaskSubmissionCriteriaFactory.getInstance());
    }

    @Path(PhoenixSubmission.WEB_RESOURCE_GET)
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSubmissions(SelectEntity<PhoenixSubmission> selectEntity) {
        return onGet(selectEntity);
    }

    @Path(PhoenixSubmission.WEB_RESOURCE_DELETE)
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteSubmission(SelectEntity<PhoenixSubmission> selectEntity) {
        return onDelete(selectEntity);
    }
}