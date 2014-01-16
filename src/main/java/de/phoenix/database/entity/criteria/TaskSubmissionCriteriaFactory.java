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
import org.joda.time.DateTime;

import de.phoenix.database.entity.TaskSubmission;
import de.phoenix.rs.entity.PhoenixSubmission;
import de.phoenix.rs.key.SelectEntity;

public class TaskSubmissionCriteriaFactory extends CriteriaFactory<TaskSubmission, PhoenixSubmission> {

    public TaskSubmissionCriteriaFactory() {
        super(TaskSubmission.class);
    }

    @Override
    public void setAttributes(SelectEntity<PhoenixSubmission> selectEntity, Criteria criteria) {
        addParameter(selectEntity, "date", DateTime.class, criteria);
        addParameter(selectEntity, "status", int.class, criteria);
        addParameter(selectEntity, "statusText", String.class, criteria);
    }
}
