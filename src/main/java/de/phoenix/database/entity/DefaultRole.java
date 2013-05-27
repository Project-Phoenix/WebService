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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table(name = "defaultRole")
@XmlRootElement
//@formatter:off
@NamedQueries({
    @NamedQuery(name = "DefaultRole.findAll", query = "SELECT d FROM DefaultRole d"),
    @NamedQuery(name = "DefaultRole.findById", query = "SELECT d FROM DefaultRole d WHERE d.id = :id"),
    @NamedQuery(name = "DefaultRole.findByName", query = "SELECT d FROM DefaultRole d WHERE d.name = :name"),
    @NamedQuery(name = "DefaultRole.findByInheritatedRole", query = "SELECT d FROM DefaultRole d WHERE d.inheritatedRole = :inheritatedRole")})
//@formatter:on
public class DefaultRole implements Serializable {

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
    @Column(name = "inheritatedRole")
    private int inheritatedRole;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "defaultRoleId")
    private List<DefaultPermission> defaultPermissionList;

    public DefaultRole() {
    }

    public DefaultRole(Integer id) {
        this.id = id;
    }

    public DefaultRole(Integer id, String name, int inheritatedRole) {
        this.id = id;
        this.name = name;
        this.inheritatedRole = inheritatedRole;
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

    public int getInheritatedRole() {
        return inheritatedRole;
    }

    public void setInheritatedRole(int inheritatedRole) {
        this.inheritatedRole = inheritatedRole;
    }

    @XmlTransient
    public List<DefaultPermission> getDefaultPermissions() {
        return defaultPermissionList;
    }

    public void setDefaultPermissions(List<DefaultPermission> defaultPermissions) {
        this.defaultPermissionList = defaultPermissions;
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
        if (!(object instanceof DefaultRole)) {
            return false;
        }
        DefaultRole other = (DefaultRole) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "de.phoenix.database.entity.DefaultRole[ id=" + id + " ]";
    }

}
