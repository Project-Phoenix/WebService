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

import de.phoenix.database.entity.User;
import de.phoenix.database.entity.Userlevel;
import de.phoenix.rs.key.SelectEntity;
import de.phoenix.security.user.PhoenixUser;
import de.phoenix.security.user.PhoenixUserLevel;

public class UserCriteriaFactory extends CriteriaFactory<User, PhoenixUser> {

    private final static UserCriteriaFactory instance = new UserCriteriaFactory();

    public static UserCriteriaFactory getInstance() {
        return instance;
    }

    private UserCriteriaFactory() {
        super(User.class);
    }

    @Override
    public void setAttributes(SelectEntity<PhoenixUser> selectEntity, Criteria criteria, Session session) {
        addParameter(selectEntity, "surname", criteria);
        addParameter(selectEntity, "name", criteria);
        addParameter(selectEntity, "username", criteria);
        addParameter(selectEntity, "mail", criteria);

        SelectEntity<PhoenixUserLevel> levelKey = selectEntity.get("userLevel");
        if (levelKey != null) {
            Userlevel level = search(levelKey, session, UserLevelCriteriaFactory.getInstance());
            criteria.add(Restrictions.eq("userlevelId", level));
        }

    }

}
