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
import javax.persistence.ManyToMany;
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
@Table(name = "exercise_sheet")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ExerciseSheet.findAll", query = "SELECT e FROM ExerciseSheet e"),
    @NamedQuery(name = "ExerciseSheet.findById", query = "SELECT e FROM ExerciseSheet e WHERE e.id = :id"),
    @NamedQuery(name = "ExerciseSheet.findByName", query = "SELECT e FROM ExerciseSheet e WHERE e.name = :name"),
    @NamedQuery(name = "ExerciseSheet.findByExpirationDate", query = "SELECT e FROM ExerciseSheet e WHERE e.expirationDate = :expirationDate"),
    @NamedQuery(name = "ExerciseSheet.findByReleaseDate", query = "SELECT e FROM ExerciseSheet e WHERE e.releaseDate = :releaseDate"),
    @NamedQuery(name = "ExerciseSheet.findByVisible", query = "SELECT e FROM ExerciseSheet e WHERE e.visible = :visible")})
public class ExerciseSheet implements Serializable {
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
    @Column(name = "expirationDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date expirationDate;
    @Basic(optional = false)
    @Column(name = "release_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date releaseDate;
    @Basic(optional = false)
    @Column(name = "visible")
    private boolean visible;
    @ManyToMany(mappedBy = "exerciseSheetCollection")
    private Collection<Group> GroupCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "exerciseSheet")
    private Collection<ExerciseSheetHasTask> exerciseSheetHasTaskCollection;

    public ExerciseSheet() {
    }

    public ExerciseSheet(Integer id) {
        this.id = id;
    }

    public ExerciseSheet(Integer id, String name, Date expirationDate, Date releaseDate, boolean visible) {
        this.id = id;
        this.name = name;
        this.expirationDate = expirationDate;
        this.releaseDate = releaseDate;
        this.visible = visible;
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

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public boolean getVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    @XmlTransient
    public Collection<Group> getGroupCollection() {
        return GroupCollection;
    }

    public void setGroupCollection(Collection<Group> GroupCollection) {
        this.GroupCollection = GroupCollection;
    }

    @XmlTransient
    public Collection<ExerciseSheetHasTask> getExerciseSheetHasTaskCollection() {
        return exerciseSheetHasTaskCollection;
    }

    public void setExerciseSheetHasTaskCollection(Collection<ExerciseSheetHasTask> exerciseSheetHasTaskCollection) {
        this.exerciseSheetHasTaskCollection = exerciseSheetHasTaskCollection;
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
        if (!(object instanceof ExerciseSheet)) {
            return false;
        }
        ExerciseSheet other = (ExerciseSheet) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "de.phoenix.database.entity.ExerciseSheet[ id=" + id + " ]";
    }
    
}
