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

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import de.phoenix.database.entity.util.Convertable;
import de.phoenix.database.entity.util.ConverterUtil;
import de.phoenix.rs.entity.PhoenixAttachment;
import de.phoenix.rs.entity.PhoenixSubmission;
import de.phoenix.rs.entity.PhoenixSubmissionResult;
import de.phoenix.rs.entity.PhoenixSubmissionResult.SubmissionStatus;
import de.phoenix.rs.entity.PhoenixText;
import de.phoenix.submission.SubmissionResult;

@Entity
@Table(name = "taskSubmission")
public class TaskSubmission implements Serializable, Convertable<PhoenixSubmission> {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;

    @Basic(optional = false)
    @Column(name = "date")
    @Type(type = "org.joda.time.contrib.hibernate.PersistentDateTime")
    private DateTime date;

    @Column(name = "status")
    private Integer status;
    @Lob
    @Column(name = "statusText")
    private String statusText;

    //@formatter:off
    @JoinTable(name = "taskSubmissionAttachment", joinColumns = {
        @JoinColumn(name = "taskSubmission_id", referencedColumnName = "id")}, inverseJoinColumns = {
        @JoinColumn(name = "attachment_id", referencedColumnName = "id")})
    //@formatter:on
    @ManyToMany
    @Cascade(CascadeType.SAVE_UPDATE)
    private List<Attachment> attachmentList;

    //@formatter:off
    @JoinTable(name = "taskSubmissionText", joinColumns = {
        @JoinColumn(name = "taskSubmission_id", referencedColumnName = "id")}, inverseJoinColumns = {
        @JoinColumn(name = "text_id", referencedColumnName = "id")})
    //@formatter:on
    @ManyToMany
    @Cascade(CascadeType.SAVE_UPDATE)
    private List<Text> textList;

    @JoinColumn(name = "task", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Task task;

    public TaskSubmission() {
    }

    public TaskSubmission(Integer id) {
        this.id = id;
    }

    public TaskSubmission(Integer id, DateTime date) {
        this.id = id;
        this.date = date;
    }

    public TaskSubmission(int status, String statusText, Task task, List<Attachment> attachments, List<Text> texts) {
        this.task = task;
        this.attachmentList = attachments;
        this.textList = texts;
        this.date = new DateTime();
        this.status = status;
        this.statusText = statusText;
    }

    public TaskSubmission(Task task, PhoenixSubmission phoenixSubmission) {

        this.task = task;
        this.date = new DateTime();

        List<PhoenixAttachment> attachments = phoenixSubmission.getAttachments();
        this.attachmentList = new ArrayList<Attachment>();
        for (PhoenixAttachment phoenixAttachment : attachments) {
            try {
                this.attachmentList.add(new Attachment(phoenixAttachment));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        List<PhoenixText> texts = phoenixSubmission.getTexts();
        this.textList = new ArrayList<Text>();
        for (PhoenixText phoenixText : texts) {
            this.textList.add(new Text(phoenixText));
        }

        this.status = SubmissionStatus.SUBMITTED.ordinal();
        this.statusText = "";
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public DateTime getDate() {
        return date;
    }

    public void setDate(DateTime date) {
        this.date = date;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getStatusText() {
        return statusText;
    }

    public void setStatusText(String statusText) {
        this.statusText = statusText;
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

    public void setSubmissionResult(SubmissionResult result) {
        this.status = result.getStatus().ordinal();
        this.statusText = result.getStatusText();
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

    @Override
    public PhoenixSubmission convert() {
        PhoenixSubmissionResult res = new PhoenixSubmissionResult(SubmissionStatus.values()[getStatus()], statusText);
        return new PhoenixSubmission(getDate(), getTask().convert(), res, ConverterUtil.convert(getAttachments()), ConverterUtil.convert(getTexts()));
    }

    @Override
    public void copyValues(PhoenixSubmission phoenixEntity) {
        this.setStatus(phoenixEntity.getResult().getStatus().ordinal());
        this.setStatusText(phoenixEntity.getResult().getStatusText());
    }

}
