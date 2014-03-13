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

import de.phoenix.database.entity.Attachment;
import de.phoenix.rs.entity.PhoenixAttachment;
import de.phoenix.rs.key.SelectEntity;

/**
 * Construct criterias for {@link Attachment}
 */
public class AttachmentCriteriaFactory extends CriteriaFactory<Attachment, PhoenixAttachment> {

    private final static AttachmentCriteriaFactory instance = new AttachmentCriteriaFactory();

    private AttachmentCriteriaFactory() {
        super(Attachment.class);
    }

    public static AttachmentCriteriaFactory getInstance() {
        return instance;
    }

    @Override
    public void setAttributes(SelectEntity<PhoenixAttachment> selectEntity, Criteria criteria, Session session) {
        addParameter(selectEntity, "creationDate", criteria);
        addParameter(selectEntity, "name", criteria);
        addParameter(selectEntity, "type", criteria);
    }

}
