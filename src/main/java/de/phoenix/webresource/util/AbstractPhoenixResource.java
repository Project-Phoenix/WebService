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

package de.phoenix.webresource.util;

import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import de.phoenix.database.DatabaseManager;
import de.phoenix.database.entity.util.Convertable;
import de.phoenix.database.entity.util.ConverterArrayList;
import de.phoenix.rs.key.PhoenixEntity;
import de.phoenix.rs.key.SelectEntity;
import de.phoenix.rs.key.UpdateEntity;

public abstract class AbstractPhoenixResource<T extends Convertable<E>, E extends PhoenixEntity> {
    
    private final Class<T> clazz;
    
    public AbstractPhoenixResource(Class<T> clazz) {
        this.clazz = clazz;
    }

    protected Response onCreate(E phoenixEntity) {
        Session session = DatabaseManager.getSession();
        try {
            Transaction trans = session.beginTransaction();

            T entity = create(phoenixEntity, session);
            session.save(entity);

            trans.commit();
            return Response.ok().build();
        } finally {
            if (session != null)
                session.close();
        }
    }

    protected T create(E phoenixEntity, Session session) {
        throw new UnsupportedOperationException("Not supportet!");
    }

    protected Response onUpdate(UpdateEntity<E> updatedEntity) {
        Session session = DatabaseManager.getSession();
        try {

            List<T> entities = searchEntity(updatedEntity, session);
            Response response = checkOnlyOne(entities);
            if (response.getStatus() == 200) {
                T entity = entities.get(0);
                E phoenixEntity = updatedEntity.getNewObject();

                setValues(entity, phoenixEntity);

                session.update(entity);
            }

            return response;

        } finally {
            if (session != null)
                session.close();
        }
    }

    protected abstract void setValues(T entity, E phoenixEntity);

    protected Response onDelete(SelectEntity<E> selectEntity) {
        Session session = DatabaseManager.getSession();
        try {
            List<T> entities = searchEntity(selectEntity, session);
            Response response = checkOnlyOne(entities);
            if (response.getStatus() == 200) {
                T entity = entities.get(0);
                session.delete(entity);
            }

            return response;
        } finally {
            if (session != null)
                session.close();
        }
    }

    protected List<E> onGet(SelectEntity<E> selectEntity) {
        Session session = DatabaseManager.getSession();
        try {

            List<T> entities = searchEntity(selectEntity, session);

            List<E> result = new ConverterArrayList<E>(entities);

            return result;
        } finally {
            if (session != null)
                session.close();
        }
    }

    @SuppressWarnings("unchecked")
    protected List<T> searchEntity(SelectEntity<E> selectEntity, Session session) {

        Criteria criteria = session.createCriteria(clazz);

        setCriteria(selectEntity, criteria);

        return criteria.list();
    }

    protected abstract void setCriteria(SelectEntity<E> selectEntity, Criteria criteria);

    protected void addParameter(SelectEntity<E> entity, String entityName, Class<?> clazz, String criteriaName, Criteria criteria) {
        Object o = entity.get(entityName, clazz);
        if (o != null)
            criteria.add(Restrictions.eq(criteriaName, o));
    }

    private Response checkOnlyOne(List<T> entities) {

        if (entities.isEmpty()) {
            return Response.status(Status.BAD_REQUEST).entity("No entity").build();
        } else if (entities.size() > 1) {
            return Response.status(Status.NOT_MODIFIED).entity("Multiple entities").build();
        } else {
            return Response.ok().build();
        }
    }

}