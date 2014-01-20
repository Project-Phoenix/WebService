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

package de.phoenix.webresource;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.joda.time.DateTime;

import de.phoenix.database.DatabaseManager;
import de.phoenix.database.entity.LectureGroup;
import de.phoenix.database.entity.LectureGroupTaskSheet;
import de.phoenix.database.entity.TaskSheet;
import de.phoenix.database.entity.criteria.LectureGroupCriteriaFactory;
import de.phoenix.database.entity.criteria.LectureGroupTaskSheetCriteriaFactory;
import de.phoenix.database.entity.criteria.TaskSheetCriteriaFactory;
import de.phoenix.rs.entity.PhoenixLectureGroup;
import de.phoenix.rs.entity.PhoenixLectureGroupTaskSheet;
import de.phoenix.rs.entity.PhoenixTaskSheet;
import de.phoenix.rs.key.ConnectionEntity;
import de.phoenix.rs.key.SelectEntity;
import de.phoenix.webresource.util.AbstractPhoenixResource;

@Path("/" + PhoenixLectureGroupTaskSheet.WEB_RESOURCE_ROOT)
public class LectureGroupTaskSheetResource extends AbstractPhoenixResource<LectureGroupTaskSheet, PhoenixLectureGroupTaskSheet> {

    public LectureGroupTaskSheetResource() {
        super(LectureGroupTaskSheetCriteriaFactory.getInstance());
    }

    @Path("/" + PhoenixLectureGroupTaskSheet.WEB_RESOURCE_CREATE)
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createLectureGroupTaskSheet(ConnectionEntity connectionEntity) {

        Session session = DatabaseManager.getSession();
        try {
            Transaction trans = session.beginTransaction();

            // Search for taskSheet
            SelectEntity<PhoenixTaskSheet> taskSheetSelector = connectionEntity.getFirstSelectEntity(PhoenixTaskSheet.class);
            Criteria taskSheetCriteria = TaskSheetCriteriaFactory.getInstance().extractCriteria(taskSheetSelector, session);
            TaskSheet taskSheet;
            try {
                taskSheet = (TaskSheet) taskSheetCriteria.uniqueResult();
                if (taskSheet == null) {
                    return Response.status(Status.NOT_FOUND).entity("No entity").build();
                }
            } catch (HibernateException e) {
                return Response.status(Status.NOT_MODIFIED).entity("Multiple entities").build();
            }

            // Search for groups
            LectureGroupCriteriaFactory groupCriteriaFactory = LectureGroupCriteriaFactory.getInstance();
            List<SelectEntity<PhoenixLectureGroup>> groupSelectors = connectionEntity.getSelectEntities(PhoenixLectureGroup.class);

            for (SelectEntity<PhoenixLectureGroup> selectEntity : groupSelectors) {
                Criteria criteria = groupCriteriaFactory.extractCriteria(selectEntity, session);
                try {
                    LectureGroup group = (LectureGroup) criteria.uniqueResult();
                    if (group == null) {
                        return Response.status(Status.NOT_FOUND).entity("No entity").build();
                    }

                    // Persist lecture group task sheet
                    LectureGroupTaskSheet groupTaskSheet = new LectureGroupTaskSheet();
                    groupTaskSheet.setDefaultDeadline((DateTime) connectionEntity.getAttribute("defaultDeadLine"));
                    groupTaskSheet.setDefaultReleaseDate((DateTime) connectionEntity.getAttribute("defaultReleaseDate"));
                    groupTaskSheet.setTaskSheet(taskSheet);
                    groupTaskSheet.setLectureGroup(group);
                    session.save(groupTaskSheet);
                } catch (HibernateException e) {
                    return Response.status(Status.NOT_MODIFIED).entity("Multiple entities").build();
                }
            }

            trans.commit();

        } finally {
            if (session != null) {
                session.close();
            }
        }

        return Response.ok().build();
    }

    @Path("/" + PhoenixLectureGroupTaskSheet.WEB_RESOURCE_GET)
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response get(SelectEntity<PhoenixLectureGroupTaskSheet> selectEntity) {
        return onGet(selectEntity);
    }
}
