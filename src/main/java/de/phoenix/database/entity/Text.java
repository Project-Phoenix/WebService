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

import java.io.Serializable;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import de.phoenix.database.entity.util.Convertable;
import de.phoenix.rs.entity.PhoenixText;
import de.phoenix.util.hash.SHA1Hasher;

@Entity
@Table(name = "text")
@XmlRootElement
//@formatter:off
@NamedQueries({
    @NamedQuery(name = "Text.findAll", query = "SELECT t FROM Text t"),
    @NamedQuery(name = "Text.findById", query = "SELECT t FROM Text t WHERE t.id = :id"),
    @NamedQuery(name = "Text.findByCreationDate", query = "SELECT t FROM Text t WHERE t.creationDate = :creationDate"),
    @NamedQuery(name = "Text.findByName", query = "SELECT t FROM Text t WHERE t.name = :name"),
    @NamedQuery(name = "Text.findByType", query = "SELECT t FROM Text t WHERE t.type = :type")})
//@formatter:on
public class Text implements Serializable, Convertable<PhoenixText> {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;

    @Lob
    @Column(name = "content")
    private String content;

    @Column(name = "creationDate")
    @Type(type = "org.joda.time.contrib.hibernate.PersistentDateTime")
    private DateTime creationDate;

    @Column(name = "name")
    private String name;

    @Column(name = "type")
    private String type;

    @Column(name = "sha1")
    private String sha1;

    @ManyToMany(mappedBy = "textList")
    private List<TaskSubmission> taskSubmissionList;

    // The text pattern for a task
    @ManyToMany(mappedBy = "textList")
    private List<Task> taskList;

    // The possible test files for a task
    @ManyToMany(mappedBy = "testList")
    private List<Task> taskList1;

    public Text() {
    }

    public Text(Integer id) {
        this.id = id;
    }

    public Text(String content, DateTime creationDate, String name, String type) {
        this.creationDate = creationDate;
        this.name = name;
        this.type = type;
        this.setContent(content);
    }

    public Text(String content, DateTime creationDate, String name, String type, String sha1) {
        this.content = content;
        this.creationDate = creationDate;
        this.name = name;
        this.type = type;
        this.sha1 = sha1;
    }

    public Text(PhoenixText text) {
        this(text.getText(), new DateTime(), text.getName(), text.getType());
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
        this.sha1 = new SHA1Hasher().generate(content);
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

    @XmlTransient
    public List<Task> getTaskTests() {
        return taskList1;
    }

    public void setTaskTests(List<Task> taskTests) {
        this.taskList1 = taskTests;
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
        if (!(object instanceof Text)) {
            return false;
        }
        Text other = (Text) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "de.phoenix.database.entityt.Text[ id=" + id + " ]";
    }

    @Override
    public PhoenixText convert() {
        return new PhoenixText(this.getContent(), this.getCreationDate(), this.getName(), this.getType());
    }

    @Override
    public void copyValues(PhoenixText phoenixEntity) {
        this.setName(phoenixEntity.getName());
        this.setType(phoenixEntity.getType());
        this.setContent(phoenixEntity.getText());
    }
}
