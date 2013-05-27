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
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
//@formatter:on
public class TaskPK implements Serializable {

    private static final long serialVersionUID = 1L;

    @Basic(optional = false)
    @Column(name = "exercise_sheet_pool_id")
    private int exerciseSheetPoolId;

    @Basic(optional = false)
    @Column(name = "task_id")
    private int taskId;

    public TaskPK() {
    }

    public TaskPK(int exerciseSheetPoolId, int taskId) {
        this.exerciseSheetPoolId = exerciseSheetPoolId;
        this.taskId = taskId;
    }

    public int getExerciseSheetPoolId() {
        return exerciseSheetPoolId;
    }

    public void setExerciseSheetPoolId(int exerciseSheetPoolId) {
        this.exerciseSheetPoolId = exerciseSheetPoolId;
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
        hash += (int) exerciseSheetPoolId;
        hash += (int) taskId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are
        // not set
        if (!(object instanceof TaskPK)) {
            return false;
        }
        TaskPK other = (TaskPK) object;
        if (this.exerciseSheetPoolId != other.exerciseSheetPoolId) {
            return false;
        }
        if (this.taskId != other.taskId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "de.phoenix.database.entity.TaskPK[ exerciseSheetPoolId=" + exerciseSheetPoolId + ", taskId=" + taskId + " ]";
    }

}
