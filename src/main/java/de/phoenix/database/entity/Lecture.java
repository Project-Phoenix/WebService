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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import de.phoenix.database.entity.util.Convertable;
import de.phoenix.database.entity.util.ConverterUtil;
import de.phoenix.rs.entity.PhoenixLecture;

@Entity
@Table(name = "lecture")
@XmlRootElement
//@formatter:off
@NamedQueries({
    @NamedQuery(name = "Lecture.findAll", query = "SELECT l FROM Lecture l"),
    @NamedQuery(name = "Lecture.findById", query = "SELECT l FROM Lecture l WHERE l.id = :id"),
    @NamedQuery(name = "Lecture.findByName", query = "SELECT l FROM Lecture l WHERE l.name = :name")})
//@formatter:on
public class Lecture implements Serializable, Convertable<PhoenixLecture> {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "lecture")
    @Cascade(CascadeType.SAVE_UPDATE)
    private List<LectureGroup> lectureGroupList;

    //@formatter:off
    @JoinTable(name = "lectureDetails", joinColumns = {
            @JoinColumn(name = "lecture_id", referencedColumnName = "id")}, inverseJoinColumns = {
            @JoinColumn(name = "additionalInfo_id", referencedColumnName = "id")})
    //@formatter:on
    @ManyToMany
    @Cascade(CascadeType.SAVE_UPDATE)
    private List<Details> detailsList;

    public Lecture() {
    }

    public Lecture(Integer id) {
        this.id = id;
    }

    public Lecture(PhoenixLecture lecture) {
        this.name = lecture.getTitle();
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

    @XmlTransient
    public List<LectureGroup> getLectureGroups() {
        return lectureGroupList;
    }

    public void setLectureGroups(List<LectureGroup> lectureGroups) {
        this.lectureGroupList = lectureGroups;
    }

    @XmlTransient
    public List<Details> getDetails() {
        return detailsList;
    }

    public void setDetails(List<Details> details) {
        this.detailsList = details;
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
        if (!(object instanceof Lecture)) {
            return false;
        }
        Lecture other = (Lecture) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "de.phoenix.database.entityt.Lecture[ id=" + id + " ]";
    }

    @Override
    public PhoenixLecture convert() {
        return new PhoenixLecture(getName(), ConverterUtil.convert(getDetails()));
    }
}
