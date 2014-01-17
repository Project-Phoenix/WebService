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

import java.util.ArrayList;
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
import de.phoenix.rs.key.ConnectWithEntity;
import de.phoenix.rs.key.PhoenixEntity;
import de.phoenix.rs.key.SelectEntity;
import de.phoenix.rs.key.UpdateEntity;

public abstract class AbstractPhoenixResource<T extends Convertable<E>, E extends PhoenixEntity> {

    private CriteriaFactory<T, E> criteriaFactory;

    public AbstractPhoenixResource(CriteriaFactory<T, E> criteriaFactory) {
        this.criteriaFactory = criteriaFactory;
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

    protected Response onGet(SelectEntity<E> selectEntity) {
        Session session = DatabaseManager.getSession();
        try {

            List<T> entities = searchEntity(selectEntity, session);
            if (entities.isEmpty()) {
                Response.noContent().build();
            }

            List<E> result = ConverterUtil.convert(entities);

            return Response.ok(result).build();
        } finally {
            if (session != null)
                session.close();
        }
    }

    @SuppressWarnings("unchecked")
    protected <N extends Convertable<M>, M extends PhoenixEntity> Response onConnect(ConnectWithEntity<E, M> connectEntity, CriteriaFactory<N, M> factory, EntityConnector<T, N> connector) {

        Session session = DatabaseManager.getSession();
        try {

            List<T> entities = searchEntity(connectEntity, session);
            Response response = checkOnlyOne(entities);

            if (response.getStatus() == 200) {
                T entity = entities.get(0);

                List<SelectEntity<M>> connectEntities = connectEntity.getToConnectEntities();
                List<N> foundConnectEntites = new ArrayList<N>(connectEntities.size());

                for (SelectEntity<M> selectEntity : connectEntities) {
                    Criteria criteria = factory.extractCriteria(selectEntity, session);
                    N foundConnectEntity = (N) criteria.uniqueResult();
                    if (foundConnectEntity == null) {
                        return Response.status(Status.BAD_REQUEST).entity("Unknown entity!").build();
                    }
                    foundConnectEntites.add(foundConnectEntity);
                }
                connector.connect(entity, foundConnectEntites);
                Transaction trans = session.beginTransaction();
                session.update(entity);
                trans.commit();
            }

            return response;
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
            return Response.status(Status.BAD_REQUEST).entity("No entity").build();
        } else if (entities.size() > 1) {
            return Response.status(Status.NOT_MODIFIED).entity("Multiple entities").build();
        } else {
            return Response.ok().build();
        }
    }

    public interface EntityConnector<T, N extends Convertable<? extends PhoenixEntity>> {
        public void connect(T entity, List<N> entities);
    }

}