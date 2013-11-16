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
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

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
    @NamedQuery(name = "Details.findByTime", query = "SELECT d FROM Details d WHERE d.time = :time"),
    @NamedQuery(name = "Details.findByTurnus", query = "SELECT d FROM Details d WHERE d.turnus = :turnus"),
    @NamedQuery(name = "Details.findByStartTime", query = "SELECT d FROM Details d WHERE d.startTime = :startTime"),
    @NamedQuery(name = "Details.findByEndTime", query = "SELECT d FROM Details d WHERE d.endTime = :endTime")})
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

    @Column(name = "time")
    @Temporal(TemporalType.TIME)
    private Date time;

    @Column(name = "turnus")
    private String turnus;

    @Column(name = "startTime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startTime;

    @Column(name = "endTime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endTime;

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
        this.turnus = details.getInvervall();
        this.weekday = details.getWeekDay();
        this.time = details.getTime().toDateTimeToday().toDate();
        this.startTime = details.getStartDate().toDate();
        this.endTime = details.getEndDate().toDate();
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

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getTurnus() {
        return turnus;
    }

    public void setTurnus(String turnus) {
        this.turnus = turnus;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
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
        return new PhoenixDetails(getRoom(), getWeekday(), new LocalTime(getTime()), getTurnus(), new LocalDate(getStartTime()), new LocalDate(getEndTime()));
    }

}
