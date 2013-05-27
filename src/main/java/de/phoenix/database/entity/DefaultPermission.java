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
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "defaultPermission")
@XmlRootElement
//@formatter:off
@NamedQueries({
    @NamedQuery(name = "DefaultPermission.findAll", query = "SELECT d FROM DefaultPermission d"),
    @NamedQuery(name = "DefaultPermission.findByNode", query = "SELECT d FROM DefaultPermission d WHERE d.node = :node")})
//@formatter:on
public class DefaultPermission implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Basic(optional = false)
    @Column(name = "node")
    private String node;

    @JoinColumn(name = "default_role_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private DefaultRole defaultRoleId;

    public DefaultPermission() {
    }

    public DefaultPermission(String node) {
        this.node = node;
    }

    public String getNode() {
        return node;
    }

    public void setNode(String node) {
        this.node = node;
    }

    public DefaultRole getDefaultRole() {
        return defaultRoleId;
    }

    public void setDefaultRole(DefaultRole defaultRole) {
        this.defaultRoleId = defaultRole;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (node != null ? node.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are
        // not set
        if (!(object instanceof DefaultPermission)) {
            return false;
        }
        DefaultPermission other = (DefaultPermission) object;
        if ((this.node == null && other.node != null) || (this.node != null && !this.node.equals(other.node))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "de.phoenix.database.entity.DefaultPermission[ node=" + node + " ]";
    }

}
