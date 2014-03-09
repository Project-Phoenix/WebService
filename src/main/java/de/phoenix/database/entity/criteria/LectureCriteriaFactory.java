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

import de.phoenix.database.entity.Lecture;
import de.phoenix.rs.entity.PhoenixLecture;
import de.phoenix.rs.key.SelectEntity;

public class LectureCriteriaFactory extends CriteriaFactory<Lecture, PhoenixLecture> {

    private final static LectureCriteriaFactory instance = new LectureCriteriaFactory();

    private LectureCriteriaFactory() {
        super(Lecture.class);
    }

    public static LectureCriteriaFactory getInstance() {
        return instance;
    }

    @Override
    public void setAttributes(SelectEntity<PhoenixLecture> selectEntity, Criteria criteria, Session session) {
        addParameter(selectEntity, "title", String.class, criteria);
    }
}
