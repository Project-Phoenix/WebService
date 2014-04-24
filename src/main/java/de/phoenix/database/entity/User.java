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
import de.phoenix.security.SaltedPassword;
import de.phoenix.security.user.PhoenixUser;

@Entity
@Table(name = "user")
public class User implements Serializable, Convertable<PhoenixUser> {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;

    @Column(name = "surname")
    private String surname;

    @Column(name = "name")
    private String name;

    @Column(name = "username")
    private String username;

    @Column(name = "mail")
    private String mail;

    @Column(name = "hash")
    private String hash;

    @Column(name = "salt")
    private String salt;

    @JoinColumn(name = "userlevel_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Userlevel userlevelId;

    public User() {
    }

    public User(Integer id) {
        this.id = id;
    }
    
    public User(PhoenixUser pUser, SaltedPassword password) {
        this.hash = password.getHash();
        this.salt = password.getSalt();
        
        this.username = pUser.getUsername();
        this.name = pUser.getName();
        this.surname = pUser.getSurname();
        this.mail= pUser.getMail();
        
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public Userlevel getUserlevelId() {
        return userlevelId;
    }

    public void setUserlevelId(Userlevel userlevelId) {
        this.userlevelId = userlevelId;
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
        if (!(object instanceof User)) {
            return false;
        }
        User other = (User) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "de.phoenix.database.entity.User[ id=" + id + " ]";
    }

    @Override
    public PhoenixUser convert() {
        return new PhoenixUser(surname, name, username, mail, userlevelId != null ? userlevelId.convert() : null);
    }

    @Override
    public void copyValues(PhoenixUser phoenixEntity) {
        this.setName(phoenixEntity.getName());
        this.setUsername(phoenixEntity.getUsername());
        this.setSurname(phoenixEntity.getSurname());
        this.setMail(phoenixEntity.getMail());
    }

}
