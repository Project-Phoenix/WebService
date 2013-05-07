/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.phoenix.database.entity;

import java.io.Serializable;
import java.util.Collection;
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
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Meldanor
 */
@Entity
@Table(name = "group")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Group.findAll", query = "SELECT g FROM Group g"),
    @NamedQuery(name = "Group.findById", query = "SELECT g FROM Group g WHERE g.id = :id"),
    @NamedQuery(name = "Group.findByRoom", query = "SELECT g FROM Group g WHERE g.room = :room"),
    @NamedQuery(name = "Group.findByTurnus", query = "SELECT g FROM Group g WHERE g.turnus = :turnus")})
public class Group implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "room")
    private String room;
    @Basic(optional = false)
    @Column(name = "turnus")
    private String turnus;
    @ManyToMany(mappedBy = "GroupCollection")
    private Collection<User> userCollection;
    @JoinTable(name = "group_has_exercise_sheet", joinColumns = {
        @JoinColumn(name = "group_id", referencedColumnName = "id")}, inverseJoinColumns = {
        @JoinColumn(name = "exercise_sheet_id", referencedColumnName = "id")})
    @ManyToMany
    private Collection<ExerciseSheet> exerciseSheetCollection;
    @JoinColumn(name = "lecture_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Lecture lectureId;

    public Group() {
    }

    public Group(Integer id) {
        this.id = id;
    }

    public Group(Integer id, String room, String turnus) {
        this.id = id;
        this.room = room;
        this.turnus = turnus;
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

    public String getTurnus() {
        return turnus;
    }

    public void setTurnus(String turnus) {
        this.turnus = turnus;
    }

    @XmlTransient
    public Collection<User> getUserCollection() {
        return userCollection;
    }

    public void setUserCollection(Collection<User> userCollection) {
        this.userCollection = userCollection;
    }

    @XmlTransient
    public Collection<ExerciseSheet> getExerciseSheetCollection() {
        return exerciseSheetCollection;
    }

    public void setExerciseSheetCollection(Collection<ExerciseSheet> exerciseSheetCollection) {
        this.exerciseSheetCollection = exerciseSheetCollection;
    }

    public Lecture getLectureId() {
        return lectureId;
    }

    public void setLectureId(Lecture lectureId) {
        this.lectureId = lectureId;
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
