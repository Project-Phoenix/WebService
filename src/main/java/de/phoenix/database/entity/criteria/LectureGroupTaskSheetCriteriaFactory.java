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

import de.phoenix.database.entity.LectureGroup;
import de.phoenix.database.entity.LectureGroupTaskSheet;
import de.phoenix.database.entity.TaskSheet;
import de.phoenix.rs.entity.PhoenixLectureGroup;
import de.phoenix.rs.entity.PhoenixLectureGroupTaskSheet;
import de.phoenix.rs.entity.PhoenixTaskSheet;
import de.phoenix.rs.key.SelectEntity;

/**
 * Construct Criterias for {@link LectureGroupTaskSheetCriteriaFactory}
 */
public class LectureGroupTaskSheetCriteriaFactory extends CriteriaFactory<LectureGroupTaskSheet, PhoenixLectureGroupTaskSheet> {

    private final static LectureGroupTaskSheetCriteriaFactory instance = new LectureGroupTaskSheetCriteriaFactory();

    private LectureGroupTaskSheetCriteriaFactory() {
        super(LectureGroupTaskSheet.class);
    }

    public static LectureGroupTaskSheetCriteriaFactory getInstance() {
        return instance;
    }

    @Override
    public void setAttributes(SelectEntity<PhoenixLectureGroupTaskSheet> selectEntity, Criteria criteria, Session session) {
        addParameter(selectEntity, "defaultDeadline", criteria);
        addParameter(selectEntity, "defaultReleaseDate", criteria);

        SelectEntity<PhoenixTaskSheet> taskSheetKey = selectEntity.get("taskSheet");
        if (taskSheetKey != null) {
            TaskSheet taskSheet = search(taskSheetKey, session, TaskSheetCriteriaFactory.getInstance());
            criteria.add(Restrictions.eq("taskSheet", taskSheet));

        }

        SelectEntity<PhoenixLectureGroup> lectureGroupKey = selectEntity.get("lectureGroup");
        if (lectureGroupKey != null) {
            LectureGroup lectureGroup = search(lectureGroupKey, session, LectureGroupCriteriaFactory.getInstance());
            criteria.add(Restrictions.eq("lectureGroup", lectureGroup));
        }
    }

}
