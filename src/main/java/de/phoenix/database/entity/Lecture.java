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

@Entity
@Table(name = "lecture")
@XmlRootElement
//@formatter:off
@NamedQueries({
    @NamedQuery(name = "Lecture.findAll", query = "SELECT l FROM Lecture l"),
    @NamedQuery(name = "Lecture.findById", query = "SELECT l FROM Lecture l WHERE l.id = :id"),
    @NamedQuery(name = "Lecture.findByName", query = "SELECT l FROM Lecture l WHERE l.name = :name"),
    @NamedQuery(name = "Lecture.findByStartTime", query = "SELECT l FROM Lecture l WHERE l.startTime = :startTime"),
    @NamedQuery(name = "Lecture.findByEndTime", query = "SELECT l FROM Lecture l WHERE l.endTime = :endTime"),
    @NamedQuery(name = "Lecture.findByRoom", query = "SELECT l FROM Lecture l WHERE l.room = :room"),
    @NamedQuery(name = "Lecture.findByIsActive", query = "SELECT l FROM Lecture l WHERE l.isActive = :isActive")})
//@formatter:on
public class Lecture implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;

    @Basic(optional = false)
    @Column(name = "name")
    private String name;

    @Basic(optional = false)
    @Column(name = "startTime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startTime;

    @Basic(optional = false)
    @Column(name = "endTime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endTime;

    @Basic(optional = false)
    @Column(name = "room")
    private String room;

    @Basic(optional = false)
    @Column(name = "isActive")
    private boolean isActive;

    @JoinTable(name = "lectureMaterial", joinColumns = {@JoinColumn(name = "lecture_id", referencedColumnName = "id")}, inverseJoinColumns = {@JoinColumn(name = "material_id", referencedColumnName = "id")})
    @ManyToMany
    private List<Material> materialList;

    @ManyToMany(mappedBy = "lectureList")
    private List<User> userList;

    @ManyToMany(mappedBy = "lectureList1")
    private List<User> userList1;

    @JoinColumn(name = "instance_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Instance instanceId;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "lectureId")
    private List<News> newsList;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "lectureId")
    private List<SampleSolution> sampleSolutionList;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "lecture")
    private List<Group> groupList;

    public Lecture() {
    }

    public Lecture(Integer id) {
        this.id = id;
    }

    public Lecture(Integer id, String name, Date startTime, Date endTime, String room, boolean isActive) {
        this.id = id;
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.room = room;
        this.isActive = isActive;
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

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public boolean issActive() {
        return isActive;
    }

    public void enable() {
        this.isActive = true;
    }

    public void disable() {
        this.isActive = false;
    }

    @XmlTransient
    public List<Material> getMaterials() {
        return materialList;
    }

    public void setMaterials(List<Material> materials) {
        this.materialList = materials;
    }

    @XmlTransient
    public List<User> getLectureLeader() {
        return userList;
    }

    public void setLectureLeader(List<User> lectureLeader) {
        this.userList = lectureLeader;
    }

    @XmlTransient
    public List<User> getGroupLeader() {
        return userList1;
    }

    public void setGroupLeader(List<User> groupLeader) {
        this.userList1 = groupLeader;
    }

    public Instance getInstance() {
        return instanceId;
    }

    public void setInstance(Instance instance) {
        this.instanceId = instance;
    }

    @XmlTransient
    public List<News> getNews() {
        return newsList;
    }

    public void setNews(List<News> news) {
        this.newsList = news;
    }

    @XmlTransient
    public List<SampleSolution> getSampleSolutions() {
        return sampleSolutionList;
    }

    public void setSampleSolutions(List<SampleSolution> sampleSolutions) {
        this.sampleSolutionList = sampleSolutions;
    }

    @XmlTransient
    public List<Group> getGroups() {
        return groupList;
    }

    public void setGroups(List<Group> groups) {
        this.groupList = groups;
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
        if (!(object instanceof Lecture)) {
            return false;
        }
        Lecture other = (Lecture) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "de.phoenix.database.entity.Lecture[ id=" + id + " ]";
    }

}
