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
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "lectureGroupTaskSheetDates")
@XmlRootElement
//@formatter:off
@NamedQueries({
    @NamedQuery(name = "LectureGroupTaskSheetDates.findAll", query = "SELECT l FROM LectureGroupTaskSheetDates l"),
    @NamedQuery(name = "LectureGroupTaskSheetDates.findByGroupTaskSheetgroup", query = "SELECT l FROM LectureGroupTaskSheetDates l WHERE l.lectureGroupTaskSheetDatesPK.groupTaskSheetgroup = :groupTaskSheetgroup"),
    @NamedQuery(name = "LectureGroupTaskSheetDates.findByGroupTaskSheettaskSheet", query = "SELECT l FROM LectureGroupTaskSheetDates l WHERE l.lectureGroupTaskSheetDatesPK.groupTaskSheettaskSheet = :groupTaskSheettaskSheet"),
    @NamedQuery(name = "LectureGroupTaskSheetDates.findByTaskId", query = "SELECT l FROM LectureGroupTaskSheetDates l WHERE l.lectureGroupTaskSheetDatesPK.taskId = :taskId"),
    @NamedQuery(name = "LectureGroupTaskSheetDates.findByDeadline", query = "SELECT l FROM LectureGroupTaskSheetDates l WHERE l.deadline = :deadline"),
    @NamedQuery(name = "LectureGroupTaskSheetDates.findByReleaseDate", query = "SELECT l FROM LectureGroupTaskSheetDates l WHERE l.releaseDate = :releaseDate")})
//@formatter:on
public class LectureGroupTaskSheetDates implements Serializable {

    private static final long serialVersionUID = 1L;

    @EmbeddedId
    protected LectureGroupTaskSheetDatesPK lectureGroupTaskSheetDatesPK;

    @Column(name = "deadline")
    @Temporal(TemporalType.TIMESTAMP)
    private Date deadline;

    @Column(name = "releaseDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date releaseDate;

    @JoinColumn(name = "task_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Task task;

    //@formatter:off
    @JoinColumns({
        @JoinColumn(name = "groupTaskSheet_group", referencedColumnName = "group", insertable = false, updatable = false),
        @JoinColumn(name = "groupTaskSheet_taskSheet", referencedColumnName = "taskSheet", insertable = false, updatable = false)})
    //formatter:on
    @ManyToOne(optional = false)
    private LectureGroupTaskSheet lectureGroupTaskSheet;

    public LectureGroupTaskSheetDates() {
    }

    public LectureGroupTaskSheetDates(LectureGroupTaskSheetDatesPK lectureGroupTaskSheetDatesPK) {
        this.lectureGroupTaskSheetDatesPK = lectureGroupTaskSheetDatesPK;
    }

    public LectureGroupTaskSheetDates(int groupTaskSheetgroup, int groupTaskSheettaskSheet, int taskId) {
        this.lectureGroupTaskSheetDatesPK = new LectureGroupTaskSheetDatesPK(groupTaskSheetgroup, groupTaskSheettaskSheet, taskId);
    }

    public LectureGroupTaskSheetDatesPK getLectureGroupTaskSheetDatesPK() {
        return lectureGroupTaskSheetDatesPK;
    }

    public void setLectureGroupTaskSheetDatesPK(LectureGroupTaskSheetDatesPK lectureGroupTaskSheetDatesPK) {
        this.lectureGroupTaskSheetDatesPK = lectureGroupTaskSheetDatesPK;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
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
        hash += (lectureGroupTaskSheetDatesPK != null ? lectureGroupTaskSheetDatesPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are
        // not set
        if (!(object instanceof LectureGroupTaskSheetDates)) {
            return false;
        }
        LectureGroupTaskSheetDates other = (LectureGroupTaskSheetDates) object;
        if ((this.lectureGroupTaskSheetDatesPK == null && other.lectureGroupTaskSheetDatesPK != null) || (this.lectureGroupTaskSheetDatesPK != null && !this.lectureGroupTaskSheetDatesPK.equals(other.lectureGroupTaskSheetDatesPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "de.phoenix.database.entityt.LectureGroupTaskSheetDates[ lectureGroupTaskSheetDatesPK=" + lectureGroupTaskSheetDatesPK + " ]";
    }

}
