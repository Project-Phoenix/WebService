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

import de.phoenix.database.entity.util.Convertable;
import de.phoenix.rs.key.PhoenixEntity;
import de.phoenix.rs.key.SelectEntity;

/**
 * Abstract factory to create criterias for a certain entity. The criterias are
 * used to search for an instance of the entity type
 * 
 * @param <T>
 * @param <E>
 */
public abstract class CriteriaFactory<T extends Convertable<E>, E extends PhoenixEntity> {

    private final Class<T> clazz;

    /**
     * Used to identify at runtime the class of the type
     * 
     * @param clazz
     *            The class of the entity type
     */
    protected CriteriaFactory(Class<T> clazz) {
        this.clazz = clazz;
    }

    /**
     * Create a criteria from a select entity using its set keys
     * 
     * @param selectEntity
     *            SelectEntity containg the keys for the search
     * @param session
     *            The current session
     * @return A created criteria matching the select entity
     */
    public Criteria extractCriteria(SelectEntity<E> selectEntity, Session session) {
        Criteria criteria = session.createCriteria(clazz);

        setAttributes(selectEntity, criteria, session);
        return criteria;
    }

    /**
     * Set the different attributes of criteria for this entity type.
     * 
     * @param selectEntity
     *            The select entity containg keys
     * @param criteria
     *            The criteria to fill
     * @param session
     *            The current session
     */
    public abstract void setAttributes(SelectEntity<E> selectEntity, Criteria criteria, Session session);

    /**
     * Helper method to set a criteria attribute
     * 
     * @param entity
     *            The select entity
     * @param entityAttributeName
     *            The name of the attribute in the select entity
     * @param criteriaAttributeName
     *            The name of the attribute in the criteria
     * @param criteria
     *            The criteria
     */
    protected void addParameter(SelectEntity<E> entity, String entityAttributeName, String criteriaAttributeName, Criteria criteria) {
        Object o = entity.get(entityAttributeName);
        if (o != null)
            criteria.add(Restrictions.eq(criteriaAttributeName, o));
    }

    /**
     * Same as {@link #addParameter(SelectEntity, String, String, Criteria)} ,
     * where attributename == criteriaAttributeName
     * 
     * @param entity
     *            The select entity
     * @param attributeName
     *            The name of the attribute in the select entity
     * @param criteria
     *            The criteria
     */
    protected void addParameter(SelectEntity<E> entity, String attributeName, Criteria criteria) {
        this.addParameter(entity, attributeName, attributeName, criteria);
    }

    /**
     * Searches for another PhoenixEntity for the criteria
     * 
     * @param selectEntity
     *            The selectEntity
     * @param session
     *            The current session
     * @param factory
     *            The factory for the other PhoenixEntity
     * @return The result of the search
     */
    @SuppressWarnings("unchecked")
    protected <M extends Convertable<N>, N extends PhoenixEntity> M search(SelectEntity<N> selectEntity, Session session, CriteriaFactory<M, N> factory) {
        Criteria criteria = factory.extractCriteria(selectEntity, session);
        return (M) criteria.uniqueResult();
    }
}
