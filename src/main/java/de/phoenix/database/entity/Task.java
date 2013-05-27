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
import javax.persistence.CascadeType;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table(name = "task")
@XmlRootElement
//@formatter:off
@NamedQueries({
    @NamedQuery(name = "Task.findAll", query = "SELECT t FROM Task t"),
    @NamedQuery(name = "Task.findByExerciseSheetPoolId", query = "SELECT t FROM Task t WHERE t.taskPK.exerciseSheetPoolId = :exerciseSheetPoolId"),
    @NamedQuery(name = "Task.findByTaskId", query = "SELECT t FROM Task t WHERE t.taskPK.taskId = :taskId")})
//@formatter:on
public class Task implements Serializable {

    private static final long serialVersionUID = 1L;

    @EmbeddedId
    protected TaskPK taskPK;

    @JoinColumn(name = "task_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private TaskPool taskPool;

    @JoinColumn(name = "exercise_sheet_pool_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private ExerciseSheetPool exerciseSheetPool;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "task")
    private List<SampleSolution> sampleSolutionList;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "task")
    private List<Submission> submissionList;

    public Task() {
    }

    public Task(TaskPK taskPK) {
        this.taskPK = taskPK;
    }

    public Task(int exerciseSheetPoolId, int taskId) {
        this.taskPK = new TaskPK(exerciseSheetPoolId, taskId);
    }

    public TaskPK getTaskPK() {
        return taskPK;
    }

    public void setTaskPK(TaskPK taskPK) {
        this.taskPK = taskPK;
    }

    public TaskPool getTaskPool() {
        return taskPool;
    }

    public void setTaskPool(TaskPool taskPool) {
        this.taskPool = taskPool;
    }

    public ExerciseSheetPool getExerciseSheetPool() {
        return exerciseSheetPool;
    }

    public void setExerciseSheetPool(ExerciseSheetPool exerciseSheetPool) {
        this.exerciseSheetPool = exerciseSheetPool;
    }

    @XmlTransient
    public List<SampleSolution> getSampleSolutions() {
        return sampleSolutionList;
    }

    public void setSampleSolutions(List<SampleSolution> sampleSolutions) {
        this.sampleSolutionList = sampleSolutions;
    }

    @XmlTransient
    public List<Submission> getSubmissions() {
        return submissionList;
    }

    public void setSubmissions(List<Submission> submissions) {
        this.submissionList = submissions;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (taskPK != null ? taskPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are
        // not set
        if (!(object instanceof Task)) {
            return false;
        }
        Task other = (Task) object;
        if ((this.taskPK == null && other.taskPK != null) || (this.taskPK != null && !this.taskPK.equals(other.taskPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "de.phoenix.database.entity.Task[ taskPK=" + taskPK + " ]";
    }

}
