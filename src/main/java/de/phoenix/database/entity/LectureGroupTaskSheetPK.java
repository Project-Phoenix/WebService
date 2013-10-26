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
public class LectureGroupTaskSheetPK implements Serializable {

    private static final long serialVersionUID = 3866708767624507052L;

    @Basic(optional = false)
    @Column(name = "group")
    private int group;
    
    @Basic(optional = false)
    @Column(name = "taskSheet")
    private int taskSheet;

    public LectureGroupTaskSheetPK() {
    }

    public LectureGroupTaskSheetPK(int group, int taskSheet) {
        this.group = group;
        this.taskSheet = taskSheet;
    }

    public int getGroup() {
        return group;
    }

    public void setGroup(int group) {
        this.group = group;
    }

    public int getTaskSheet() {
        return taskSheet;
    }

    public void setTaskSheet(int taskSheet) {
        this.taskSheet = taskSheet;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) group;
        hash += (int) taskSheet;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are
        // not set
        if (!(object instanceof LectureGroupTaskSheetPK)) {
            return false;
        }
        LectureGroupTaskSheetPK other = (LectureGroupTaskSheetPK) object;
        if (this.group != other.group) {
            return false;
        }
        if (this.taskSheet != other.taskSheet) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "de.phoenix.database.entityt.LectureGroupTaskSheetPK[ group=" + group + ", taskSheet=" + taskSheet + " ]";
    }

}
