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

public abstract class CriteriaFactory<T extends Convertable<E>, E extends PhoenixEntity> {

    private final Class<T> clazz;

    protected CriteriaFactory(Class<T> clazz) {
        this.clazz = clazz;
    }

    public Criteria extractCriteria(SelectEntity<E> selectEntity, Session session) {
        Criteria criteria = session.createCriteria(clazz);

        setAttributes(selectEntity, criteria, session);
        return criteria;
    }

    public abstract void setAttributes(SelectEntity<E> selectEntity, Criteria criteria, Session session);

    protected void addParameter(SelectEntity<E> entity, String entityAttributeName, Class<?> clazz, String criteriaAttributeName, Criteria criteria) {
        Object o = entity.get(entityAttributeName);
        if (o != null)
            criteria.add(Restrictions.eq(criteriaAttributeName, o));
    }

    protected void addParameter(SelectEntity<E> entity, String attributeName, Class<?> clazz, Criteria criteria) {
        this.addParameter(entity, attributeName, clazz, attributeName, criteria);
    }

    @SuppressWarnings("unchecked")
    protected <M extends Convertable<N>, N extends PhoenixEntity> M search(SelectEntity<N> bla, Session session, CriteriaFactory<M, N> factory) {
        Criteria criteria = factory.extractCriteria(bla, session);
        return (M) criteria.uniqueResult();
    }
}
