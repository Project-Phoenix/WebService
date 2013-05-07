/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.phoenix.database.entity;

import java.io.Serializable;
import java.util.Collection;
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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Meldanor
 */
@Entity
@Table(name = "submission")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Submission.findAll", query = "SELECT s FROM Submission s"),
    @NamedQuery(name = "Submission.findById", query = "SELECT s FROM Submission s WHERE s.id = :id"),
    @NamedQuery(name = "Submission.findByStatus", query = "SELECT s FROM Submission s WHERE s.status = :status"),
    @NamedQuery(name = "Submission.findByControlStatus", query = "SELECT s FROM Submission s WHERE s.controlStatus = :controlStatus"),
    @NamedQuery(name = "Submission.findBySampleSolution", query = "SELECT s FROM Submission s WHERE s.sampleSolution = :sampleSolution")})
public class Submission implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "status")
    private int status;
    @Basic(optional = false)
    @Column(name = "controlStatus")
    private int controlStatus;
    @Basic(optional = false)
    @Column(name = "sampleSolution")
    private boolean sampleSolution;
    @JoinTable(name = "submission_for_task", joinColumns = {
        @JoinColumn(name = "submission_id", referencedColumnName = "id")}, inverseJoinColumns = {
        @JoinColumn(name = "exercise_sheet_has_task_exercise_sheet_id", referencedColumnName = "exercise_sheet_id"),
        @JoinColumn(name = "exercise_sheet_has_task_task_id", referencedColumnName = "task_id")})
    @ManyToMany
    private Collection<ExerciseSheetHasTask> exerciseSheetHasTaskCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "submissionId")
    private Collection<Files> filesCollection;

    public Submission() {
    }

    public Submission(Integer id) {
        this.id = id;
    }

    public Submission(Integer id, int status, int controlStatus, boolean sampleSolution) {
        this.id = id;
        this.status = status;
        this.controlStatus = controlStatus;
        this.sampleSolution = sampleSolution;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getControlStatus() {
        return controlStatus;
    }

    public void setControlStatus(int controlStatus) {
        this.controlStatus = controlStatus;
    }

    public boolean getSampleSolution() {
        return sampleSolution;
    }

    public void setSampleSolution(boolean sampleSolution) {
        this.sampleSolution = sampleSolution;
    }

    @XmlTransient
    public Collection<ExerciseSheetHasTask> getExerciseSheetHasTaskCollection() {
        return exerciseSheetHasTaskCollection;
    }

    public void setExerciseSheetHasTaskCollection(Collection<ExerciseSheetHasTask> exerciseSheetHasTaskCollection) {
        this.exerciseSheetHasTaskCollection = exerciseSheetHasTaskCollection;
    }

    @XmlTransient
    public Collection<Files> getFilesCollection() {
        return filesCollection;
    }

    public void setFilesCollection(Collection<Files> filesCollection) {
        this.filesCollection = filesCollection;
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
        if (!(object instanceof Submission)) {
            return false;
        }
        Submission other = (Submission) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "de.phoenix.database.entity.Submission[ id=" + id + " ]";
    }
    
}
