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
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table(name = "instance")
@XmlRootElement
//@formatter:off
@NamedQueries({
    @NamedQuery(name = "Instance.findAll", query = "SELECT i FROM Instance i"),
    @NamedQuery(name = "Instance.findById", query = "SELECT i FROM Instance i WHERE i.id = :id"),
    @NamedQuery(name = "Instance.findByName", query = "SELECT i FROM Instance i WHERE i.name = :name"),
    @NamedQuery(name = "Instance.findByHost", query = "SELECT i FROM Instance i WHERE i.host = :host"),
    @NamedQuery(name = "Instance.findByPort", query = "SELECT i FROM Instance i WHERE i.port = :port")})
//@formatter:on
public class Instance implements Serializable {

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
    @Column(name = "host")
    private String host;

    @Basic(optional = false)
    @Column(name = "port")
    private int port;

    @ManyToMany(mappedBy = "instanceList")
    private List<User> userList;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "instanceId")
    private List<Lecture> lectureList;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "instanceId")
    private List<Role> roleList;

    public Instance() {
    }

    public Instance(Integer id) {
        this.id = id;
    }

    public Instance(Integer id, String name, String host, int port) {
        this.id = id;
        this.name = name;
        this.host = host;
        this.port = port;
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

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @XmlTransient
    public List<User> getInstanceAdmins() {
        return userList;
    }

    public void setInstanceAdmins(List<User> instanceAdmins) {
        this.userList = instanceAdmins;
    }

    @XmlTransient
    public List<Lecture> getLectureList() {
        return lectureList;
    }

    public void setLectureList(List<Lecture> lectureList) {
        this.lectureList = lectureList;
    }

    @XmlTransient
    public List<Role> getRoles() {
        return roleList;
    }

    public void setRoles(List<Role> roles) {
        this.roleList = roles;
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
        if (!(object instanceof Instance)) {
            return false;
        }
        Instance other = (Instance) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "de.phoenix.database.entity.Instance[ id=" + id + " ]";
    }

}
