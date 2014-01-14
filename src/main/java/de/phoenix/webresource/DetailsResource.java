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

import org.hibernate.Criteria;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.joda.time.Period;

import de.phoenix.database.entity.Details;
import de.phoenix.rs.entity.PhoenixDetails;
import de.phoenix.rs.key.SelectEntity;
import de.phoenix.rs.key.UpdateEntity;
import de.phoenix.webresource.util.AbstractPhoenixResource;

@Path("/" + PhoenixDetails.WEB_RESOURCE_ROOT)
public class DetailsResource extends AbstractPhoenixResource<Details, PhoenixDetails> {

    public DetailsResource() {
        super(Details.class);
    }

    @Path("/" + PhoenixDetails.WEB_RESOURCE_UPDATE)
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateDetails(UpdateEntity<PhoenixDetails> updatedDetails) {
        return onUpdate(updatedDetails);
    }

    @Path("/" + PhoenixDetails.WEB_RESOURCE_DELETE)
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteDetails(SelectEntity<PhoenixDetails> selectDetails) {
        return onDelete(selectDetails);
    }

    @Override
    protected void setCriteria(SelectEntity<PhoenixDetails> selectEntity, Criteria criteria) {
        addParameter(selectEntity, "room", String.class, "room", criteria);
        addParameter(selectEntity, "weekDay", int.class, "weekday", criteria);
        addParameter(selectEntity, "startTime", LocalTime.class, "startTime", criteria);
        addParameter(selectEntity, "endTime", LocalTime.class, "endTime", criteria);
        addParameter(selectEntity, "interval", Period.class, "interval", criteria);
        addParameter(selectEntity, "startDate", LocalDate.class, "startDate", criteria);
        addParameter(selectEntity, "endDate", LocalDate.class, "endDate", criteria);
    }
}
