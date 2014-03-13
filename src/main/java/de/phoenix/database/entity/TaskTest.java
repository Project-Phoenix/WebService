/*
 * Copyright (C) 2014 Project-Phoenix
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

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.phoenix.database.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import de.phoenix.database.entity.util.Convertable;
import de.phoenix.rs.entity.PhoenixTaskTest;

@Entity
@Table(name = "taskTest")
public class TaskTest implements Serializable, Convertable<PhoenixTaskTest> {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;

    @Column(name = "timeout")
    private Integer timeout;

    @JoinColumn(name = "text", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Text text;

    @JoinColumn(name = "task", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Task task;

    public TaskTest() {
    }

    public TaskTest(Integer id) {
        this.id = id;
    }

    public TaskTest(PhoenixTaskTest test) {
        this.text = new Text(test.getContent());
        this.timeout = (int) test.getTimeout();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTimeout() {
        return timeout;
    }

    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }

    public Text getText() {
        return text;
    }

    public void setText(Text text) {
        this.text = text;
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
        if (!(object instanceof TaskTest)) {
            return false;
        }
        TaskTest other = (TaskTest) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "de.phoenix.database.entity.TaskTest[ id=" + id + " ]";
    }

    @Override
    public PhoenixTaskTest convert() {
        PhoenixTaskTest pTest = new PhoenixTaskTest(this.text.convert());
        pTest.setTimeout(this.timeout);
        return pTest;
    }

    @Override
    public void copyValues(PhoenixTaskTest phoenixEntity) {
        this.timeout = (int) phoenixEntity.getTimeout();
        this.text = new Text(phoenixEntity.getContent());
    }

}
