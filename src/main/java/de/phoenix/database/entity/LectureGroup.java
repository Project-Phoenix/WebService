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
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Type;
import org.joda.time.LocalTime;

import de.phoenix.database.entity.util.Convertable;
import de.phoenix.database.entity.util.ConverterUtil;
import de.phoenix.date.Weekday;
import de.phoenix.rs.entity.PhoenixDetails;
import de.phoenix.rs.entity.PhoenixLectureGroup;

/**
 * Class defining database strucut for single groups assigned to a lecture.
 * PhoenixEntity is {@link PhoenixLectureGroup}
 */
@Entity
@Table(name = "lectureGroup")
public class LectureGroup implements Serializable, Convertable<PhoenixLectureGroup> {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @Lob
    @Column(name = "description")
    private String description;

    @Column(name = "maxMember")
    private Integer maxMember;

    @Column(name = "submissionDeadlineTime")
    @Type(type = "org.joda.time.contrib.hibernate.PersistentLocalTimeAsTime")
    private LocalTime submissionDeadlineTime;

    @Column(name = "submissionDeadlineWeekday", columnDefinition = "TINYINT")
    private int submissionDeadlineWeekday;

    //@formatter:off
    @JoinTable(name = "lectureGroupDetails", joinColumns = {
        @JoinColumn(name = "group_id", referencedColumnName = "id")}, inverseJoinColumns = {
        @JoinColumn(name = "additionalInfo_id", referencedColumnName = "id")})
    //@formatter:on
    @ManyToMany
    @Cascade({CascadeType.SAVE_UPDATE, CascadeType.DELETE})
    private List<Details> detailsList;

    @JoinColumn(name = "lecture", referencedColumnName = "id", unique = true)
    @ManyToOne(optional = false)
    @Cascade(CascadeType.SAVE_UPDATE)
    private Lecture lecture;

    @OneToMany(mappedBy = "lectureGroup")
    private List<LectureGroupTaskSheet> lectureGroupTaskSheetList;

    public LectureGroup() {
    }

    public LectureGroup(Integer id) {
        this.id = id;
    }

    public LectureGroup(PhoenixLectureGroup phoenixLectureGroup, Lecture lecture) {
        this.maxMember = phoenixLectureGroup.getMaxMember();
        this.name = phoenixLectureGroup.getName();
        this.description = phoenixLectureGroup.getDescription();
        this.submissionDeadlineTime = phoenixLectureGroup.getSubmissionDeadlineTime();
        this.submissionDeadlineWeekday = phoenixLectureGroup.getSubmissionDeadlineWeekday().getDateTimeConstant();
        this.lecture = lecture;

        this.detailsList = new ArrayList<Details>();
        for (PhoenixDetails phoenixDetails : phoenixLectureGroup.getDetails()) {
            detailsList.add(new Details(phoenixDetails));
        }
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public LocalTime getSubmissionDeadlineTime() {
        return submissionDeadlineTime;
    }

    public void setSubmissionDeadlineTime(LocalTime submissionDeadlineTime) {
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
        return new PhoenixLectureGroup(getName(), getDescription(), getMaxMember(), Weekday.forID(getSubmissionDeadlineWeekday()), new LocalTime(getSubmissionDeadlineTime()), ConverterUtil.convert(getDetails()), getLecture().convert());
    }

    @Override
    public void copyValues(PhoenixLectureGroup phoenixEntity) {
        this.setName(phoenixEntity.getName());
        this.setMaxMember(phoenixEntity.getMaxMember());
        this.setSubmissionDeadlineTime(phoenixEntity.getSubmissionDeadlineTime());
        this.setSubmissionDeadlineWeekday(phoenixEntity.getSubmissionDeadlineWeekday().getDateTimeConstant());
    }
}
