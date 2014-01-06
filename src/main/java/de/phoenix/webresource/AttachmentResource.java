/*
 * Copyright (C) 2013 Project-Phoenix
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

package de.phoenix.webresource;

import java.io.IOException;
import java.util.Date;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;

import de.phoenix.database.entity.Attachment;
import de.phoenix.rs.entity.PhoenixAttachment;
import de.phoenix.rs.key.SelectEntity;
import de.phoenix.rs.key.UpdateEntity;
import de.phoenix.webresource.util.AbstractPhoenixResource;

@Path("/" + PhoenixAttachment.WEB_RESOURCE_ROOT)
public class AttachmentResource extends AbstractPhoenixResource<Attachment, PhoenixAttachment> {

    public AttachmentResource() {
        super(Attachment.class);
    }

    @Path("/" + PhoenixAttachment.WEB_RESOURCE_UPDATE)
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateAttachment(UpdateEntity<PhoenixAttachment> updatedAttachment) {
        return onUpdate(updatedAttachment);
    }

    @Path("/" + PhoenixAttachment.WEB_RESOURCE_DELETE)
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteAttachment(SelectEntity<PhoenixAttachment> selectAttachment) {
        return onDelete(selectAttachment);
    }

    @Override
    protected void setValues(Attachment entity, PhoenixAttachment phoenixEntity) {

        entity.setName(phoenixEntity.getName());
        entity.setType(phoenixEntity.getType());
        entity.setCreationDate(phoenixEntity.getCreationDate());
        try {
            entity.setFile(Hibernate.createBlob(phoenixEntity.getStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void setCriteria(SelectEntity<PhoenixAttachment> selectEntity, Criteria criteria) {
        addParameter(selectEntity, "creationDate", Date.class, "creationDate", criteria);
        addParameter(selectEntity, "name", String.class, "name", criteria);
        addParameter(selectEntity, "type", String.class, "type", criteria);
    }
}