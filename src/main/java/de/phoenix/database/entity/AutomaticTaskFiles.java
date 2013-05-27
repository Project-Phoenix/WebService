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
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "automaticTaskFiles")
@XmlRootElement
//@formatter:off
@NamedQueries({
    @NamedQuery(name = "AutomaticTaskFiles.findAll", query = "SELECT a FROM AutomaticTaskFiles a"),
    @NamedQuery(name = "AutomaticTaskFiles.findById", query = "SELECT a FROM AutomaticTaskFiles a WHERE a.id = :id")})
//@formatter:on
public class AutomaticTaskFiles implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;

    @Basic(optional = false)
    @Lob
    @Column(name = "text", columnDefinition = "text")
    private String text;

    @Lob
    @Column(name = "unitTest", columnDefinition = "text")
    private String unitTest;

    @JoinColumn(name = "automaticTask_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private AutomaticTask automaticTaskid;

    public AutomaticTaskFiles() {
    }

    public AutomaticTaskFiles(Integer id) {
        this.id = id;
    }

    public AutomaticTaskFiles(Integer id, String text) {
        this.id = id;
        this.text = text;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUnitTest() {
        return unitTest;
    }

    public void setUnitTest(String unitTest) {
        this.unitTest = unitTest;
    }

    public AutomaticTask getAutomaticTask() {
        return automaticTaskid;
    }

    public void setAutomaticTask(AutomaticTask automaticTask) {
        this.automaticTaskid = automaticTask;
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
        if (!(object instanceof AutomaticTaskFiles)) {
            return false;
        }
        AutomaticTaskFiles other = (AutomaticTaskFiles) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "de.phoenix.database.entity.AutomaticTaskFiles[ id=" + id + " ]";
    }

}
