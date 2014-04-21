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
        return onCreate(phoenixLecture, new EntityCreator<Lecture, PhoenixLecture>() {
            public Lecture create(PhoenixLecture phoenixEntity) {
                return new Lecture(phoenixEntity);
            }
        });
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
            checkOnlyOne(lectures);

            Lecture lecture = lectures.get(0);

            List<PhoenixLectureGroup> newPhoenixLectureGroups = addToEntity.getAttachedEntities();
            Transaction trans = session.beginTransaction();
            for (PhoenixLectureGroup phoenixLectureGroup : newPhoenixLectureGroups) {
                lecture.addLectureGroup(new LectureGroup(phoenixLectureGroup, lecture));
            }

            return handlePossibleDuplicateUpdate(session, trans, lecture);
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
            checkOnlyOne(lectures);

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
