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
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table(name = "task")
@XmlRootElement
//@formatter:off
@NamedQueries({
    @NamedQuery(name = "Task.findAll", query = "SELECT t FROM Task t"),
    @NamedQuery(name = "Task.findById", query = "SELECT t FROM Task t WHERE t.id = :id"),
    @NamedQuery(name = "Task.findByTitle", query = "SELECT t FROM Task t WHERE t.title = :title")})
//@formatter:on
public class Task implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;

    @Column(name = "title")
    private String title;

    @Lob
    @Column(name = "description")
    private String description;

    @ManyToMany(mappedBy = "taskList")
    private List<TaskSheet> taskSheetList;

    //@formatter:off
    @JoinTable(name = "taskAttachments", joinColumns = {
            @JoinColumn(name = "task_id", referencedColumnName = "id")}, inverseJoinColumns = {
            @JoinColumn(name = "attachment_id", referencedColumnName = "id")})
    //@formatter:on
    @ManyToMany
    private List<Attachment> attachmentList;

    //@formatter:off
    @JoinTable(name = "taskPattern", joinColumns = {
            @JoinColumn(name = "task_id", referencedColumnName = "id")}, inverseJoinColumns = {
            @JoinColumn(name = "text_id", referencedColumnName = "id")})
    //@formatter:on
    @ManyToMany
    private List<Text> textList;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "task")
    private List<TaskSubmission> taskSubmissionList;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "task")
    private AutomaticTask automaticTask;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "task")
    private List<LectureGroupTaskSheetDates> lectureGroupTaskSheetDatesList;

    public Task() {
    }

    public Task(Integer id) {
        this.id = id;
    }

    public Task(String title, String description, List<Attachment> attachmentList, List<Text> textList) {
        this.title = title;
        this.description = description;
        this.attachmentList = attachmentList;
        this.textList = textList;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @XmlTransient
    public List<TaskSheet> getTaskSheets() {
        return taskSheetList;
    }

    public void setTaskSheets(List<TaskSheet> taskSheets) {
        this.taskSheetList = taskSheets;
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

    public void setTexts(List<Text> texts) {
        this.textList = texts;
    }

    @XmlTransient
    public List<TaskSubmission> getTaskSubmissions() {
        return taskSubmissionList;
    }

    public void setTaskSubmissions(List<TaskSubmission> taskSubmissions) {
        this.taskSubmissionList = taskSubmissions;
    }

    public AutomaticTask getAutomaticTask() {
        return automaticTask;
    }

    public void setAutomaticTask(AutomaticTask automaticTask) {
        this.automaticTask = automaticTask;
    }

    @XmlTransient
    public List<LectureGroupTaskSheetDates> getLectureGroupTaskSheetDates() {
        return lectureGroupTaskSheetDatesList;
    }

    public void setLectureGroupTaskSheetDates(List<LectureGroupTaskSheetDates> lectureGroupTaskSheetDates) {
        this.lectureGroupTaskSheetDatesList = lectureGroupTaskSheetDates;
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
        if (!(object instanceof Task)) {
            return false;
        }
        Task other = (Task) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "de.phoenix.database.entityt.Task[ id=" + id + " ]";
    }

}
