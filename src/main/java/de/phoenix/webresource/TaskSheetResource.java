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
import javax.ws.rs.core.Response.Status;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.joda.time.DateTime;

import de.phoenix.database.DatabaseManager;
import de.phoenix.database.entity.Task;
import de.phoenix.database.entity.TaskSheet;
import de.phoenix.database.entity.criteria.TaskCriteriaFactory;
import de.phoenix.database.entity.criteria.TaskSheetCriteriaFactory;
import de.phoenix.database.entity.util.ConverterUtil;
import de.phoenix.rs.entity.PhoenixTask;
import de.phoenix.rs.entity.PhoenixTaskSheet;
import de.phoenix.rs.key.ConnectionEntity;
import de.phoenix.rs.key.SelectEntity;
import de.phoenix.webresource.util.AbstractPhoenixResource;

@Path("/" + PhoenixTaskSheet.WEB_RESOURCE_ROOT)
public class TaskSheetResource extends AbstractPhoenixResource<TaskSheet, PhoenixTaskSheet> {

    public TaskSheetResource() {
        super(TaskSheetCriteriaFactory.getInstance());
    }

    @Path("/" + PhoenixTaskSheet.WEB_RESOURCE_CREATE)
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createTaskSheet(PhoenixTaskSheet phoenixSheet) {

        return onCreate(phoenixSheet);

    }

    @Override
    protected TaskSheet create(PhoenixTaskSheet phoenixEntity, Session session) {
        TaskSheet taskSheet = new TaskSheet(phoenixEntity);
        return taskSheet;
    }

    @SuppressWarnings("unchecked")
    @Path("/" + PhoenixTaskSheet.WEB_RESOURCE_GETALL)
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllSheets() {
        Session session = DatabaseManager.getSession();

        try {
            List<TaskSheet> sheets = session.getNamedQuery("TaskSheet.findAll").list();

            List<PhoenixTaskSheet> result = ConverterUtil.convert(sheets);
            session.close();

            return Response.ok(result).build();

        } finally {
            if (session != null)
                session.close();
        }
    }

    @Path("/" + PhoenixTaskSheet.WEB_RESOURCE_GET)
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getTaskSheets(SelectEntity<PhoenixTaskSheet> selectEntity) {
        return onGet(selectEntity);
    }

    // TODO: Move to other method name?
    @Path("/" + PhoenixTaskSheet.WEB_RESOURCE_CONNECT_TASKSHEET_WITH_TASK)
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response connectTasksWithTaskSheet(ConnectionEntity connectionEntity) {

        Session session = DatabaseManager.getSession();
        try {
            Transaction trans = session.beginTransaction();

            TaskSheet taskSheet = new TaskSheet();

            taskSheet.setTitle((String) connectionEntity.getAttribute("title"));
            taskSheet.setCreationDate(DateTime.now());

            // Search Tasks
            List<SelectEntity<PhoenixTask>> taskSelectors = connectionEntity.getSelectEntities(PhoenixTask.class);
            TaskCriteriaFactory taskCriteriaFactory = TaskCriteriaFactory.getInstance();
            List<Task> tasks = new ArrayList<Task>(taskSelectors.size());

            for (SelectEntity<PhoenixTask> selectEntity : taskSelectors) {
                Criteria criteria = taskCriteriaFactory.extractCriteria(selectEntity, session);
                try {
                    Task task = (Task) criteria.uniqueResult();
                    if (task == null) {
                        return Response.status(Status.NOT_FOUND).entity("No entity").build();
                    }
                    tasks.add(task);
                } catch (HibernateException e) {
                    return Response.status(Status.NOT_MODIFIED).entity("Multiple entities").build();
                }
            }

            taskSheet.setTasks(tasks);

            session.save(taskSheet);
            trans.commit();

        } finally {
            if (session != null) {
                session.close();
            }
        }

        return Response.ok().build();
    }
}
