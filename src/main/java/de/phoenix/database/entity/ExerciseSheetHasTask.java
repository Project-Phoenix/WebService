/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.phoenix.database.entity;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
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
@Table(name = "exercise_sheet_has_task")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ExerciseSheetHasTask.findAll", query = "SELECT e FROM ExerciseSheetHasTask e"),
    @NamedQuery(name = "ExerciseSheetHasTask.findByExerciseSheetId", query = "SELECT e FROM ExerciseSheetHasTask e WHERE e.exerciseSheetHasTaskPK.exerciseSheetId = :exerciseSheetId"),
    @NamedQuery(name = "ExerciseSheetHasTask.findByTaskId", query = "SELECT e FROM ExerciseSheetHasTask e WHERE e.exerciseSheetHasTaskPK.taskId = :taskId")})
public class ExerciseSheetHasTask implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ExerciseSheetHasTaskPK exerciseSheetHasTaskPK;
    @ManyToMany(mappedBy = "exerciseSheetHasTaskCollection")
    private Collection<Submission> submissionCollection;
    @JoinColumn(name = "task_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Task task;
    @JoinColumn(name = "exercise_sheet_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private ExerciseSheet exerciseSheet;

    public ExerciseSheetHasTask() {
    }

    public ExerciseSheetHasTask(ExerciseSheetHasTaskPK exerciseSheetHasTaskPK) {
        this.exerciseSheetHasTaskPK = exerciseSheetHasTaskPK;
    }

    public ExerciseSheetHasTask(int exerciseSheetId, int taskId) {
        this.exerciseSheetHasTaskPK = new ExerciseSheetHasTaskPK(exerciseSheetId, taskId);
    }

    public ExerciseSheetHasTaskPK getExerciseSheetHasTaskPK() {
        return exerciseSheetHasTaskPK;
    }

    public void setExerciseSheetHasTaskPK(ExerciseSheetHasTaskPK exerciseSheetHasTaskPK) {
        this.exerciseSheetHasTaskPK = exerciseSheetHasTaskPK;
    }

    @XmlTransient
    public Collection<Submission> getSubmissionCollection() {
        return submissionCollection;
    }

    public void setSubmissionCollection(Collection<Submission> submissionCollection) {
        this.submissionCollection = submissionCollection;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public ExerciseSheet getExerciseSheet() {
        return exerciseSheet;
    }

    public void setExerciseSheet(ExerciseSheet exerciseSheet) {
        this.exerciseSheet = exerciseSheet;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (exerciseSheetHasTaskPK != null ? exerciseSheetHasTaskPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ExerciseSheetHasTask)) {
            return false;
        }
        ExerciseSheetHasTask other = (ExerciseSheetHasTask) object;
        if ((this.exerciseSheetHasTaskPK == null && other.exerciseSheetHasTaskPK != null) || (this.exerciseSheetHasTaskPK != null && !this.exerciseSheetHasTaskPK.equals(other.exerciseSheetHasTaskPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "de.phoenix.database.entity.ExerciseSheetHasTask[ exerciseSheetHasTaskPK=" + exerciseSheetHasTaskPK + " ]";
    }
    
}
