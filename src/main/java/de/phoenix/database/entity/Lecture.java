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
import javax.persistence.Id;
import javax.persistence.JoinColumn;
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
@Table(name = "lecture")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Lecture.findAll", query = "SELECT l FROM Lecture l"),
    @NamedQuery(name = "Lecture.findById", query = "SELECT l FROM Lecture l WHERE l.id = :id"),
    @NamedQuery(name = "Lecture.findByName", query = "SELECT l FROM Lecture l WHERE l.name = :name"),
    @NamedQuery(name = "Lecture.findByLectur", query = "SELECT l FROM Lecture l WHERE l.lectur = :lectur"),
    @NamedQuery(name = "Lecture.findByTime", query = "SELECT l FROM Lecture l WHERE l.time = :time"),
    @NamedQuery(name = "Lecture.findByRoom", query = "SELECT l FROM Lecture l WHERE l.room = :room")})
public class Lecture implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "name")
    private String name;
    @Basic(optional = false)
    @Column(name = "lectur")
    private String lectur;
    @Basic(optional = false)
    @Column(name = "time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date time;
    @Basic(optional = false)
    @Column(name = "room")
    private String room;
    @ManyToMany(mappedBy = "lectureCollection")
    private Collection<User> userCollection;
    @JoinColumn(name = "instance_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Instance instanceId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "lectureId")
    private Collection<Group> GroupCollection;

    public Lecture() {
    }

    public Lecture(Integer id) {
        this.id = id;
    }

    public Lecture(Integer id, String name, String lectur, Date time, String room) {
        this.id = id;
        this.name = name;
        this.lectur = lectur;
        this.time = time;
        this.room = room;
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

    public String getLectur() {
        return lectur;
    }

    public void setLectur(String lectur) {
        this.lectur = lectur;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    @XmlTransient
    public Collection<User> getUserCollection() {
        return userCollection;
    }

    public void setUserCollection(Collection<User> userCollection) {
        this.userCollection = userCollection;
    }

    public Instance getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(Instance instanceId) {
        this.instanceId = instanceId;
    }

    @XmlTransient
    public Collection<Group> getGroupCollection() {
        return GroupCollection;
    }

    public void setGroupCollection(Collection<Group> GroupCollection) {
        this.GroupCollection = GroupCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
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
