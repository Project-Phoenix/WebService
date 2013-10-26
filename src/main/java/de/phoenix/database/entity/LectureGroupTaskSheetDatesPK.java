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
public class LectureGroupTaskSheetDatesPK implements Serializable {

    private static final long serialVersionUID = 6623354243797397148L;

    @Basic(optional = false)
    @Column(name = "groupTaskSheet_group")
    private int groupTaskSheetgroup;

    @Basic(optional = false)
    @Column(name = "groupTaskSheet_taskSheet")
    private int groupTaskSheettaskSheet;

    @Basic(optional = false)
    @Column(name = "task_id")
    private int taskId;

    public LectureGroupTaskSheetDatesPK() {
    }

    public LectureGroupTaskSheetDatesPK(int groupTaskSheetgroup, int groupTaskSheettaskSheet, int taskId) {
        this.groupTaskSheetgroup = groupTaskSheetgroup;
        this.groupTaskSheettaskSheet = groupTaskSheettaskSheet;
        this.taskId = taskId;
    }

    public int getGroupTaskSheetgroup() {
        return groupTaskSheetgroup;
    }

    public void setGroupTaskSheetgroup(int groupTaskSheetgroup) {
        this.groupTaskSheetgroup = groupTaskSheetgroup;
    }

    public int getGroupTaskSheettaskSheet() {
        return groupTaskSheettaskSheet;
    }

    public void setGroupTaskSheettaskSheet(int groupTaskSheettaskSheet) {
        this.groupTaskSheettaskSheet = groupTaskSheettaskSheet;
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
        hash += (int) groupTaskSheetgroup;
        hash += (int) groupTaskSheettaskSheet;
        hash += (int) taskId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are
        // not set
        if (!(object instanceof LectureGroupTaskSheetDatesPK)) {
            return false;
        }
        LectureGroupTaskSheetDatesPK other = (LectureGroupTaskSheetDatesPK) object;
        if (this.groupTaskSheetgroup != other.groupTaskSheetgroup) {
            return false;
        }
        if (this.groupTaskSheettaskSheet != other.groupTaskSheettaskSheet) {
            return false;
        }
        if (this.taskId != other.taskId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "de.phoenix.database.entityt.LectureGroupTaskSheetDatesPK[ groupTaskSheetgroup=" + groupTaskSheetgroup + ", groupTaskSheettaskSheet=" + groupTaskSheettaskSheet + ", taskId=" + taskId + " ]";
    }

}
