/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.phoenix.database.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author Meldanor
 */
@Embeddable
public class ExerciseSheetHasTaskPK implements Serializable {

    private static final long serialVersionUID = 780111852590263672L;
    @Basic(optional = false)
    @Column(name = "exercise_sheet_id")
    private int exerciseSheetId;
    @Basic(optional = false)
    @Column(name = "task_id")
    private int taskId;

    public ExerciseSheetHasTaskPK() {
    }

    public ExerciseSheetHasTaskPK(int exerciseSheetId, int taskId) {
        this.exerciseSheetId = exerciseSheetId;
        this.taskId = taskId;
    }

    public int getExerciseSheetId() {
        return exerciseSheetId;
    }

    public void setExerciseSheetId(int exerciseSheetId) {
        this.exerciseSheetId = exerciseSheetId;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) exerciseSheetId;
        hash += (int) taskId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ExerciseSheetHasTaskPK)) {
            return false;
        }
        ExerciseSheetHasTaskPK other = (ExerciseSheetHasTaskPK) object;
        if (this.exerciseSheetId != other.exerciseSheetId) {
            return false;
        }
        if (this.taskId != other.taskId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "de.phoenix.database.entity.ExerciseSheetHasTaskPK[ exerciseSheetId=" + exerciseSheetId + ", taskId=" + taskId + " ]";
    }
    
}
