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
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import de.phoenix.database.DatabaseManager;
import de.phoenix.database.entity.Task;
import de.phoenix.database.entity.TaskSheet;
import de.phoenix.database.entity.criteria.TaskCriteriaFactory;
import de.phoenix.database.entity.criteria.TaskSheetCriteriaFactory;
import de.phoenix.rs.PhoenixStatusType;
import de.phoenix.rs.entity.PhoenixTask;
import de.phoenix.rs.entity.PhoenixTaskSheet;
import de.phoenix.rs.key.ConnectionEntity;
import de.phoenix.rs.key.SelectEntity;
import de.phoenix.webresource.util.AbstractPhoenixResource;

@Path(PhoenixTaskSheet.WEB_RESOURCE_ROOT)
public class TaskSheetResource extends AbstractPhoenixResource<TaskSheet, PhoenixTaskSheet> {

    public TaskSheetResource() {
        super(TaskSheetCriteriaFactory.getInstance());
    }

    @Path(PhoenixTaskSheet.WEB_RESOURCE_GET)
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getTaskSheets(SelectEntity<PhoenixTaskSheet> selectEntity) {
        return onGet(selectEntity);
    }

    // TODO: Move to other method name?
    @Path(PhoenixTaskSheet.WEB_RESOURCE_CONNECT_TASKSHEET_WITH_TASK)
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response connectTasksWithTaskSheet(ConnectionEntity connectionEntity) {

        Session session = DatabaseManager.getSession();
        try {
            Transaction trans = session.beginTransaction();

            String title = connectionEntity.getAttribute("title");
            TaskSheet taskSheet = new TaskSheet(title);

            // Search Tasks
            List<SelectEntity<PhoenixTask>> taskSelectors = connectionEntity.getSelectEntities(PhoenixTask.class);
            TaskCriteriaFactory taskCriteriaFactory = TaskCriteriaFactory.getInstance();
            List<Task> tasks = new ArrayList<Task>(taskSelectors.size());

            for (SelectEntity<PhoenixTask> selectEntity : taskSelectors) {
                Criteria criteria = taskCriteriaFactory.extractCriteria(selectEntity, session);
                try {
                    Task task = (Task) criteria.uniqueResult();
                    if (task == null) {
                        return Response.status(PhoenixStatusType.NO_ENTITIES).build();
                    }
                    tasks.add(task);
                } catch (HibernateException e) {
                    return Response.status(PhoenixStatusType.MULTIPLE_ENTITIES).build();
                }
            }

            taskSheet.setTasks(tasks);

            return handlePossibleDuplicateInsert(session, trans, taskSheet);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Path(PhoenixTaskSheet.WEB_RESOURCE_ADD_TASK_TO_TASKSHEET)
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addTaskToTaskSheet(ConnectionEntity connectionEntity) {

        Session session = DatabaseManager.getSession();
        try {

            // Search for the task sheet
            String title = connectionEntity.getAttribute("title");
            Criteria criteria = session.createCriteria(TaskSheet.class).add(Restrictions.eq("title", title));
            TaskSheet taskSheet;
            try {
                taskSheet = (TaskSheet) criteria.uniqueResult();
                if (taskSheet == null) {
                    return Response.status(PhoenixStatusType.NO_ENTITIES).build();
                }
            } catch (HibernateException e) {
                return Response.status(PhoenixStatusType.MULTIPLE_ENTITIES).build();
            }

            // Search Tasks
            List<SelectEntity<PhoenixTask>> taskSelectors = connectionEntity.getSelectEntities(PhoenixTask.class);
            TaskCriteriaFactory taskCriteriaFactory = TaskCriteriaFactory.getInstance();
            List<Task> tasks = new ArrayList<Task>(taskSelectors.size());
            for (SelectEntity<PhoenixTask> selectEntity : taskSelectors) {
                criteria = taskCriteriaFactory.extractCriteria(selectEntity, session);
                try {
                    Task task = (Task) criteria.uniqueResult();
                    if (task == null) {
                        return Response.status(PhoenixStatusType.NO_ENTITIES).build();
                    }
                    tasks.add(task);
                } catch (HibernateException e) {
                    return Response.status(PhoenixStatusType.MULTIPLE_ENTITIES).build();
                }
            }

            Transaction trans = session.beginTransaction();
            List<Task> taskSheetTasks = taskSheet.getTasks();
            for (Task task : tasks) {
                if (!taskSheetTasks.contains(task))
                    taskSheetTasks.add(task);
            }
            session.update(taskSheet);
            trans.commit();
        } finally {
            if (session != null)
                session.close();
        }

        return Response.ok().build();
    }

    @Path(PhoenixTaskSheet.WEB_RESOURCE_REMOVE_TASK_FROM_TASKSHEET)
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response removeTaskFromTaskSheet(ConnectionEntity connectionEntity) {
        Session session = DatabaseManager.getSession();
        try {
            Transaction trans = session.beginTransaction();

            SelectEntity<PhoenixTask> taskSelector = connectionEntity.getFirstSelectEntity(PhoenixTask.class);
            SelectEntity<PhoenixTaskSheet> taskSheetSelector = connectionEntity.getFirstSelectEntity(PhoenixTaskSheet.class);

            // Search for the task
            // TODO: Refactor and generalize it to a own method
            Task task;
            try {
                task = (Task) TaskCriteriaFactory.getInstance().extractCriteria(taskSelector, session).uniqueResult();
                if (task == null) {
                    return Response.status(PhoenixStatusType.NO_ENTITIES).build();
                }
            } catch (HibernateException e) {
                return Response.status(PhoenixStatusType.MULTIPLE_ENTITIES).build();
            }

            // Search for the taksheet
            TaskSheet taskSheet;
            try {
                taskSheet = (TaskSheet) TaskSheetCriteriaFactory.getInstance().extractCriteria(taskSheetSelector, session).uniqueResult();
                if (taskSheet == null) {
                    return Response.status(PhoenixStatusType.NO_ENTITIES).build();
                }
            } catch (HibernateException e) {
                return Response.status(PhoenixStatusType.MULTIPLE_ENTITIES).build();
            }

            // Delete connection
            // Hibernate needs to remove both links
            task.getTaskSheets().remove(taskSheet);
            taskSheet.getTasks().remove(task);
            session.update(task);

            trans.commit();

            return Response.ok().build();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}
