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

import de.phoenix.database.entity.Userlevel;
import de.phoenix.rs.key.SelectEntity;
import de.phoenix.security.user.permission.PhoenixUserLevel;

public class UserLevelCriteriaFactory extends CriteriaFactory<Userlevel, PhoenixUserLevel> {

    private final static UserLevelCriteriaFactory instance = new UserLevelCriteriaFactory();

    public static UserLevelCriteriaFactory getInstance() {
        return instance;
    }

    private UserLevelCriteriaFactory() {
        super(Userlevel.class);
    }

    @Override
    public void setAttributes(SelectEntity<PhoenixUserLevel> selectEntity, Criteria criteria, Session session) {
        addParameter(selectEntity, "name", criteria);
    }

}
