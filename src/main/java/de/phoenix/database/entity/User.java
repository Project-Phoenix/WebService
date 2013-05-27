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
@Table(name = "user")
@XmlRootElement
//@formatter:off
@NamedQueries({
    @NamedQuery(name = "User.findAll", query = "SELECT u FROM User u"),
    @NamedQuery(name = "User.findById", query = "SELECT u FROM User u WHERE u.id = :id"),
    @NamedQuery(name = "User.findBySurname", query = "SELECT u FROM User u WHERE u.surname = :surname"),
    @NamedQuery(name = "User.findByName", query = "SELECT u FROM User u WHERE u.name = :name"),
    @NamedQuery(name = "User.findByTitle", query = "SELECT u FROM User u WHERE u.title = :title"),
    @NamedQuery(name = "User.findByMatrikelNr", query = "SELECT u FROM User u WHERE u.matrikelNr = :matrikelNr"),
    @NamedQuery(name = "User.findByUsername", query = "SELECT u FROM User u WHERE u.username = :username"),
    @NamedQuery(name = "User.findByPassword", query = "SELECT u FROM User u WHERE u.password = :password"),
    @NamedQuery(name = "User.findBySalt", query = "SELECT u FROM User u WHERE u.salt = :salt"),
    @NamedQuery(name = "User.findByEmail", query = "SELECT u FROM User u WHERE u.email = :email"),
    @NamedQuery(name = "User.findByRegdate", query = "SELECT u FROM User u WHERE u.regdate = :regdate"),
    @NamedQuery(name = "User.findByIsActive", query = "SELECT u FROM User u WHERE u.isActive = :isActive")})
//@formatter:on
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;

    @Basic(optional = false)
    @Column(name = "surname")
    private String surname;

    @Basic(optional = false)
    @Column(name = "name")
    private String name;

    @Basic(optional = false)
    @Column(name = "title")
    private String title;

    @Column(name = "matrikelNr")
    private String matrikelNr;

    @Basic(optional = false)
    @Column(name = "username")
    private String username;

    @Basic(optional = false)
    @Column(name = "password")
    private String password;

    @Basic(optional = false)
    @Column(name = "salt")
    private String salt;

    @Basic(optional = false)
    @Column(name = "email")
    private String email;

    @Basic(optional = false)
    @Column(name = "regdate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date regdate;

    @Basic(optional = false)
    @Column(name = "isActive")
    private boolean isActive;

    @JoinTable(name = "instanceAdmin", joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")}, inverseJoinColumns = {@JoinColumn(name = "instance_id", referencedColumnName = "id")})
    @ManyToMany
    private List<Instance> instanceList;

    @ManyToMany(mappedBy = "userList")
    private List<Message> messageList;

    @JoinTable(name = "groupMember", joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")}, inverseJoinColumns = {@JoinColumn(name = "group_id", referencedColumnName = "id")})
    @ManyToMany
    private List<Group> groupList;

    @JoinTable(name = "lectureLeader", joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")}, inverseJoinColumns = {@JoinColumn(name = "lecture_id", referencedColumnName = "id")})
    @ManyToMany
    private List<Lecture> lectureList;

    @JoinTable(name = "groupLeader", joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")}, inverseJoinColumns = {@JoinColumn(name = "lecture_id", referencedColumnName = "id")})
    @ManyToMany
    private List<Lecture> lectureList1;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "author")
    private List<News> newsList;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "sender")
    private List<Message> messageList1;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "exerciseLeader")
    private List<Group> groupList1;

    @JoinColumn(name = "role_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Role roleId;

    public User() {
    }

    public User(Integer id) {
        this.id = id;
    }

    public User(Integer id, String surname, String name, String title, String username, String password, String salt, String email, Date regdate, boolean isActive) {
        this.id = id;
        this.surname = surname;
        this.name = name;
        this.title = title;
        this.username = username;
        this.password = password;
        this.salt = salt;
        this.email = email;
        this.regdate = regdate;
        this.isActive = isActive;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMatrikelNr() {
        return matrikelNr;
    }

    public void setMatrikelNr(String matrikelNr) {
        this.matrikelNr = matrikelNr;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getRegdate() {
        return regdate;
    }

    public void setRegdate(Date regdate) {
        this.regdate = regdate;
    }

    public boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    @XmlTransient
    public List<Instance> getLeadingInstances() {
        return instanceList;
    }

    public void setLeadingInstances(List<Instance> leadingInstances) {
        this.instanceList = leadingInstances;
    }

    @XmlTransient
    public List<Message> getReceivedMessages() {
        return messageList;
    }

    public void setReceivedMessages(List<Message> receivedMessages) {
        this.messageList = receivedMessages;
    }

    @XmlTransient
    public List<Group> getJoinedGroups() {
        return groupList;
    }

    public void setJoinedGroups(List<Group> joinedGroups) {
        this.groupList = joinedGroups;
    }

    @XmlTransient
    public List<Lecture> getLeadingLectures() {
        return lectureList;
    }

    public void setLeadingLectures(List<Lecture> leadingLectures) {
        this.lectureList = leadingLectures;
    }

    @XmlTransient
    /**
     * @return Lectures where the user is a group leader 
     */
    public List<Lecture> getLeadingGroups() {
        return lectureList1;
    }

    /**
     * @param leadingGroups
     *            Lectures where the user is group leader
     */
    public void setLeadingGroups(List<Lecture> leadingGroups) {
        this.lectureList1 = leadingGroups;
    }

    @XmlTransient
    public List<News> getWrittenNews() {
        return newsList;
    }

    public void setWrittenNews(List<News> writtenNews) {
        this.newsList = writtenNews;
    }

    @XmlTransient
    public List<Message> getSentMessages() {
        return messageList1;
    }

    public void setSentMessages(List<Message> sentMessages) {
        this.messageList1 = sentMessages;
    }

    @XmlTransient
    /**
     * @return Groups where this user is exercise leader
     */
    public List<Group> getLeadingExerciseGroups() {
        return groupList1;
    }

    public void setLeadingExerciseGroups(List<Group> leadingExerciseGroups) {
        this.groupList1 = leadingExerciseGroups;
    }

    public Role getRole() {
        return roleId;
    }

    public void setRole(Role roleId) {
        this.roleId = roleId;
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
        if (!(object instanceof User)) {
            return false;
        }
        User other = (User) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "de.phoenix.database.entity.User[ id=" + id + " ]";
    }

}
