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

import java.util.Date;

import org.hibernate.Criteria;
import org.hibernate.Session;

import de.phoenix.database.entity.Text;
import de.phoenix.rs.entity.PhoenixText;
import de.phoenix.rs.key.SelectEntity;

/**
 * Construct criterias for {@link Text}
 */
public class TextCriteriaFactory extends CriteriaFactory<Text, PhoenixText> {

    private final static TextCriteriaFactory instance = new TextCriteriaFactory();

    private TextCriteriaFactory() {
        super(Text.class);
    }

    public static TextCriteriaFactory getInstance() {
        return instance;
    }

    @Override
    public void setAttributes(SelectEntity<PhoenixText> selectEntity, Criteria criteria, Session session) {
        addParameter(selectEntity, "name", String.class, "title", criteria);
        addParameter(selectEntity, "type", String.class, criteria);
        addParameter(selectEntity, "creationDate", Date.class, criteria);
    }
}
