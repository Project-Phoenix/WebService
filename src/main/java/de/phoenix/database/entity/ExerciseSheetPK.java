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
public class ExerciseSheetPK implements Serializable {

    private static final long serialVersionUID = 1L;

    @Basic(optional = false)
    @Column(name = "group_id")
    private int groupId;

    @Basic(optional = false)
    @Column(name = "exercise_sheet_id")
    private int exerciseSheetId;

    public ExerciseSheetPK() {
    }

    public ExerciseSheetPK(int groupId, int exerciseSheetId) {
        this.groupId = groupId;
        this.exerciseSheetId = exerciseSheetId;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public int getExerciseSheetId() {
        return exerciseSheetId;
    }

    public void setExerciseSheetId(int exerciseSheetId) {
        this.exerciseSheetId = exerciseSheetId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) groupId;
        hash += (int) exerciseSheetId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are
        // not set
        if (!(object instanceof ExerciseSheetPK)) {
            return false;
        }
        ExerciseSheetPK other = (ExerciseSheetPK) object;
        if (this.groupId != other.groupId) {
            return false;
        }
        if (this.exerciseSheetId != other.exerciseSheetId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "de.phoenix.database.entity.ExerciseSheetPK[ groupId=" + groupId + ", exerciseSheetId=" + exerciseSheetId + " ]";
    }

}
