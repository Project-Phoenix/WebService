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
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.annotations.Type;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.joda.time.Period;

import de.phoenix.database.entity.util.Convertable;
import de.phoenix.rs.entity.PhoenixDetails;

@Entity
@Table(name = "details")
@XmlRootElement
//@formatter:off
@NamedQueries({
    @NamedQuery(name = "Details.findAll", query = "SELECT d FROM Details d"),
    @NamedQuery(name = "Details.findById", query = "SELECT d FROM Details d WHERE d.id = :id"),
    @NamedQuery(name = "Details.findByRoom", query = "SELECT d FROM Details d WHERE d.room = :room"),
    @NamedQuery(name = "Details.findByWeekday", query = "SELECT d FROM Details d WHERE d.weekday = :weekday"),
    @NamedQuery(name = "Details.findByStartTime", query = "SELECT d FROM Details d WHERE d.startTime = :startTime"),
    @NamedQuery(name = "Details.findByEndTime", query = "SELECT d FROM Details d WHERE d.endTime = :endTime"),
    @NamedQuery(name = "Details.findByInterval", query = "SELECT d FROM Details d WHERE d.interval = :interval"),
    @NamedQuery(name = "Details.findByStartDate", query = "SELECT d FROM Details d WHERE d.startDate = :startDate"),
    @NamedQuery(name = "Details.findByEndDate", query = "SELECT d FROM Details d WHERE d.endDate = :endDate"),
    @NamedQuery(name = "Details.findByPhoenixDetails", query = "SELECT d FROM Details d WHERE d.room = :room AND d.weekday = :weekday AND d.startTime = :startTime AND d.endTime = :endTime AND d.interval = :interval AND d.startDate = :startDate AND d.endDate = :endDate")})
//@formatter:on
public class Details implements Serializable, Convertable<PhoenixDetails> {

    private static final long serialVersionUID = 1L;

    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "room")
    private String room;

    @Column(name = "weekday")
    private Integer weekday;

    @Column(name = "startTime")
    @Type(type = "org.joda.time.contrib.hibernate.PersistentLocalTimeAsTime")
    private LocalTime startTime;

    @Column(name = "endTime")
    @Type(type = "org.joda.time.contrib.hibernate.PersistentLocalTimeAsTime")
    private LocalTime endTime;

    // Brackets [] because of , interval is a keyword
    @Column(name = "[interval]")
    @Type(type = "org.joda.time.contrib.hibernate.PersistentPeriod")
    private Period interval;

    @Column(name = "startDate")
    @Type(type = "org.joda.time.contrib.hibernate.PersistentLocalDate")
    private LocalDate startDate;

    @Column(name = "endDate")
    @Type(type = "org.joda.time.contrib.hibernate.PersistentLocalDate")
    private LocalDate endDate;

    @ManyToMany(mappedBy = "detailsList")
    private List<LectureGroup> lectureGroupList;

    @ManyToMany(mappedBy = "detailsList")
    private List<Lecture> lectureList;

    public Details() {
    }

    public Details(Integer id) {
        this.id = id;
    }

    public Details(PhoenixDetails details) {
        this.room = details.getRoom();
        this.setInterval(details.getInverval());
        this.weekday = details.getWeekDay();

        this.startTime = details.getStartTime();
        this.endTime = details.getEndTime();

        this.startDate = details.getStartDate();
        this.endDate = details.getEndDate();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public Integer getWeekday() {
        return weekday;
    }

    public void setWeekday(Integer weekday) {
        this.weekday = weekday;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public Period getInterval() {
        return interval;
    }

    public void setInterval(Period interval) {
        this.interval = interval;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    @XmlTransient
    public List<LectureGroup> getLectureGroups() {
        return lectureGroupList;
    }

    public void setLectureGroups(List<LectureGroup> lectureGroups) {
        this.lectureGroupList = lectureGroups;
    }

    @XmlTransient
    public List<Lecture> getLectures() {
        return lectureList;
    }

    public void setLectures(List<Lecture> lectures) {
        this.lectureList = lectures;
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
        if (!(object instanceof Details)) {
            return false;
        }
        Details other = (Details) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "de.phoenix.database.entityt.Details[ id=" + id + " ]";
    }

    @Override
    public PhoenixDetails convert() {
        return new PhoenixDetails(getRoom(), getWeekday(), new LocalTime(getStartTime()), new LocalTime(getEndTime()), getInterval(), new LocalDate(getStartDate()), new LocalDate(getEndDate()));
    }
}
