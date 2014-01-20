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

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

@Entity
@Table(name = "taskSubmissionDates")
@XmlRootElement
//@formatter:off
@NamedQueries({
    @NamedQuery(name = "TaskSubmissionDates.findAll", query = "SELECT t FROM TaskSubmissionDates t"),
    @NamedQuery(name = "TaskSubmissionDates.findById", query = "SELECT t FROM TaskSubmissionDates t WHERE t.id = :id"),
    @NamedQuery(name = "TaskSubmissionDates.findByDeadline", query = "SELECT t FROM TaskSubmissionDates t WHERE t.deadline = :deadline"),
    @NamedQuery(name = "TaskSubmissionDates.findByReleasedate", query = "SELECT t FROM TaskSubmissionDates t WHERE t.releasedate = :releasedate")})
//@formatter:on
public class TaskSubmissionDates implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;

    @Column(name = "deadline")
    @Type(type = "org.joda.time.contrib.hibernate.PersistentDateTime")
    private DateTime deadline;

    @Column(name = "releasedate")
    @Type(type = "org.joda.time.contrib.hibernate.PersistentDateTime")
    private DateTime releasedate;

    @JoinColumn(name = "task", referencedColumnName = "id")
    @ManyToOne
    private Task task;

    @JoinColumn(name = "lectureGroupTaskSheet", referencedColumnName = "id")
    @ManyToOne
    private LectureGroupTaskSheet lectureGroupTaskSheet;

    public TaskSubmissionDates() {
    }

    public TaskSubmissionDates(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public DateTime getDeadline() {
        return deadline;
    }

    public void setDeadline(DateTime deadline) {
        this.deadline = deadline;
    }

    public DateTime getReleasedate() {
        return releasedate;
    }

    public void setReleasedate(DateTime releasedate) {
        this.releasedate = releasedate;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public LectureGroupTaskSheet getLectureGroupTaskSheet() {
        return lectureGroupTaskSheet;
    }

    public void setLectureGroupTaskSheet(LectureGroupTaskSheet lectureGroupTaskSheet) {
        this.lectureGroupTaskSheet = lectureGroupTaskSheet;
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
        if (!(object instanceof TaskSubmissionDates)) {
            return false;
        }
        TaskSubmissionDates other = (TaskSubmissionDates) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "de.phoenix.database.entity.TaskSubmissionDates[ id=" + id + " ]";
    }

}
