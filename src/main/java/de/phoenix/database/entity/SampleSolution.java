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
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table(name = "sampleSolution")
@XmlRootElement
//@formatter:off
@NamedQueries({
    @NamedQuery(name = "SampleSolution.findAll", query = "SELECT s FROM SampleSolution s"),
    @NamedQuery(name = "SampleSolution.findById", query = "SELECT s FROM SampleSolution s WHERE s.id = :id"),
    @NamedQuery(name = "SampleSolution.findByAuthor", query = "SELECT s FROM SampleSolution s WHERE s.author = :author")})
//@formatter:on
public class SampleSolution implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;

    @Basic(optional = false)
    @Column(name = "author")
    private String author;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "sampleSolutionid")
    private List<SampleSolutionFile> sampleSolutionFileList;

    @JoinColumn(name = "lecture_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Lecture lectureId;

    @JoinColumns({@JoinColumn(name = "task_exercise_sheet_pool_id", referencedColumnName = "exercise_sheet_pool_id"), @JoinColumn(name = "task_task_id", referencedColumnName = "task_id")})
    @ManyToOne(optional = false)
    private Task task;

    public SampleSolution() {
    }

    public SampleSolution(Integer id) {
        this.id = id;
    }

    public SampleSolution(Integer id, String author) {
        this.id = id;
        this.author = author;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @XmlTransient
    public List<SampleSolutionFile> getFiles() {
        return sampleSolutionFileList;
    }

    public void setFiles(List<SampleSolutionFile> files) {
        this.sampleSolutionFileList = files;
    }

    public Lecture getLecture() {
        return lectureId;
    }

    public void setLectureId(Lecture lecture) {
        this.lectureId = lecture;
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
        if (!(object instanceof SampleSolution)) {
            return false;
        }
        SampleSolution other = (SampleSolution) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "de.phoenix.database.entity.SampleSolution[ id=" + id + " ]";
    }

}
