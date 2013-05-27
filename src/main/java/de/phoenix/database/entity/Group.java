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
@Table(name = "group")
@XmlRootElement
//@formatter:off
@NamedQueries({
    @NamedQuery(name = "Group.findAll", query = "SELECT g FROM Group g"),
    @NamedQuery(name = "Group.findById", query = "SELECT g FROM Group g WHERE g.id = :id"),
    @NamedQuery(name = "Group.findByName", query = "SELECT g FROM Group g WHERE g.name = :name"),
    @NamedQuery(name = "Group.findByRoom", query = "SELECT g FROM Group g WHERE g.room = :room"),
    @NamedQuery(name = "Group.findByTurnus", query = "SELECT g FROM Group g WHERE g.turnus = :turnus"),
    @NamedQuery(name = "Group.findBySubmissionExpireDate", query = "SELECT g FROM Group g WHERE g.submissionExpireDate = :submissionExpireDate"),
    @NamedQuery(name = "Group.findByRegistrationStartDate", query = "SELECT g FROM Group g WHERE g.registrationStartDate = :registrationStartDate"),
    @NamedQuery(name = "Group.findByRegistrationEndDate", query = "SELECT g FROM Group g WHERE g.registrationEndDate = :registrationEndDate")})
//@formatter:on
public class Group implements Serializable {

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
    @Column(name = "room")
    private String room;

    @Basic(optional = false)
    @Column(name = "turnus")
    private String turnus;

    @Column(name = "submission_expire_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date submissionExpireDate;

    @Basic(optional = false)
    @Column(name = "registration_start_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date registrationStartDate;

    @Basic(optional = false)
    @Column(name = "registration_end_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date registrationEndDate;

    @ManyToMany(mappedBy = "groupList")
    private List<User> userList;

    @JoinTable(name = "groupMaterial", joinColumns = {@JoinColumn(name = "group_id", referencedColumnName = "id")}, inverseJoinColumns = {@JoinColumn(name = "material_id", referencedColumnName = "id")})
    @ManyToMany
    private List<Material> materialList;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "group")
    private List<ExerciseSheet> exerciseSheetList;

    @JoinColumn(name = "exerciseLeader", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private User exerciseLeader;

    @JoinColumn(name = "lecture", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Lecture lecture;

    public Group() {
    }

    public Group(Integer id) {
        this.id = id;
    }

    public Group(Integer id, String name, String room, String turnus, Date registrationStartDate, Date registrationEndDate) {
        this.id = id;
        this.name = name;
        this.room = room;
        this.turnus = turnus;
        this.registrationStartDate = registrationStartDate;
        this.registrationEndDate = registrationEndDate;
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

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getTurnus() {
        return turnus;
    }

    public void setTurnus(String turnus) {
        this.turnus = turnus;
    }

    public Date getSubmissionExpireDate() {
        return submissionExpireDate;
    }

    public void setSubmissionExpireDate(Date submissionExpireDate) {
        this.submissionExpireDate = submissionExpireDate;
    }

    public Date getRegistrationStartDate() {
        return registrationStartDate;
    }

    public void setRegistrationStartDate(Date registrationStartDate) {
        this.registrationStartDate = registrationStartDate;
    }

    public Date getRegistrationEndDate() {
        return registrationEndDate;
    }

    public void setRegistrationEndDate(Date registrationEndDate) {
        this.registrationEndDate = registrationEndDate;
    }

    @XmlTransient
    public List<User> getMember() {
        return userList;
    }

    public void setMember(List<User> member) {
        this.userList = member;
    }

    @XmlTransient
    public List<Material> getMaterials() {
        return materialList;
    }

    public void setMaterials(List<Material> materials) {
        this.materialList = materials;
    }

    @XmlTransient
    public List<ExerciseSheet> getExerciseSheets() {
        return exerciseSheetList;
    }

    public void setExerciseSheets(List<ExerciseSheet> exerciseSheets) {
        this.exerciseSheetList = exerciseSheets;
    }

    public User getExerciseLeader() {
        return exerciseLeader;
    }

    public void setExerciseLeader(User exerciseLeader) {
        this.exerciseLeader = exerciseLeader;
    }

    public Lecture getLecture() {
        return lecture;
    }

    public void setLecture(Lecture lecture) {
        this.lecture = lecture;
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
        if (!(object instanceof Group)) {
            return false;
        }
        Group other = (Group) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "de.phoenix.database.entity.Group[ id=" + id + " ]";
    }

}
