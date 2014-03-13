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

import de.phoenix.database.DatabaseManager;
import de.phoenix.database.entity.criteria.CriteriaFactory;
import de.phoenix.database.entity.util.Convertable;
import de.phoenix.database.entity.util.ConverterUtil;
import de.phoenix.rs.key.PhoenixEntity;
import de.phoenix.rs.key.SelectEntity;
import de.phoenix.rs.key.UpdateEntity;

/**
 * Abstract class to generalize the get and delete process of entities via
 * {@link SelectEntity} and the update process with {@link UpdateEntity}
 * 
 * @param <T>
 *            The database side of the entity
 * @param <E>
 *            The API implementation of the entity
 */
public abstract class AbstractPhoenixResource<T extends Convertable<E>, E extends PhoenixEntity> {

    private CriteriaFactory<T, E> criteriaFactory;

    /**
     * Add a criteria factory for generalizing the get, update and delete
     * operations
     * 
     * @param criteriaFactory
     *            The factory creating criterias for the entity the resource
     *            manages.
     */
    public AbstractPhoenixResource(CriteriaFactory<T, E> criteriaFactory) {
        this.criteriaFactory = criteriaFactory;
    }

    // TODO: Check if there are duplicate entry errors
    /**
     * Invoke the method, when a single entity has to be simply created
     * 
     * @param phoenixEntity
     *            The API implementation of the entity
     * @param creator
     *            The creator, which transfers the phoenixEntity to a
     *            persistable entity
     * @return 200, when everything was ok
     */
    protected Response onCreate(E phoenixEntity, EntityCreator<T, E> creator) {
        Session session = DatabaseManager.getSession();
        try {
            Transaction trans = session.beginTransaction();

            T entity = creator.create(phoenixEntity, session);
            session.save(entity);

            trans.commit();
            return Response.ok().build();
        } finally {
            if (session != null)
                session.close();
        }
    }

    /**
     * Simple interface to encapsulate the constructor
     * 
     * @param <T>
     * @param <E>
     */
    protected interface EntityCreator<T extends Convertable<E>, E> {
        public T create(E phoenixEntity, Session session);
    }

    /**
     * Invoke the method, when a entity has to be updated
     * 
     * @param updatedEntity
     *            The update entity containing old entity (to find, what to
     *            update) and the new one to assign the values
     * @return 304, when the update entity matches more than one entity to
     *         update <br>
     *         404, when the update entity does not match any entity to update <br>
     *         Otherwise 200
     */
    protected Response onUpdate(UpdateEntity<E> updatedEntity) {
        Session session = DatabaseManager.getSession();
        try {

            List<T> entities = searchEntity(updatedEntity, session);
            Response response = checkOnlyOne(entities);
            if (response.getStatus() == 200) {
                T entity = entities.get(0);
                E phoenixEntity = updatedEntity.getNewObject();

                Transaction trans = session.beginTransaction();
                entity.copyValues(phoenixEntity);

                session.update(entity);
                trans.commit();
            }

            return response;

        } finally {
            if (session != null)
                session.close();
        }
    }

    /**
     * Invoke the method, when a entity has to be deleted
     * 
     * @param selectEntity
     *            Describes, what entity will be deleted
     * @return 304, when the select entity matches more than one entity to
     *         update <br>
     *         404, when the update entity does not match any entity to update <br>
     *         Otherwise 200
     */
    protected Response onDelete(SelectEntity<E> selectEntity) {
        Session session = DatabaseManager.getSession();
        try {
            List<T> entities = searchEntity(selectEntity, session);
            Response response = checkOnlyOne(entities);
            if (response.getStatus() == 200) {
                T entity = entities.get(0);

                Transaction trans = session.beginTransaction();
                session.delete(entity);
                trans.commit();
            }

            return response;
        } finally {
            if (session != null)
                session.close();
        }
    }

    /**
     * Searches for a certain entity in the database and retrieve a list
     * containing all entities matches by the SelectEntity
     * 
     * @param selectEntity
     *            Describe what entities to match
     * @return 404, when no entity was found <br>
     *         Otherwise a list containg all entities and status code 200
     */
    protected Response onGet(SelectEntity<E> selectEntity) {
        Session session = DatabaseManager.getSession();
        try {

            List<T> entities = searchEntity(selectEntity, session);
            if (entities.isEmpty()) {
                return Response.status(Status.NOT_FOUND).build();
            }

            List<E> result = ConverterUtil.convert(entities);

            return Response.ok(result).build();
        } finally {
            if (session != null)
                session.close();
        }
    }

    @SuppressWarnings("unchecked")
    protected List<T> searchEntity(SelectEntity<E> selectEntity, Session session) {

        Criteria criteria = criteriaFactory.extractCriteria(selectEntity, session);
        return criteria.list();
    }

    protected Response checkOnlyOne(List<T> entities) {

        if (entities.isEmpty()) {
            return Response.status(Status.NOT_FOUND).entity("No entity").build();
        } else if (entities.size() > 1) {
            return Response.status(Status.NOT_MODIFIED).entity("Multiple entities").build();
        } else {
            return Response.ok().build();
        }
    }
}