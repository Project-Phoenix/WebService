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
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table(name = "automaticTask")
@XmlRootElement
//@formatter:off
@NamedQueries({
    @NamedQuery(name = "AutomaticTask.findAll", query = "SELECT a FROM AutomaticTask a"),
    @NamedQuery(name = "AutomaticTask.findById", query = "SELECT a FROM AutomaticTask a WHERE a.id = :id"),
    @NamedQuery(name = "AutomaticTask.findByBackend", query = "SELECT a FROM AutomaticTask a WHERE a.backend = :backend")})
//@formatter:on
public class AutomaticTask implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;

    @Column(name = "backend")
    private String backend;

    //@formatter:off
    @JoinTable(name = "automaticTaskTests", joinColumns = {
            @JoinColumn(name = "automaticTask_id", referencedColumnName = "id")}, inverseJoinColumns = {
            @JoinColumn(name = "test_id", referencedColumnName = "id")})
    //@formatter:on
    @ManyToMany
    private List<Test> testList;

    @JoinColumn(name = "id", referencedColumnName = "id", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Task task;

    public AutomaticTask() {
    }

    public AutomaticTask(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBackend() {
        return backend;
    }

    public void setBackend(String backend) {
        this.backend = backend;
    }

    @XmlTransient
    public List<Test> getTests() {
        return testList;
    }

    public void setTests(List<Test> tests) {
        this.testList = tests;
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
        if (!(object instanceof AutomaticTask)) {
            return false;
        }
        AutomaticTask other = (AutomaticTask) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "de.phoenix.database.entityt.AutomaticTask[ id=" + id + " ]";
    }

}
