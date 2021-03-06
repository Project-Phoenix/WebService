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

package de.phoenix.database.entity.criteria;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import de.phoenix.database.entity.Task;
import de.phoenix.database.entity.TaskSubmission;
import de.phoenix.rs.entity.PhoenixSubmission;
import de.phoenix.rs.entity.PhoenixTask;
import de.phoenix.rs.key.SelectEntity;

/**
 * Construct criterias for {@link TaskSubmission}
 */
public class TaskSubmissionCriteriaFactory extends CriteriaFactory<TaskSubmission, PhoenixSubmission> {

    private final static TaskSubmissionCriteriaFactory instance = new TaskSubmissionCriteriaFactory();

    public static TaskSubmissionCriteriaFactory getInstance() {
        return instance;
    }

    public TaskSubmissionCriteriaFactory() {
        super(TaskSubmission.class);
    }

    @Override
    public void setAttributes(SelectEntity<PhoenixSubmission> selectEntity, Criteria criteria, Session session) {
        addParameter(selectEntity, "date", criteria);
        addParameter(selectEntity, "status", criteria);
        addParameter(selectEntity, "statusText", criteria);

        SelectEntity<PhoenixTask> lectureKey = selectEntity.get("task");
        if (lectureKey != null) {
            Task task = search(lectureKey, session, TaskCriteriaFactory.getInstance());
            criteria.add(Restrictions.eq("task", task));
        }
    }
}
