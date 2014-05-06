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

package de.phoenix.database.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlTransient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.phoenix.database.entity.util.Convertable;
import de.phoenix.security.user.PermissionTree;
import de.phoenix.security.user.PhoenixUserLevel;

@Entity
@Table(name = "userlevel")
public class Userlevel implements Serializable, Convertable<PhoenixUserLevel> {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @Lob
    @Column(name = "permissionTree")
    private String permissionTree;

    @OneToMany(mappedBy = "userlevelId")
    private List<User> userList;

    @JoinColumn(name = "parent", referencedColumnName = "id")
    @ManyToOne
    private Userlevel parent;

    public Userlevel() {
    }

    public Userlevel(Integer id) {
        this.id = id;
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

    public Userlevel getParent() {
        return parent;
    }

    public void setParent(Userlevel parent) {
        this.parent = parent;
    }

    private final static ObjectMapper JSON_MAPPER = new ObjectMapper();

    @XmlTransient
    public PermissionTree getPermissionTree() {
        try {
            @SuppressWarnings("unchecked")
            List<String> nodes = JSON_MAPPER.readValue(permissionTree, List.class);
            PermissionTree tree = new PermissionTree();
            for (String node : nodes) {
                tree.addNode(node);
            }
            return tree;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setPermissionList(PermissionTree tree) {
        try {
            this.permissionTree = JSON_MAPPER.writeValueAsString(tree.toList());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @XmlTransient
    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
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
        if (!(object instanceof Userlevel)) {
            return false;
        }
        Userlevel other = (Userlevel) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "de.phoenix.database.entity.Userlevel[ id=" + id + " ]";
    }

    @Override
    public PhoenixUserLevel convert() {
        return new PhoenixUserLevel((parent != null ? parent.convert() : null), name, getPermissionTree());
    }

    @Override
    public void copyValues(PhoenixUserLevel phoenixEntity) {
        // TODO Auto-generated method stub

    }

}
