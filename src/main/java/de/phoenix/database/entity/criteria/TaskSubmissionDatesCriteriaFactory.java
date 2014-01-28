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
import org.joda.time.DateTime;

import de.phoenix.database.entity.LectureGroupTaskSheet;
import de.phoenix.database.entity.Task;
import de.phoenix.database.entity.TaskSubmissionDates;
import de.phoenix.rs.entity.PhoenixLectureGroupTaskSheet;
import de.phoenix.rs.entity.PhoenixTask;
import de.phoenix.rs.entity.PhoenixTaskSubmissionDates;
import de.phoenix.rs.key.SelectEntity;

public class TaskSubmissionDatesCriteriaFactory extends CriteriaFactory<TaskSubmissionDates, PhoenixTaskSubmissionDates> {

    private final static TaskSubmissionDatesCriteriaFactory instance = new TaskSubmissionDatesCriteriaFactory();

    private TaskSubmissionDatesCriteriaFactory() {
        super(TaskSubmissionDates.class);
    }

    public static TaskSubmissionDatesCriteriaFactory getInstance() {
        return instance;
    }

    @Override
    public void setAttributes(SelectEntity<PhoenixTaskSubmissionDates> selectEntity, Criteria criteria, Session session) {
        addParameter(selectEntity, "deadline", DateTime.class, criteria);
        addParameter(selectEntity, "releaseDate", DateTime.class, criteria);

        SelectEntity<PhoenixLectureGroupTaskSheet> taskSheetKey = selectEntity.get("lectureGroupTaskSheet");
        if (taskSheetKey != null) {
            LectureGroupTaskSheet taskSheet = search(taskSheetKey, session, LectureGroupTaskSheetCriteriaFactory.getInstance());
            criteria.add(Restrictions.eq("lectureGroupTaskSheet", taskSheet));
        }

        SelectEntity<PhoenixTask> taskKey = selectEntity.get("task");
        if (taskKey != null) {
            Task task = search(taskKey, session, TaskCriteriaFactory.getInstance());
            criteria.add(Restrictions.eq("task", task));
        }
    }

}
