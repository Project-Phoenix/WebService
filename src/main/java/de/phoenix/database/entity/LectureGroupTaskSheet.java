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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import de.phoenix.database.entity.util.Convertable;
import de.phoenix.rs.entity.PhoenixLectureGroupTaskSheet;

@Entity
@Table(name = "lectureGroupTaskSheet")
public class LectureGroupTaskSheet implements Serializable, Convertable<PhoenixLectureGroupTaskSheet> {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;

    @Column(name = "defaultDeadline")
    @Type(type = "org.joda.time.contrib.hibernate.PersistentDateTime")
    private DateTime defaultDeadline;

    @Column(name = "defaultReleaseDate")
    @Type(type = "org.joda.time.contrib.hibernate.PersistentDateTime")
    private DateTime defaultReleaseDate;

    @OneToMany(mappedBy = "lectureGroupTaskSheet")
    private List<TaskSubmissionDates> taskSubmissionDatesList;

    @JoinColumn(name = "lectureGroup", referencedColumnName = "id")
    @ManyToOne
    private LectureGroup lectureGroup;

    @JoinColumn(name = "taskSheet", referencedColumnName = "id")
    @ManyToOne
    private TaskSheet taskSheet;

    public LectureGroupTaskSheet() {
    }

    public LectureGroupTaskSheet(Integer id) {
        this.id = id;
    }

    public LectureGroupTaskSheet(DateTime defaultDeadline, DateTime defaultReleaseDate) {
        this.defaultDeadline = defaultDeadline;
        this.defaultReleaseDate = defaultReleaseDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public DateTime getDefaultDeadline() {
        return defaultDeadline;
    }

    public void setDefaultDeadline(DateTime defaultDeadline) {
        this.defaultDeadline = defaultDeadline;
    }

    public DateTime getDefaultReleaseDate() {
        return defaultReleaseDate;
    }

    public void setDefaultReleaseDate(DateTime defaultReleaseDate) {
        this.defaultReleaseDate = defaultReleaseDate;
    }

    @XmlTransient
    public List<TaskSubmissionDates> getTaskSubmissionDates() {
        return taskSubmissionDatesList;
    }

    public void setTaskSubmissionDates(List<TaskSubmissionDates> taskSubmissionDates) {
        this.taskSubmissionDatesList = taskSubmissionDates;
    }

    public LectureGroup getLectureGroup() {
        return lectureGroup;
    }

    public void setLectureGroup(LectureGroup lectureGroup) {
        this.lectureGroup = lectureGroup;
    }

    public TaskSheet getTaskSheet() {
        return taskSheet;
    }

    public void setTaskSheet(TaskSheet taskSheet) {
        this.taskSheet = taskSheet;
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
        if (!(object instanceof LectureGroupTaskSheet)) {
            return false;
        }
        LectureGroupTaskSheet other = (LectureGroupTaskSheet) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "de.phoenix.database.entity.LectureGroupTaskSheet[ id=" + id + " ]";
    }

    @Override
    public PhoenixLectureGroupTaskSheet convert() {
        return new PhoenixLectureGroupTaskSheet(defaultDeadline, defaultReleaseDate, this.taskSheet.convert(), this.lectureGroup.convert());
    }

    @Override
    public void copyValues(PhoenixLectureGroupTaskSheet phoenixEntity) {
        this.defaultDeadline = phoenixEntity.getDefaultDeadline();
        this.defaultReleaseDate = phoenixEntity.getDefaultReleaseDate();

    }

}
