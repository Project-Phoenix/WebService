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
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table(name = "exerciseSheetPool")
@XmlRootElement
//@formatter:off
@NamedQueries({
    @NamedQuery(name = "ExerciseSheetPool.findAll", query = "SELECT e FROM ExerciseSheetPool e"),
    @NamedQuery(name = "ExerciseSheetPool.findById", query = "SELECT e FROM ExerciseSheetPool e WHERE e.id = :id"),
    @NamedQuery(name = "ExerciseSheetPool.findByName", query = "SELECT e FROM ExerciseSheetPool e WHERE e.name = :name")})
//@formatter:on
public class ExerciseSheetPool implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;

    @Basic(optional = false)
    @Column(name = "name")
    private String name;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "exerciseSheetPool")
    private List<Task> taskList;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "exerciseSheetPool")
    private List<ExerciseSheet> exerciseSheetList;

    public ExerciseSheetPool() {
    }

    public ExerciseSheetPool(Integer id) {
        this.id = id;
    }

    public ExerciseSheetPool(Integer id, String name) {
        this.id = id;
        this.name = name;
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

    @XmlTransient
    public List<Task> getTaskList() {
        return taskList;
    }

    public void setTaskList(List<Task> taskList) {
        this.taskList = taskList;
    }

    @XmlTransient
    public List<ExerciseSheet> getExerciseSheetList() {
        return exerciseSheetList;
    }

    public void setExerciseSheetList(List<ExerciseSheet> exerciseSheetList) {
        this.exerciseSheetList = exerciseSheetList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are
        // not set
        if (!(object instanceof ExerciseSheetPool)) {
            return false;
        }
        ExerciseSheetPool other = (ExerciseSheetPool) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "de.phoenix.database.entity.ExerciseSheetPool[ id=" + id + " ]";
    }

}
