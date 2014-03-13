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

import de.phoenix.database.entity.Lecture;
import de.phoenix.database.entity.LectureGroup;
import de.phoenix.rs.entity.PhoenixLecture;
import de.phoenix.rs.entity.PhoenixLectureGroup;
import de.phoenix.rs.key.SelectEntity;

/**
 * Construct criterias for {@link LectureGroup}
 */
public class LectureGroupCriteriaFactory extends CriteriaFactory<LectureGroup, PhoenixLectureGroup> {

    private final static LectureGroupCriteriaFactory instance = new LectureGroupCriteriaFactory();

    private LectureGroupCriteriaFactory() {
        super(LectureGroup.class);
    }

    public static LectureGroupCriteriaFactory getInstance() {
        return instance;
    }

    @Override
    public void setAttributes(SelectEntity<PhoenixLectureGroup> selectEntity, Criteria criteria, Session session) {
        addParameter(selectEntity, "name", String.class, criteria);
        addParameter(selectEntity, "maxMember", int.class, criteria);
        addParameter(selectEntity, "submissionDeadLineTime", String.class, criteria);
        addParameter(selectEntity, "submissionDeadlineWeekyday", String.class, criteria);
        addParameter(selectEntity, "name", String.class, criteria);

        SelectEntity<PhoenixLecture> lectureKey = selectEntity.get("lecture");
        if (lectureKey != null) {
            Lecture lecture = search(lectureKey, session, LectureCriteriaFactory.getInstance());
            criteria.add(Restrictions.eq("lecture", lecture));
        }
    }
}
