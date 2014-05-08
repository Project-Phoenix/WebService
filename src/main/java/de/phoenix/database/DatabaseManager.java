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

package de.phoenix.database;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Restrictions;

import de.phoenix.database.entity.Userlevel;
import de.phoenix.security.user.permission.PermissionTree;

/**
 * Wrapper class to the database access via Hibernate
 */
public class DatabaseManager {

    /* Singletone Start */
    private static final DatabaseManager INSTANCE = new DatabaseManager();

    private DatabaseManager() {
        Configuration config = new Configuration().configure("hibernate.cfg.xml");
        this.sessionFactory = config.buildSessionFactory();
        this.createDefaultUser();
    }

    public final static DatabaseManager getInstance() {
        return INSTANCE;
    }

    /**
     * Shortcut for DatabaseManager.getInstance().openSession().<br>
     * See {@link #openSession()}
     * 
     * @return A session for the database
     */
    public static Session getSession() {
        if (getInstance() == null)
            return null;
        return getInstance().openSession();
    }

    /* Singletone End */

    private final SessionFactory sessionFactory;

    /**
     * Close the database manager closing all sessions form the session factory
     */
    public void close() {
        this.sessionFactory.close();
    }

    /**
     * Open a new session
     * 
     * @return New session
     */
    public Session openSession() {
        return sessionFactory.openSession();
    }

    private void createDefaultUser() {
        Session session = openSession();
        try {
            Transaction trans = session.beginTransaction();
            System.out.println("Add default user levels...");
            if (!existsUserLevel("admin", session)) {

                Userlevel adminLevel = new Userlevel();
                adminLevel.setName("admin");
                PermissionTree tree = new PermissionTree();
                tree.addNode("*");
                adminLevel.setPermissionList(tree);

                session.save(adminLevel);
            }
            if (!existsUserLevel("default", session)) {

                Userlevel defaultLevel = new Userlevel();
                defaultLevel.setName("default");
                PermissionTree tree = new PermissionTree();
                defaultLevel.setPermissionList(tree);
                session.save(defaultLevel);
            }

            trans.commit();
        } finally {
            if (session != null)
                session.close();
        }
    }

    private boolean existsUserLevel(String name, Session session) {
        return session.createCriteria(Userlevel.class).add(Restrictions.eq("name", name)).uniqueResult() != null;
    }

}
