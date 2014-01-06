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

import org.hibernate.Criteria;
import org.hibernate.Session;

import de.phoenix.database.DatabaseManager;
import de.phoenix.database.entity.Details;
import de.phoenix.database.entity.Lecture;
import de.phoenix.database.entity.util.ConverterArrayList;
import de.phoenix.rs.entity.PhoenixDetails;
import de.phoenix.rs.entity.PhoenixLecture;
import de.phoenix.rs.key.SelectEntity;
import de.phoenix.rs.key.UpdateEntity;
import de.phoenix.webresource.util.AbstractPhoenixResource;

@Path("/" + PhoenixLecture.WEB_RESOURCE_ROOT)
public class LectureResource extends AbstractPhoenixResource<Lecture, PhoenixLecture> {

    public LectureResource() {
        super(Lecture.class);
    }

    @Path("/" + PhoenixLecture.WEB_RESOURCE_CREATE)
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createLecture(PhoenixLecture phoenixLecture) {

        return onCreate(phoenixLecture);
    }

    @Override
    protected Lecture create(PhoenixLecture phoenixEntity, Session session) {
        // Store all relevant details of this lecture
        List<Details> details = new ArrayList<Details>();
        for (PhoenixDetails phoenixDetails : phoenixEntity.getLectureDetails()) {
            Details detail = new Details(phoenixDetails);
            details.add(detail);
        }

        Lecture lecture = new Lecture(phoenixEntity);
        lecture.setDetails(details);

        return lecture;
    }

    // TODO: Remove next version
    @SuppressWarnings("unchecked")
    @Path("/" + PhoenixLecture.WEB_RESOURCE_GETALL)
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Deprecated
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

    @Path("/" + PhoenixLecture.WEB_RESOURCE_UPDATE)
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateLecture(UpdateEntity<PhoenixLecture> updateLecture) {
        return onUpdate(updateLecture);
    }

    @Path("/" + PhoenixLecture.WEB_RESOURCE_DELETE)
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteLecture(SelectEntity<PhoenixLecture> selectLecture) {
        return onDelete(selectLecture);
    }

    @Path("/" + PhoenixLecture.WEB_RESOURCE_GET)
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getLecture(SelectEntity<PhoenixLecture> selectLecture) {

        List<PhoenixLecture> list = onGet(selectLecture);

        return Response.ok(PhoenixLecture.toSendableList(list)).build();
    }

    @Override
    protected void setValues(Lecture entity, PhoenixLecture phoenixEntity) {
        entity.setName(phoenixEntity.getTitle());
    }

    @Override
    protected void setCriteria(SelectEntity<PhoenixLecture> selectEntity, Criteria criteria) {
        addParameter(selectEntity, "title", String.class, "name", criteria);
    }
}
