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

package de.phoenix.database.entity;

import java.io.IOException;
import java.io.Serializable;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.Hibernate;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import de.phoenix.database.entity.util.Convertable;
import de.phoenix.rs.entity.PhoenixAttachment;
import de.phoenix.util.hash.Hasher;
import de.phoenix.util.hash.SHA1Hasher;

/**
 * Class defining database structure for binary attachments. PhoenixEntity is
 * {@link PhoenixAttachment}
 */
@Entity
@Table(name = "attachment")
public class Attachment implements Serializable, Convertable<PhoenixAttachment> {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;

    @Lob
    @Column(name = "file")
    private Blob file;

    @Column(name = "creationDate")
    @Type(type = "org.joda.time.contrib.hibernate.PersistentDateTime")
    private DateTime creationDate;

    @Column(name = "name")
    private String name;

    @Column(name = "type")
    private String type;

    @Column(name = "sha1")
    private String sha1;

    @ManyToMany(mappedBy = "attachmentList")
    private List<TaskSubmission> taskSubmissionList;

    @ManyToMany(mappedBy = "attachmentList")
    private List<Task> taskList;

    public Attachment() {
    }

    public Attachment(Integer id) {
        this.id = id;
    }

    public Attachment(Blob file, DateTime creationDate, String name, String type) {
        this.setFile(file);
        this.creationDate = creationDate;
        this.name = name;
        this.type = type;
    }

    public Attachment(Blob file, DateTime creationDate, String name, String type, String sha1) {
        this.file = file;
        this.creationDate = creationDate;
        this.name = name;
        this.type = type;
        this.sha1 = sha1;
    }

    @SuppressWarnings("deprecation")
    public Attachment(PhoenixAttachment attachment) throws IOException {

        this(Hibernate.createBlob(attachment.getStream()), new DateTime(), attachment.getName(), attachment.getType());
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Blob getFile() {
        return file;
    }

    public void setFile(Blob file) {
        this.file = file;
        this.sha1 = calculateSHA1(this.file);
    }

    public DateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(DateTime creationDate) {
        this.creationDate = creationDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSha1() {
        return sha1;
    }

    public void setSha1(String sha1) {
        this.sha1 = sha1;
    }

    @XmlTransient
    public List<TaskSubmission> getTaskSubmissions() {
        return taskSubmissionList;
    }

    public void setTaskSubmissions(List<TaskSubmission> taskSubmissions) {
        this.taskSubmissionList = taskSubmissions;
    }

    @XmlTransient
    public List<Task> getTasks() {
        return taskList;
    }

    public void setTasks(List<Task> tasks) {
        this.taskList = tasks;
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
        if (!(object instanceof Attachment)) {
            return false;
        }
        Attachment other = (Attachment) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "de.phoenix.database.entityt.Attachment[ id=" + id + " ]";
    }

    private String calculateSHA1(Blob file) {

        try {
            Hasher hasher = new SHA1Hasher();
            return hasher.generate(file.getBinaryStream());
        } catch (SQLException e) {
            return null;
        }
    }

    @Override
    public PhoenixAttachment convert() {

        try {
            return new PhoenixAttachment(this.getFile().getBytes(1, (int) this.getFile().length()), getCreationDate(), getName(), getType());
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public void copyValues(PhoenixAttachment phoenixEntity) {
        this.setName(phoenixEntity.getName());
        this.setType(phoenixEntity.getType());
        this.setCreationDate(phoenixEntity.getCreationDate());
        try {
            this.setFile(Hibernate.createBlob(phoenixEntity.getStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
