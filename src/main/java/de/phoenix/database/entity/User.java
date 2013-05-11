/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.phoenix.database.entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
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

/**
 * 
 * @author Meldanor
 */
@Entity
@Table(name = "user")
@XmlRootElement
@NamedQueries({@NamedQuery(name = "User.findAll", query = "SELECT u FROM User u"), @NamedQuery(name = "User.findById", query = "SELECT u FROM User u WHERE u.id = :id"), @NamedQuery(name = "User.findByAccountName", query = "SELECT u FROM User u WHERE u.accountName = :accountName"), @NamedQuery(name = "User.findByName", query = "SELECT u FROM User u WHERE u.name = :name"), @NamedQuery(name = "User.findBySurname", query = "SELECT u FROM User u WHERE u.surname = :surname"), @NamedQuery(name = "User.findByGender", query = "SELECT u FROM User u WHERE u.gender = :gender"), @NamedQuery(name = "User.findByTitle", query = "SELECT u FROM User u WHERE u.title = :title"), @NamedQuery(name = "User.findByMatrikel", query = "SELECT u FROM User u WHERE u.matrikel = :matrikel"), @NamedQuery(name = "User.findByPassword", query = "SELECT u FROM User u WHERE u.password = :password"), @NamedQuery(name = "User.findBySalt", query = "SELECT u FROM User u WHERE u.salt = :salt"), @NamedQuery(name = "User.findByRegdate", query = "SELECT u FROM User u WHERE u.regdate = :regdate"), @NamedQuery(name = "User.findByIsActive", query = "SELECT u FROM User u WHERE u.isActive = :isActive")})
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "accountName")
    private String accountName;
    @Basic(optional = false)
    @Column(name = "name")
    private String name;
    @Basic(optional = false)
    @Column(name = "surname")
    private String surname;
    @Basic(optional = false)
    @Column(name = "gender")
    private boolean gender;
    @Basic(optional = false)
    @Column(name = "title")
    private String title;
    @Column(name = "matrikel")
    private String matrikel;
    @Basic(optional = false)
    @Column(name = "password")
    private String password;
    @Basic(optional = false)
    @Column(name = "salt")
    private String salt;
    @Basic(optional = false)
    @Column(name = "regdate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date regdate;
    @Basic(optional = false)
    @Column(name = "isActive")
    private boolean isActive;
    @ManyToMany(mappedBy = "userCollection")
    private Collection<Instance> instanceCollection;
    @JoinTable(name = "user_is_in_group", joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")}, inverseJoinColumns = {@JoinColumn(name = "group_id", referencedColumnName = "id")})
    @ManyToMany
    private Collection<Group> joinedGroups;
    @JoinTable(name = "group_leader", joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")}, inverseJoinColumns = {@JoinColumn(name = "group_id", referencedColumnName = "id")})
    @ManyToMany
    private Collection<Group> leadingGroups;
    @JoinTable(name = "lectur", joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")}, inverseJoinColumns = {@JoinColumn(name = "lecture_id", referencedColumnName = "id")})
    @ManyToMany
    private Collection<Lecture> lectureCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "author")
    private Collection<News> writtenNews;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userId")
    private Collection<Submission> submissionCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "receiver")
    private Collection<Message> receivedMessages;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "sender")
    private Collection<Message> sentMessages;
    @JoinColumn(name = "role_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Role roleId;

    public User() {
    }

    public User(Integer id) {
        this.id = id;
    }

    public User(Integer id, String accountName, String name, String surname, boolean gender, String title, String password, String salt, Date regdate, boolean isActive) {
        this.id = id;
        this.accountName = accountName;
        this.name = name;
        this.surname = surname;
        this.gender = gender;
        this.title = title;
        this.password = password;
        this.salt = salt;
        this.regdate = regdate;
        this.isActive = isActive;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public boolean getGender() {
        return gender;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMatrikel() {
        return matrikel;
    }

    public void setMatrikel(String matrikel) {
        this.matrikel = matrikel;
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
    public Collection<Instance> getInstanceCollection() {
        return instanceCollection;
    }

    public void setInstanceCollection(Collection<Instance> instanceCollection) {
        this.instanceCollection = instanceCollection;
    }

    @XmlTransient
    public Collection<Group> getjoinedGroups() {
        return joinedGroups;
    }

    public void setjoinedGroups(Collection<Group> joinedGroups) {
        this.joinedGroups = joinedGroups;
    }

    @XmlTransient
    public Collection<Group> getleadingGroups() {
        return leadingGroups;
    }

    public void setleadingGroups(Collection<Group> leadingGroups) {
        this.leadingGroups = leadingGroups;
    }

    @XmlTransient
    public Collection<Lecture> getLectureCollection() {
        return lectureCollection;
    }

    public void setLectureCollection(Collection<Lecture> lectureCollection) {
        this.lectureCollection = lectureCollection;
    }

    @XmlTransient
    public Collection<News> getWrittenNews() {
        return writtenNews;
    }

    public void WrittenNews(Collection<News> writtenNews) {
        this.writtenNews = writtenNews;
    }

    @XmlTransient
    public Collection<Submission> getSubmissionCollection() {
        return submissionCollection;
    }

    public void setSubmissionCollection(Collection<Submission> submissionCollection) {
        this.submissionCollection = submissionCollection;
    }

    @XmlTransient
    public Collection<Message> getSentMessages() {
        return this.sentMessages;
    }

    public void setSentMessages(Collection<Message> sentMessages) {
        this.sentMessages = sentMessages;
    }

    @XmlTransient
    public Collection<Message> getReceivedMessages() {
        return this.receivedMessages;
    }

    public void setReceivedMessages(Collection<Message> receivedMessages) {
        this.receivedMessages = receivedMessages;
    }

    public Role getRoleId() {
        return roleId;
    }

    public void setRoleId(Role roleId) {
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
