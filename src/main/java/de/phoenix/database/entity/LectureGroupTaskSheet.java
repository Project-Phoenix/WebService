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
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table(name = "lectureGroupTaskSheet")
@XmlRootElement
//@formatter:off
@NamedQueries({
    @NamedQuery(name = "LectureGroupTaskSheet.findAll", query = "SELECT l FROM LectureGroupTaskSheet l"),
    @NamedQuery(name = "LectureGroupTaskSheet.findByGroup", query = "SELECT l FROM LectureGroupTaskSheet l WHERE l.lectureGroupTaskSheetPK.group = :group"),
    @NamedQuery(name = "LectureGroupTaskSheet.findByTaskSheet", query = "SELECT l FROM LectureGroupTaskSheet l WHERE l.lectureGroupTaskSheetPK.taskSheet = :taskSheet"),
    @NamedQuery(name = "LectureGroupTaskSheet.findByDefaultDeadline", query = "SELECT l FROM LectureGroupTaskSheet l WHERE l.defaultDeadline = :defaultDeadline"),
    @NamedQuery(name = "LectureGroupTaskSheet.findByDefaultReleaseDate", query = "SELECT l FROM LectureGroupTaskSheet l WHERE l.defaultReleaseDate = :defaultReleaseDate")})
//@formatter:on
public class LectureGroupTaskSheet implements Serializable {

    private static final long serialVersionUID = 1L;

    @EmbeddedId
    protected LectureGroupTaskSheetPK lectureGroupTaskSheetPK;

    @Column(name = "defaultDeadline")
    @Temporal(TemporalType.TIMESTAMP)
    private Date defaultDeadline;

    @Column(name = "defaultReleaseDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date defaultReleaseDate;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "lectureGroupTaskSheet")
    private List<LectureGroupTaskSheetDates> lectureGroupTaskSheetDatesList;

    @JoinColumn(name = "taskSheet", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private TaskSheet taskSheet1;

    @JoinColumn(name = "group", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private LectureGroup lectureGroup;

    public LectureGroupTaskSheet() {
    }

    public LectureGroupTaskSheet(LectureGroupTaskSheetPK lectureGroupTaskSheetPK) {
        this.lectureGroupTaskSheetPK = lectureGroupTaskSheetPK;
    }

    public LectureGroupTaskSheet(int group, int taskSheet) {
        this.lectureGroupTaskSheetPK = new LectureGroupTaskSheetPK(group, taskSheet);
    }

    public LectureGroupTaskSheetPK getLectureGroupTaskSheetPK() {
        return lectureGroupTaskSheetPK;
    }

    public void setLectureGroupTaskSheetPK(LectureGroupTaskSheetPK lectureGroupTaskSheetPK) {
        this.lectureGroupTaskSheetPK = lectureGroupTaskSheetPK;
    }

    public Date getDefaultDeadline() {
        return defaultDeadline;
    }

    public void setDefaultDeadline(Date defaultDeadline) {
        this.defaultDeadline = defaultDeadline;
    }

    public Date getDefaultReleaseDate() {
        return defaultReleaseDate;
    }

    public void setDefaultReleaseDate(Date defaultReleaseDate) {
        this.defaultReleaseDate = defaultReleaseDate;
    }

    @XmlTransient
    public List<LectureGroupTaskSheetDates> getLectureGroupTaskSheetDates() {
        return lectureGroupTaskSheetDatesList;
    }

    public void setLectureGroupTaskSheetDates(List<LectureGroupTaskSheetDates> lectureGroupTaskSheetDates) {
        this.lectureGroupTaskSheetDatesList = lectureGroupTaskSheetDates;
    }

    public TaskSheet getTaskSheet() {
        return taskSheet1;
    }

    public void setTaskSheet(TaskSheet taskSheet) {
        this.taskSheet1 = taskSheet;
    }

    public LectureGroup getLectureGroup() {
        return lectureGroup;
    }

    public void setLectureGroup(LectureGroup lectureGroup) {
        this.lectureGroup = lectureGroup;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (lectureGroupTaskSheetPK != null ? lectureGroupTaskSheetPK.hashCode() : 0);
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
        if ((this.lectureGroupTaskSheetPK == null && other.lectureGroupTaskSheetPK != null) || (this.lectureGroupTaskSheetPK != null && !this.lectureGroupTaskSheetPK.equals(other.lectureGroupTaskSheetPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "de.phoenix.database.entityt.LectureGroupTaskSheet[ lectureGroupTaskSheetPK=" + lectureGroupTaskSheetPK + " ]";
    }

}
