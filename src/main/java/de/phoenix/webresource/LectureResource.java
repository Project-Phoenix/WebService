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
import de.phoenix.database.entity.LectureGroup;
import de.phoenix.database.entity.criteria.LectureCriteriaFactory;
import de.phoenix.database.entity.util.ConverterUtil;
import de.phoenix.rs.entity.PhoenixDetails;
import de.phoenix.rs.entity.PhoenixLecture;
import de.phoenix.rs.entity.PhoenixLectureGroup;
import de.phoenix.rs.key.AddToEntity;
import de.phoenix.rs.key.SelectEntity;
import de.phoenix.rs.key.UpdateEntity;
import de.phoenix.webresource.util.AbstractPhoenixResource;

@Path(PhoenixLecture.WEB_RESOURCE_ROOT)
public class LectureResource extends AbstractPhoenixResource<Lecture, PhoenixLecture> {

    public LectureResource() {
        super(LectureCriteriaFactory.getInstance());
    }

    @Path(PhoenixLecture.WEB_RESOURCE_CREATE)
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createLecture(PhoenixLecture phoenixLecture) {

        return onCreate(phoenixLecture, LectureCreator.INSTANCE);
    }

    private static class LectureCreator implements EntityCreator<Lecture, PhoenixLecture> {

        private final static LectureCreator INSTANCE = new LectureCreator();

        @Override
        public Lecture create(PhoenixLecture phoenixEntity, Session session) {
            return new Lecture(phoenixEntity);
        }
    }

    // TODO: Remove next version
    @SuppressWarnings("unchecked")
    @Path(PhoenixLecture.WEB_RESOURCE_GETALL)
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Deprecated
    public Response getAllLectures() {

        Session session = DatabaseManager.getSession();

        try {

            List<Lecture> lectures = session.getNamedQuery("Lecture.findAll").list();

            List<PhoenixLecture> result = ConverterUtil.convert(lectures);

            return Response.ok(result).build();

        } finally {
            if (session != null)
                session.close();
        }
    }

    @Path(PhoenixLecture.WEB_RESOURCE_UPDATE)
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateLecture(UpdateEntity<PhoenixLecture> updateLecture) {
        return onUpdate(updateLecture);
    }

    @Path(PhoenixLecture.WEB_RESOURCE_DELETE)
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteLecture(SelectEntity<PhoenixLecture> selectLecture) {
        return onDelete(selectLecture);
    }

    @Path(PhoenixLecture.WEB_RESOURCE_GET)
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getLecture(SelectEntity<PhoenixLecture> selectLecture) {
        return onGet(selectLecture);
    }

    @Path("/" + PhoenixLecture.WEB_RESOURCE_ADD_GROUP)
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addGroup(AddToEntity<PhoenixLecture, PhoenixLectureGroup> addToEntity) {

        Session session = DatabaseManager.getSession();
        try {
            List<Lecture> lectures = searchEntity(addToEntity, session);
            Response response = checkOnlyOne(lectures);
            if (response.getStatus() != 200)
                return response;

            Lecture lecture = lectures.get(0);

            List<PhoenixLectureGroup> newPhoenixLectureGroups = addToEntity.getAttachedEntities();
            Transaction trans = session.beginTransaction();
            for (PhoenixLectureGroup phoenixLectureGroup : newPhoenixLectureGroups) {
                lecture.addLectureGroup(new LectureGroup(phoenixLectureGroup, lecture));
            }

            session.update(lecture);
            trans.commit();

            return Response.ok().build();
        } finally {
            if (session != null)
                session.close();
        }
    }
    @Path(PhoenixLecture.WEB_RESOURCE_ADD_DETAIL)
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addDetail(AddToEntity<PhoenixLecture, PhoenixDetails> addToEntity) {

        Session session = DatabaseManager.getSession();
        try {
            List<Lecture> lectures = searchEntity(addToEntity, session);
            Response response = checkOnlyOne(lectures);
            if (response.getStatus() != 200)
                return response;

            Lecture lecture = lectures.get(0);

            List<PhoenixDetails> newPhoenixDetails = addToEntity.getAttachedEntities();
            Transaction trans = session.beginTransaction();
            for (PhoenixDetails phoenixDetail : newPhoenixDetails) {
                lecture.addDetail(new Details(phoenixDetail));
            }

            session.update(lecture);
            trans.commit();

            return Response.ok().build();
        } finally {
            if (session != null)
                session.close();
        }
    }
}
