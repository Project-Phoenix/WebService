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

import de.phoenix.database.entity.TaskSheet;
import de.phoenix.rs.entity.PhoenixTaskSheet;
import de.phoenix.rs.key.SelectEntity;

/**
 * Construct criterias for {@link TaskSheet}
 */
public class TaskSheetCriteriaFactory extends CriteriaFactory<TaskSheet, PhoenixTaskSheet> {

    private final static TaskSheetCriteriaFactory instance = new TaskSheetCriteriaFactory();

    private TaskSheetCriteriaFactory() {
        super(TaskSheet.class);
    }

    public static TaskSheetCriteriaFactory getInstance() {
        return instance;
    }

    @Override
    public void setAttributes(SelectEntity<PhoenixTaskSheet> selectEntity, Criteria criteria, Session session) {
        addParameter(selectEntity, "title", String.class, criteria);
    }

}
