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

import de.phoenix.database.entity.Attachment;
import de.phoenix.rs.entity.PhoenixAttachment;
import de.phoenix.rs.key.SelectEntity;

public class AttachmentCriteriaFactory extends CriteriaFactory<Attachment, PhoenixAttachment> {

    private final static AttachmentCriteriaFactory instance = new AttachmentCriteriaFactory();

    private AttachmentCriteriaFactory() {
        super(Attachment.class);
    }

    public static AttachmentCriteriaFactory getInstance() {
        return instance;
    }

    @Override
    public void setAttributes(SelectEntity<PhoenixAttachment> selectEntity, Criteria criteria) {
        addParameter(selectEntity, "creationDate", Date.class, criteria);
        addParameter(selectEntity, "name", String.class, criteria);
        addParameter(selectEntity, "type", String.class, criteria);
    }

}
