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
import de.phoenix.rs.entity.PhoenixTask;
import de.phoenix.rs.key.SelectEntity;

public class TaskCriteriaFactory extends CriteriaFactory<Task, PhoenixTask> {

    private final static TaskCriteriaFactory instance = new TaskCriteriaFactory();

    private TaskCriteriaFactory() {
        super(Task.class);
    }

    public static TaskCriteriaFactory getInstance() {
        return instance;
    }

    @Override
    public void setAttributes(SelectEntity<PhoenixTask> selectEntity, Criteria criteria, Session session) {
        addParameter(selectEntity, "title", String.class, criteria);
        addParameter(selectEntity, "description", String.class, criteria);

        // Is instance of automatic task
        if (selectEntity.get("backend", String.class) != null) {
            addParameter(selectEntity, "backend", String.class, criteria);
            criteria.add(Restrictions.eq("backend", true));
        }
    }
}
