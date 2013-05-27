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
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table(name = "taskPool")
@XmlRootElement
//@formatter:off
@NamedQueries({
    @NamedQuery(name = "TaskPool.findAll", query = "SELECT t FROM TaskPool t"),
    @NamedQuery(name = "TaskPool.findById", query = "SELECT t FROM TaskPool t WHERE t.id = :id"),
    @NamedQuery(name = "TaskPool.findByName", query = "SELECT t FROM TaskPool t WHERE t.name = :name")})
//@formatter:on
public class TaskPool implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;

    @Basic(optional = false)
    @Column(name = "name")
    private String name;

    @Basic(optional = false)
    @Lob
    @Column(name = "description", columnDefinition = "text")
    private String description;

    @JoinTable(name = "task_has_tag", joinColumns = {@JoinColumn(name = "task_id", referencedColumnName = "id")}, inverseJoinColumns = {@JoinColumn(name = "tag_id", referencedColumnName = "id")})
    @ManyToMany
    private List<Tag> tagList;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "taskPool")
    private List<Task> taskList;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "taskPoolid")
    private List<AutomaticTask> automaticTaskList;

    public TaskPool() {
    }

    public TaskPool(Integer id) {
        this.id = id;
    }

    public TaskPool(Integer id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @XmlTransient
    public List<Tag> getTags() {
        return tagList;
    }

    public void setTags(List<Tag> tags) {
        this.tagList = tags;
    }

    @XmlTransient
    public List<Task> getTasks() {
        return taskList;
    }

    public void setTasks(List<Task> tasks) {
        this.taskList = tasks;
    }

    @XmlTransient
    public List<AutomaticTask> getAutomaticTasks() {
        return automaticTaskList;
    }

    public void setAutomaticTaskList(List<AutomaticTask> automaticTasks) {
        this.automaticTaskList = automaticTasks;
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
        if (!(object instanceof TaskPool)) {
            return false;
        }
        TaskPool other = (TaskPool) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "de.phoenix.database.entity.TaskPool[ id=" + id + " ]";
    }

}
