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
import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import de.phoenix.rs.entity.PhoenixText;

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
public class Text implements Serializable {
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
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;

    @Column(name = "name")
    private String name;

    @Column(name = "type")
    private String type;

    @ManyToMany(mappedBy = "textList")
    private List<TaskSubmission> taskSubmissionList;

    @ManyToMany(mappedBy = "textList")
    private List<Task> taskList;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "test")
    private List<Test> testList;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "task")
    private List<Test> testList1;

    public Text() {
    }

    public Text(Integer id) {
        this.id = id;
    }

    public Text(String content, Date creationDate, String name, String type) {
        this.content = content;
        this.creationDate = creationDate;
        this.name = name;
        this.type = type;
    }

    public Text(PhoenixText text) {
        this.content = text.getText();
        this.creationDate = text.getCreationDate();
        this.name = text.getName();
        this.type = text.getType();
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
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
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
    public List<Test> getTests() {
        return testList;
    }

    public void setTests(List<Test> tests) {
        this.testList = tests;
    }

    @XmlTransient
    public List<Test> getTestList1() {
        return testList1;
    }

    public void setTestList1(List<Test> testList1) {
        this.testList1 = testList1;
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

}
