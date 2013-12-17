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
import javax.ws.rs.core.Response.Status;

import org.hibernate.Session;
import org.hibernate.Transaction;

import de.phoenix.database.DatabaseManager;
import de.phoenix.database.entity.Details;
import de.phoenix.database.entity.Lecture;
import de.phoenix.database.entity.LectureGroup;
import de.phoenix.rs.entity.PhoenixDetails;
import de.phoenix.rs.entity.PhoenixLecture;
import de.phoenix.rs.entity.PhoenixLectureGroup;

@Path("/" + PhoenixLectureGroup.WEB_RESOURCE_ROOT)
public class LectureGroupResource {

    @Path("/" + PhoenixLectureGroup.WEB_RESOURCE_CREATE)
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createGroup(PhoenixLectureGroup phoenixLectureGroup) {

        PhoenixLecture phoenixLecture = phoenixLectureGroup.getLecture();
        if (phoenixLecture == null) {
            return Response.status(Status.BAD_REQUEST).entity("No lecture attached to group!").build();
        }

        Session session = DatabaseManager.getSession();
        try {

            Lecture lecture = (Lecture) session.getNamedQuery("Lecture.findByName").setParameter("name", phoenixLecture.getTitle()).uniqueResult();

            if (lecture == null) {
                return Response.status(Status.BAD_REQUEST).entity("No lecture find in database!").build();
            }

            LectureGroup lectureGroup = new LectureGroup(phoenixLectureGroup);

            lectureGroup.setLecture(lecture);

            Transaction trans = session.beginTransaction();
            // Store all relevant details of this lecture
            List<Details> details = new ArrayList<Details>();
            for (PhoenixDetails phoenixDetails : phoenixLectureGroup.getDetails()) {
                Details detail = new Details(phoenixDetails);
                Integer id = (Integer) session.save(detail);
                detail.setId(id);

                details.add(detail);
            }

            lectureGroup.setDetails(details);

            session.save(lectureGroup);

            trans.commit();
            return Response.ok().build();

        } finally {
            if (session != null)
                session.close();
        }

    }

}
