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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.ws.rs.DefaultValue;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.phoenix.database.entity.util.Convertable;
import de.phoenix.database.entity.util.ConverterUtil;
import de.phoenix.rs.entity.PhoenixAttachment;
import de.phoenix.rs.entity.PhoenixAutomaticTask;
import de.phoenix.rs.entity.PhoenixTask;
import de.phoenix.rs.entity.PhoenixTaskTest;
import de.phoenix.rs.entity.PhoenixText;
import de.phoenix.submission.DisallowedContent;

/**
 * Class defining database structure for a task to solve. PhoenixEntity is
 * {@link PhoenixTask} and its inheritated class {@link PhoenixAutomaticTask}
 */
@Entity
@Table(name = "task")
public class Task implements Serializable, Convertable<PhoenixTask> {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;

    @Column(name = "title", unique = true)
    private String title;

    @Lob
    @Column(name = "description")
    private String description;

    @DefaultValue(value = "0")
    @Column(name = "isAutomaticTest")
    private Boolean isAutomaticTest = false;

    @Column(name = "backend")
    private String backend;

    @ManyToMany(mappedBy = "taskList")
    private List<TaskSheet> taskSheetList;

    //@formatter:off
    @JoinTable(name = "taskAttachments", joinColumns = {
            @JoinColumn(name = "task_id", referencedColumnName = "id")}, inverseJoinColumns = {
            @JoinColumn(name = "attachment_id", referencedColumnName = "id")})
    //@formatter:on
    @ManyToMany
    @Cascade(CascadeType.SAVE_UPDATE)
    private List<Attachment> attachmentList;

    //@formatter:off
    @JoinTable(name = "taskPattern", joinColumns = {
            @JoinColumn(name = "task_id", referencedColumnName = "id")}, inverseJoinColumns = {
            @JoinColumn(name = "text_id", referencedColumnName = "id")})
    //@formatter:on
    @ManyToMany
    @Cascade(CascadeType.SAVE_UPDATE)
    private List<Text> textList;

    @OneToMany(mappedBy = "task")
    private List<TaskSubmission> taskSubmissionList;

    @OneToMany(mappedBy = "task")
    private List<TaskSubmissionDates> taskSubmissionDatesList;

    @OneToMany(mappedBy = "task")
    @Cascade(CascadeType.SAVE_UPDATE)
    private List<TaskTest> taskTestList;

    @Lob
    @Column(name = "disallowedContent")
    private String disallowedContent;

    public Task() {
    }

    public Task(Integer id) {
        this.id = id;
    }

    public Task(PhoenixTask phoenixTask) {
        this.title = phoenixTask.getTitle();
        this.description = phoenixTask.getDescription();
        this.setDisallowedContent(phoenixTask.getDisallowedContent());

        List<PhoenixAttachment> phoenixAttachments = phoenixTask.getAttachments();
        this.attachmentList = new ArrayList<Attachment>(phoenixAttachments.size());
        for (PhoenixAttachment phoenixAttachment : phoenixAttachments) {
            try {
                this.attachmentList.add(new Attachment(phoenixAttachment));
            } catch (IOException e) {
                throw new RuntimeException("IO error");
            }
        }

        List<PhoenixText> phoenixTexts = phoenixTask.getPattern();
        this.textList = new ArrayList<Text>(phoenixTexts.size());
        for (PhoenixText phoenixText : phoenixTask.getPattern()) {
            this.textList.add(new Text(phoenixText));
        }

        if (phoenixTask instanceof PhoenixAutomaticTask) {
            PhoenixAutomaticTask autoTask = (PhoenixAutomaticTask) phoenixTask;
            this.backend = autoTask.getBackend();
            this.isAutomaticTest = true;

            List<PhoenixTaskTest> tests = autoTask.getTests();
            this.taskTestList = new ArrayList<TaskTest>(tests.size());
            for (PhoenixTaskTest test : autoTask.getTests()) {
                this.taskTestList.add(new TaskTest(this, test));
            }
        }
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

    public Boolean isAutomaticTest() {
        return isAutomaticTest;
    }

    public void setAutomaticTest(Boolean isAutomaticTest) {
        this.isAutomaticTest = isAutomaticTest;
    }

    public String getBackend() {
        return backend;
    }

    public void setBackend(String backend) {
        this.backend = backend;
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
    public List<TaskTest> getTaskTests() {
        return taskTestList;
    }

    public void setTaskTests(List<TaskTest> taskTests) {
        this.taskTestList = taskTests;
    }

    @XmlTransient
    public List<TaskSubmission> getTaskSubmissions() {
        return taskSubmissionList;
    }

    public void setTaskSubmissions(List<TaskSubmission> taskSubmissions) {
        this.taskSubmissionList = taskSubmissions;
    }

    @XmlTransient
    public List<TaskSubmissionDates> getLectureGroupTaskSheetDates() {
        return taskSubmissionDatesList;
    }

    public void setLectureGroupTaskSheetDates(List<TaskSubmissionDates> taskSubmissionDates) {
        this.taskSubmissionDatesList = taskSubmissionDates;
    }

    private static final ObjectMapper JSON_MAPPER = new ObjectMapper();

    public void setDisallowedContent(DisallowedContent content) {
        try {
            this.disallowedContent = JSON_MAPPER.writeValueAsString(content);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public DisallowedContent getDisallowedContent() {
        try {
            return JSON_MAPPER.readValue(disallowedContent, DisallowedContent.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
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

    @Override
    public PhoenixTask convert() {
        PhoenixTask pTask = new PhoenixTask(ConverterUtil.convert(getAttachments()), ConverterUtil.convert(getTexts()), getDescription(), getTitle());
        pTask.setDisallowedContent(getDisallowedContent());
        return pTask;
    }

    @Override
    public void copyValues(PhoenixTask phoenixEntity) {
        this.setTitle(phoenixEntity.getTitle());
        this.setDescription(phoenixEntity.getDescription());

        if (phoenixEntity instanceof PhoenixAutomaticTask) {
            this.setBackend(((PhoenixAutomaticTask) phoenixEntity).getBackend());
            this.setAutomaticTest(true);
        }
    }
}
