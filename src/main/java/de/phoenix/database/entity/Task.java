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
import javax.persistence.Lob;
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
@Table(name = "task")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Task.findAll", query = "SELECT t FROM Task t"),
    @NamedQuery(name = "Task.findById", query = "SELECT t FROM Task t WHERE t.id = :id"),
    @NamedQuery(name = "Task.findByName", query = "SELECT t FROM Task t WHERE t.name = :name"),
    @NamedQuery(name = "Task.findByAutoControl", query = "SELECT t FROM Task t WHERE t.autoControl = :autoControl"),
    @NamedQuery(name = "Task.findByExpirationDate", query = "SELECT t FROM Task t WHERE t.expirationDate = :expirationDate")})
public class Task implements Serializable {
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
    @Column(name = "autoControl")
    private String autoControl;
    @Basic(optional = false)
    @Lob
    @Column(name = "text", columnDefinition = "text")
    private String text;
    @Basic(optional = false)
    @Column(name = "expirationDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date expirationDate;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "task")
    private Collection<ExerciseSheetHasTask> exerciseSheetHasTaskCollection;

    public Task() {
    }

    public Task(Integer id) {
        this.id = id;
    }

    public Task(Integer id, String name, String autoControl, String text, Date expirationDate) {
        this.id = id;
        this.name = name;
        this.autoControl = autoControl;
        this.text = text;
        this.expirationDate = expirationDate;
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

    public String getAutoControl() {
        return autoControl;
    }

    public void setAutoControl(String autoControl) {
        this.autoControl = autoControl;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
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
        if (!(object instanceof Task)) {
            return false;
        }
        Task other = (Task) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "de.phoenix.database.entity.Task[ id=" + id + " ]";
    }
    
}
