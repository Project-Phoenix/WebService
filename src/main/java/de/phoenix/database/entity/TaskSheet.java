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
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import de.phoenix.database.entity.util.Convertable;
import de.phoenix.database.entity.util.ConverterUtil;
import de.phoenix.rs.entity.PhoenixTaskSheet;

@Entity
@Table(name = "taskSheet")
@XmlRootElement
//@formatter:off
@NamedQueries({
    @NamedQuery(name = "TaskSheet.findAll", query = "SELECT t FROM TaskSheet t"),
    @NamedQuery(name = "TaskSheet.findById", query = "SELECT t FROM TaskSheet t WHERE t.id = :id"),
    @NamedQuery(name = "TaskSheet.findByCreationDate", query = "SELECT t FROM TaskSheet t WHERE t.creationDate = :creationDate")})
//@formatter:on
public class TaskSheet implements Serializable, Convertable<PhoenixTaskSheet> {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;

    @Column(name = "creationDate")
    @Type(type = "org.joda.time.contrib.hibernate.PersistentDateTime")
    private DateTime creationDate;

    //@formatter:off
    @JoinTable(name = "taskSheetTasks", joinColumns = {
            @JoinColumn(name = "taskSheet_id", referencedColumnName = "id")}, inverseJoinColumns = {
            @JoinColumn(name = "task_id", referencedColumnName = "id")})
    //@formatter:on
    @ManyToMany
    private List<Task> taskList;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "taskSheet1")
    private List<LectureGroupTaskSheet> lectureGroupTaskSheetList;

    public TaskSheet() {
    }

    public TaskSheet(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public DateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(DateTime creationDate) {
        this.creationDate = creationDate;
    }

    @XmlTransient
    public List<Task> getTasks() {
        return taskList;
    }

    public void setTasks(List<Task> tasks) {
        this.taskList = tasks;
    }

    @XmlTransient
    public List<LectureGroupTaskSheet> getLectureGroupTaskSheets() {
        return lectureGroupTaskSheetList;
    }

    public void setLectureGroupTaskSheets(List<LectureGroupTaskSheet> lectureGroupTaskSheets) {
        this.lectureGroupTaskSheetList = lectureGroupTaskSheets;
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
        if (!(object instanceof TaskSheet)) {
            return false;
        }
        TaskSheet other = (TaskSheet) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "de.phoenix.database.entityt.TaskSheet[ id=" + id + " ]";
    }

    @Override
    public PhoenixTaskSheet convert() {
        return new PhoenixTaskSheet(ConverterUtil.convert(taskList), getCreationDate());
    }

    @Override
    public void copyValues(PhoenixTaskSheet phoenixEntity) {
        throw new UnsupportedOperationException("Copy values in TaskSheet not yet implemented!");

    }

}
