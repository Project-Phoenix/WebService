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

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;

import de.phoenix.database.DatabaseManager;
import de.phoenix.database.entity.LectureGroupTaskSheet;
import de.phoenix.database.entity.Task;
import de.phoenix.database.entity.TaskSubmissionDates;
import de.phoenix.database.entity.criteria.LectureGroupTaskSheetCriteriaFactory;
import de.phoenix.database.entity.criteria.TaskCriteriaFactory;
import de.phoenix.database.entity.criteria.TaskSubmissionDatesCriteriaFactory;
import de.phoenix.rs.entity.PhoenixLectureGroupTaskSheet;
import de.phoenix.rs.entity.PhoenixTask;
import de.phoenix.rs.entity.PhoenixTaskSubmissionDates;
import de.phoenix.rs.key.ConnectionEntity;
import de.phoenix.rs.key.SelectEntity;
import de.phoenix.webresource.util.AbstractPhoenixResource;

@Path(PhoenixTaskSubmissionDates.WEB_RESOURCE_ROOT)
public class TaskSubmissionDatesResource extends AbstractPhoenixResource<TaskSubmissionDates, PhoenixTaskSubmissionDates> {

    public TaskSubmissionDatesResource() {
        super(TaskSubmissionDatesCriteriaFactory.getInstance());
    }

    @Path(PhoenixTaskSubmissionDates.WEB_RESOURCE_CREATE)
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createLectureGroupTaskSheet(ConnectionEntity connectionEntity) {

        Session session = DatabaseManager.getSession();
        try {
            Transaction trans = session.beginTransaction();

            // Search for taskSheet
            SelectEntity<PhoenixLectureGroupTaskSheet> taskSheetSelector = connectionEntity.getFirstSelectEntity(PhoenixLectureGroupTaskSheet.class);
            Criteria taskSheetCriteria = LectureGroupTaskSheetCriteriaFactory.getInstance().extractCriteria(taskSheetSelector, session);
            LectureGroupTaskSheet taskSheet;
            try {
                taskSheet = (LectureGroupTaskSheet) taskSheetCriteria.uniqueResult();
                if (taskSheet == null) {
                    return Response.status(Status.NOT_FOUND).entity("No entity").build();
                }
            } catch (HibernateException e) {
                return Response.status(Status.NOT_MODIFIED).entity("Multiple entities").build();
            }

            // Search for taskSheet
            SelectEntity<PhoenixTask> taskSelector = connectionEntity.getFirstSelectEntity(PhoenixTask.class);
            Criteria taskCriteria = TaskCriteriaFactory.getInstance().extractCriteria(taskSelector, session);
            Task task;
            try {
                task = (Task) taskCriteria.uniqueResult();
                if (task == null) {
                    return Response.status(Status.NOT_FOUND).entity("No entity").build();
                }
            } catch (HibernateException e) {
                return Response.status(Status.NOT_MODIFIED).entity("Multiple entities").build();
            }

            DateTime deadLine = connectionEntity.getAttribute("deadline");
            DateTime releaseDate = connectionEntity.getAttribute("releaseDate");

            // Check if there is already a submission date for this
            TaskSubmissionDates dates = get(session, taskSheet, task);
            // There is NO date for this task
            if (dates == null) {
                dates = new TaskSubmissionDates();
                dates.setTask(task);
                dates.setLectureGroupTaskSheet(taskSheet);
                dates.setDeadline(deadLine);
                dates.setReleasedate(releaseDate);
                session.save(dates);
            }
            // There IS a data for this task
            else {
                dates.setDeadline(deadLine);
                dates.setReleasedate(releaseDate);
                session.update(dates);
            }

            trans.commit();

        } finally {
            if (session != null) {
                session.close();
            }
        }

        return Response.ok().build();
    }
    private TaskSubmissionDates get(Session session, LectureGroupTaskSheet taskSheet, Task task) {
        Criteria criteria = session.createCriteria(TaskSubmissionDates.class);
        criteria = criteria.add(Restrictions.eq("task", task)).add(Restrictions.eq("lectureGroupTaskSheet", taskSheet));
        return (TaskSubmissionDates) criteria.uniqueResult();
    }
}
