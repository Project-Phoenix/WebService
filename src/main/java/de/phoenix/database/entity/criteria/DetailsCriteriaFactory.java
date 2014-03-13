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

package de.phoenix.database.entity.criteria;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.joda.time.Period;

import de.phoenix.database.entity.Details;
import de.phoenix.rs.entity.PhoenixDetails;
import de.phoenix.rs.key.SelectEntity;

/**
 * Construct criterias for {@link Details}
 */
public class DetailsCriteriaFactory extends CriteriaFactory<Details, PhoenixDetails> {

    private final static DetailsCriteriaFactory instance = new DetailsCriteriaFactory();

    private DetailsCriteriaFactory() {
        super(Details.class);
    }

    public static DetailsCriteriaFactory getInstance() {
        return instance;
    }

    @Override
    public void setAttributes(SelectEntity<PhoenixDetails> selectEntity, Criteria criteria, Session session) {
        addParameter(selectEntity, "room", String.class, criteria);
        addParameter(selectEntity, "weekDay", int.class, criteria);
        addParameter(selectEntity, "startTime", LocalTime.class, criteria);
        addParameter(selectEntity, "endTime", LocalTime.class, criteria);
        addParameter(selectEntity, "interval", Period.class, criteria);
        addParameter(selectEntity, "startDate", LocalDate.class, criteria);
        addParameter(selectEntity, "endDate", LocalDate.class, criteria);
    }

}
