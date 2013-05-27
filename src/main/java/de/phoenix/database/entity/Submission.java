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
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table(name = "submission")
@XmlRootElement
//@formatter:off
@NamedQueries({
    @NamedQuery(name = "Submission.findAll", query = "SELECT s FROM Submission s"),
    @NamedQuery(name = "Submission.findById", query = "SELECT s FROM Submission s WHERE s.id = :id"),
    @NamedQuery(name = "Submission.findBySubmissionDate", query = "SELECT s FROM Submission s WHERE s.submissionDate = :submissionDate"),
    @NamedQuery(name = "Submission.findByStatus", query = "SELECT s FROM Submission s WHERE s.status = :status"),
    @NamedQuery(name = "Submission.findByControllStatus", query = "SELECT s FROM Submission s WHERE s.controllStatus = :controllStatus")})
//@formatter:on
public class Submission implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;

    @Basic(optional = false)
    @Column(name = "submissionDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date submissionDate;

    @Basic(optional = false)
    @Column(name = "status")
    private int status;

    @Basic(optional = false)
    @Column(name = "controllStatus")
    private int controllStatus;

    @Lob
    @Column(name = "controllMessage", columnDefinition = "text")
    private String controllMessage;

    @JoinColumns({@JoinColumn(name = "exercise_sheet_exercise_sheet_id", referencedColumnName = "exercise_sheet_id"), @JoinColumn(name = "exercise_sheet_group_id", referencedColumnName = "group_id")})
    @ManyToOne(optional = false)
    private ExerciseSheet exerciseSheet;

    @JoinColumns({@JoinColumn(name = "task_exercise_sheet_pool_id", referencedColumnName = "exercise_sheet_pool_id"), @JoinColumn(name = "task_task_id", referencedColumnName = "task_id")})
    @ManyToOne(optional = false)
    private Task task;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "submissionId")
    private List<SubmissionFiles> submissionFilesList;

    public Submission() {
    }

    public Submission(Integer id) {
        this.id = id;
    }

    public Submission(Integer id, Date submissionDate, int status, int controllStatus) {
        this.id = id;
        this.submissionDate = submissionDate;
        this.status = status;
        this.controllStatus = controllStatus;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getSubmissionDate() {
        return submissionDate;
    }

    public void setSubmissionDate(Date submissionDate) {
        this.submissionDate = submissionDate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getControllStatus() {
        return controllStatus;
    }

    public void setControllStatus(int controllStatus) {
        this.controllStatus = controllStatus;
    }

    public String getControllMessage() {
        return controllMessage;
    }

    public void setControllMessage(String controllMessage) {
        this.controllMessage = controllMessage;
    }

    public ExerciseSheet getExerciseSheet() {
        return exerciseSheet;
    }

    public void setExerciseSheet(ExerciseSheet exerciseSheet) {
        this.exerciseSheet = exerciseSheet;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    @XmlTransient
    public List<SubmissionFiles> getSubmissionFilesList() {
        return submissionFilesList;
    }

    public void setSubmissionFilesList(List<SubmissionFiles> submissionFilesList) {
        this.submissionFilesList = submissionFilesList;
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
