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
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table(name = "taskSubmission")
@XmlRootElement
//@formatter:off
@NamedQueries({
    @NamedQuery(name = "TaskSubmission.findAll", query = "SELECT t FROM TaskSubmission t"),
    @NamedQuery(name = "TaskSubmission.findById", query = "SELECT t FROM TaskSubmission t WHERE t.id = :id"),
    @NamedQuery(name = "TaskSubmission.findByDate", query = "SELECT t FROM TaskSubmission t WHERE t.date = :date")})
//@formatter:on
public class TaskSubmission implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;

    @Basic(optional = false)
    @Column(name = "date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    //@formatter:off
    @JoinTable(name = "taskSubmissionAttachment", joinColumns = {
        @JoinColumn(name = "taskSubmission_id", referencedColumnName = "id")}, inverseJoinColumns = {
        @JoinColumn(name = "attachment_id", referencedColumnName = "id")})
    //@formatter:on
    @ManyToMany
    private List<Attachment> attachmentList;

    //@formatter:off
    @JoinTable(name = "taskSubmissionText", joinColumns = {
        @JoinColumn(name = "taskSubmission_id", referencedColumnName = "id")}, inverseJoinColumns = {
        @JoinColumn(name = "text_id", referencedColumnName = "id")})
    //@formatter:on
    @ManyToMany
    private List<Text> textList;

    @JoinColumn(name = "task", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Task task;

    public TaskSubmission() {
    }

    public TaskSubmission(Integer id) {
        this.id = id;
    }

    public TaskSubmission(Integer id, Date date) {
        this.id = id;
        this.date = date;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @XmlTransient
    public List<Attachment> getAttachments() {
        return attachmentList;
    }

    public void setAttachments(List<Attachment> attachments) {
        this.attachmentList = attachments;
    }

    @XmlTransient
    public List<Text> getTexts() {
        return textList;
    }

    public void setTextList(List<Text> texts) {
        this.textList = texts;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
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
        if (!(object instanceof TaskSubmission)) {
            return false;
        }
        TaskSubmission other = (TaskSubmission) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "de.phoenix.database.entity.TaskSubmission[ id=" + id + " ]";
    }

}
