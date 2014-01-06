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
import org.hibernate.criterion.Restrictions;
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
    protected void setValues(Details entity, PhoenixDetails phoenixEntity) {

        entity.setRoom(phoenixEntity.getRoom());
        entity.setWeekday(phoenixEntity.getWeekDay());
        entity.setStartTime(phoenixEntity.getStartTime().toDateTimeToday().toDate());
        entity.setEndTime(phoenixEntity.getEndTime().toDateTimeToday().toDate());
        entity.setInterval(phoenixEntity.getInverval());
        entity.setStartDate(phoenixEntity.getStartDate().toDate());
        entity.setEndDate(phoenixEntity.getStartDate().toDate());
    }

    @Override
    protected void setCriteria(SelectEntity<PhoenixDetails> selectEntity, Criteria criteria) {
        addParameter(selectEntity, "room", String.class, "room", criteria);
        addParameter(selectEntity, "weekDay", int.class, "weekday", criteria);

        // Necessary because of conversion between Date <-> LocalTime
        // LocalTime
        LocalTime startTime = selectEntity.get("startTime", LocalTime.class);
        if (startTime != null) {
            criteria.add(Restrictions.eq("startTime", startTime.toDateTimeToday().toDate()));
        }

        // LocalTime
        LocalTime endTime = selectEntity.get("endTime", LocalTime.class);
        if (endTime != null)
            criteria.add(Restrictions.eq("endTime", endTime.toDateTimeToday().toDate()));

        // Period
        Period period = selectEntity.get("interval", Period.class);
        if (period != null) {
            criteria.add(Restrictions.eq("interval", Details.PERIOD_FORMAT.print(period)));
        }

        // LocalDate
        LocalDate startDate = selectEntity.get("startTime", LocalDate.class);
        if (startDate != null) {
            criteria.add(Restrictions.eq("startTime", startDate.toDate()));
        }

        // LocalDate
        LocalDate endDate = selectEntity.get("endTime", LocalDate.class);
        if (endDate != null) {
            criteria.add(Restrictions.eq("endTime", endDate.toDate()));
        }
    }
}
