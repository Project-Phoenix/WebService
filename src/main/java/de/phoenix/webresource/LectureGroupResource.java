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

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.hibernate.Criteria;
import org.hibernate.Session;

import de.phoenix.database.entity.Details;
import de.phoenix.database.entity.Lecture;
import de.phoenix.database.entity.LectureGroup;
import de.phoenix.rs.entity.PhoenixDetails;
import de.phoenix.rs.entity.PhoenixLectureGroup;
import de.phoenix.rs.key.SelectEntity;
import de.phoenix.webresource.util.AbstractPhoenixResource;

@Path("/" + PhoenixLectureGroup.WEB_RESOURCE_ROOT)
public class LectureGroupResource extends AbstractPhoenixResource<LectureGroup, PhoenixLectureGroup> {

    public LectureGroupResource() {
        super(LectureGroup.class);
    }

    @Path("/" + PhoenixLectureGroup.WEB_RESOURCE_CREATE)
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createGroup(PhoenixLectureGroup phoenixLectureGroup) {

        return onCreate(phoenixLectureGroup);
    }

    @Override
    protected LectureGroup create(PhoenixLectureGroup phoenixEntity, Session session) {

        Lecture lecture = (Lecture) session.getNamedQuery("Lecture.findByName").setParameter("name", phoenixEntity.getLecture().getTitle()).uniqueResult();

        if (lecture == null) {
            return null;
        }

        LectureGroup lectureGroup = new LectureGroup(phoenixEntity);
        lectureGroup.setLecture(lecture);

        // Store all relevant details of this lecture
        List<Details> details = new ArrayList<Details>();
        for (PhoenixDetails phoenixDetails : phoenixEntity.getDetails()) {
            Details detail = new Details(phoenixDetails);
            details.add(detail);
        }

        lectureGroup.setDetails(details);
        return lectureGroup;
    }

    @Override
    protected void setCriteria(SelectEntity<PhoenixLectureGroup> selectEntity, Criteria criteria) {
        addParameter(selectEntity, "name", String.class, "name", criteria);
        addParameter(selectEntity, "maxMember", int.class, "maxMember", criteria);
        addParameter(selectEntity, "submissionDeadLineTime", String.class, "name", criteria);
        addParameter(selectEntity, "submissionDeadlineWeekyday", String.class, "name", criteria);
        addParameter(selectEntity, "name", String.class, "name", criteria);
    }

    @Override
    protected void setValues(LectureGroup entity, PhoenixLectureGroup phoenixEntity) {
        entity.setName(phoenixEntity.getName());
    }

}
