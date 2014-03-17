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

package de.phoenix.database.entity;

import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.Locale;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import de.phoenix.database.DatabaseManager;

@Entity
@Table(name = "debugLog")
public class DebugLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;

    @Lob
    @Column(name = "log")
    private String log;

    @Column(name = "date")
    @Type(type = "org.joda.time.contrib.hibernate.PersistentDateTime")
    private DateTime date;

    public DebugLog() {
        // TODO Auto-generated constructor stub
    }

    public DebugLog(String log, DateTime date) {
        this.log = log;
        this.date = date;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are
        // not set
        if (!(object instanceof DebugLog)) {
            return false;
        }
        DebugLog other = (DebugLog) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    private static final DateTimeFormatter format = DateTimeFormat.mediumDateTime().withLocale(Locale.GERMANY);

    @Override
    public String toString() {
        return "[" + date.toString(format) + "]: " + log;
    }

    public String getLog() {
        return log;
    }

    public DateTime getDate() {
        return date;
    }

    public void setDate(DateTime date) {
        this.date = date;
    }

    public void setLog(String log) {
        this.log = log;
    }

    public static void log(String message) {
        try {
            Session session = DatabaseManager.getSession();
            DebugLog log = new DebugLog(message, DateTime.now());
            Transaction trans = session.beginTransaction();
            session.save(log);
            trans.commit();
            session.close();
        } catch (Exception e) {
        }
    }
    
    public static String log(Throwable throwable) {
        StringWriter sw = new StringWriter();
        PrintWriter ps = new PrintWriter(sw);
        throwable.printStackTrace(ps);
        String message = sw.toString();
        ps.close();

        Session session = DatabaseManager.getSession();
        try {
            DebugLog log = new DebugLog(message, DateTime.now());
            Transaction trans = session.beginTransaction();
            session.save(log);
            trans.commit();
        } catch (Exception e) {
        } finally {
            if (session != null)
                session.close();
        }
        return message;
    }
}