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
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.hibernate.Session;
import org.hibernate.Transaction;

import de.phoenix.database.DatabaseManager;
import de.phoenix.database.entity.Details;
import de.phoenix.database.entity.Lecture;
import de.phoenix.database.entity.util.ConverterArrayList;
import de.phoenix.rs.entity.PhoenixDetails;
import de.phoenix.rs.entity.PhoenixLecture;

@Path("/" + PhoenixLecture.WEB_RESOURCE_ROOT)
public class LectureResource {

    @Path("/" + PhoenixLecture.WEB_RESOURCE_CREATE)
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createLecture(PhoenixLecture phoenixLecture) {

        Session session = DatabaseManager.getSession();

        try {
            Transaction trans = session.beginTransaction();

            // Store all relevant details of this lecture
            List<Details> details = new ArrayList<Details>();
            for (PhoenixDetails phoenixDetails : phoenixLecture.getLectureDetails()) {
                Details detail = new Details(phoenixDetails);
                Integer id = (Integer) session.save(detail);
                detail.setId(id);

                details.add(detail);
            }

            Lecture lecture = new Lecture(phoenixLecture);
            lecture.setDetails(details);

            // Persist lecture
            session.save(lecture);

            trans.commit();
            return Response.ok().build();

        } finally {
            if (session != null)
                session.close();
        }

    }

    @SuppressWarnings("unchecked")
    @Path("/" + PhoenixLecture.WEB_RESOURCE_GETALL)
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllLectures() {

        Session session = DatabaseManager.getSession();

        try {

            List<Lecture> lectures = session.getNamedQuery("Lecture.findAll").list();

            List<PhoenixLecture> result = new ConverterArrayList<PhoenixLecture>(lectures);

            return Response.ok(PhoenixLecture.toSendableList(result)).build();

        } finally {
            if (session != null)
                session.close();
        }
    }
}
