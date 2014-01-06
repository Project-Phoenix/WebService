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
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.joda.time.LocalTime;

import de.phoenix.database.entity.util.Convertable;
import de.phoenix.database.entity.util.ConverterArrayList;
import de.phoenix.rs.entity.PhoenixDetails;
import de.phoenix.rs.entity.PhoenixLectureGroup;

@Entity
@Table(name = "lectureGroup")
@XmlRootElement
//@formatter:off
@NamedQueries({
    @NamedQuery(name = "LectureGroup.findAll", query = "SELECT l FROM LectureGroup l"),
    @NamedQuery(name = "LectureGroup.findById", query = "SELECT l FROM LectureGroup l WHERE l.id = :id"),
    @NamedQuery(name = "LectureGroup.findByName", query = "SELECT l FROM LectureGroup l WHERE l.name = :name"),
    @NamedQuery(name = "LectureGroup.findByMaxMember", query = "SELECT l FROM LectureGroup l WHERE l.maxMember = :maxMember"),
    @NamedQuery(name = "LectureGroup.findBySubmissionDeadlineTime", query = "SELECT l FROM LectureGroup l WHERE l.submissionDeadlineTime = :submissionDeadlineTime"),
    @NamedQuery(name = "LectureGroup.findBySubmissionDeadlineWeekday", query = "SELECT l FROM LectureGroup l WHERE l.submissionDeadlineWeekday = :submissionDeadlineWeekday")})
//@formatter:on
public class LectureGroup implements Serializable, Convertable<PhoenixLectureGroup> {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "maxMember")
    private Integer maxMember;

    @Column(name = "submissionDeadlineTime")
    @Temporal(TemporalType.TIME)
    private Date submissionDeadlineTime;

    @Column(name = "submissionDeadlineWeekday", columnDefinition = "TINYINT")
    private int submissionDeadlineWeekday;

    //@formatter:off
    @JoinTable(name = "lectureGroupDetails", joinColumns = {
        @JoinColumn(name = "group_id", referencedColumnName = "id")}, inverseJoinColumns = {
        @JoinColumn(name = "additionalInfo_id", referencedColumnName = "id")})
    //@formatter:on
    @ManyToMany
    @Cascade(CascadeType.SAVE_UPDATE)
    private List<Details> detailsList;

    @JoinColumn(name = "lecture", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Lecture lecture;

    @OneToMany(mappedBy = "lectureGroup")
    private List<LectureGroupTaskSheet> lectureGroupTaskSheetList;

    public LectureGroup() {
    }

    public LectureGroup(Integer id) {
        this.id = id;
    }

    public LectureGroup(PhoenixLectureGroup phoenixLectureGroup) {
        this.maxMember = phoenixLectureGroup.getMaxMember();
        this.name = phoenixLectureGroup.getName();
        this.submissionDeadlineTime = phoenixLectureGroup.getSubmissionDeadlineTime().toDateTimeToday().toDate();
        this.submissionDeadlineWeekday = phoenixLectureGroup.getSubmissionDeadlineWeekday();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getMaxMember() {
        return maxMember;
    }

    public void setMaxMember(Integer maxMember) {
        this.maxMember = maxMember;
    }

    public int getSubmissionDeadlineWeekday() {
        return submissionDeadlineWeekday;
    }

    public void setSubmissionDeadlineWeekday(int submissionDeadlineWeekday) {
        this.submissionDeadlineWeekday = submissionDeadlineWeekday;
    }

    public Date getSubmissionDeadlineTime() {
        return submissionDeadlineTime;
    }

    public void setSubmissionDeadlineTime(Date submissionDeadlineTime) {
        this.submissionDeadlineTime = submissionDeadlineTime;
    }

    @XmlTransient
    public List<Details> getDetails() {
        return detailsList;
    }

    public void setDetails(List<Details> details) {
        this.detailsList = details;
    }

    @XmlTransient
    public Lecture getLecture() {
        return lecture;
    }

    public void setLecture(Lecture lecture) {
        this.lecture = lecture;
    }

    @XmlTransient
    public List<LectureGroupTaskSheet> getLectureGroupTaskSheetList() {
        return lectureGroupTaskSheetList;
    }

    public void setLectureGroupTaskSheetList(List<LectureGroupTaskSheet> lectureGroupTaskSheetList) {
        this.lectureGroupTaskSheetList = lectureGroupTaskSheetList;
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
        if (!(object instanceof LectureGroup)) {
            return false;
        }
        LectureGroup other = (LectureGroup) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "de.phoenix.database.entityt.LectureGroup[ id=" + id + " ]";
    }

    @Override
    public PhoenixLectureGroup convert() {
        return new PhoenixLectureGroup(getName(), getMaxMember(), getSubmissionDeadlineWeekday(), new LocalTime(getSubmissionDeadlineTime()), new ConverterArrayList<PhoenixDetails>(getDetails()), getLecture().convert());
    }
}
