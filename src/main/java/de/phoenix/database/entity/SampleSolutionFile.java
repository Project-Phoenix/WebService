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
@Table(name = "sampleSolutionFile")
@XmlRootElement
//@formatter:off
@NamedQueries({
    @NamedQuery(name = "SampleSolutionFile.findAll", query = "SELECT s FROM SampleSolutionFile s"),
    @NamedQuery(name = "SampleSolutionFile.findById", query = "SELECT s FROM SampleSolutionFile s WHERE s.id = :id"),
    @NamedQuery(name = "SampleSolutionFile.findByFileName", query = "SELECT s FROM SampleSolutionFile s WHERE s.fileName = :fileName")})
//@formatter:on
public class SampleSolutionFile implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;

    @Basic(optional = false)
    @Lob
    @Column(name = "content", columnDefinition = "text")
    private String content;

    @Basic(optional = false)
    @Column(name = "fileName")
    private String fileName;

    @JoinColumn(name = "sampleSolution_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private SampleSolution sampleSolutionid;

    public SampleSolutionFile() {
    }

    public SampleSolutionFile(Integer id) {
        this.id = id;
    }

    public SampleSolutionFile(Integer id, String content, String fileName) {
        this.id = id;
        this.content = content;
        this.fileName = fileName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public SampleSolution getSampleSolution() {
        return sampleSolutionid;
    }

    public void setSampleSolution(SampleSolution sampleSolution) {
        this.sampleSolutionid = sampleSolution;
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
        if (!(object instanceof SampleSolutionFile)) {
            return false;
        }
        SampleSolutionFile other = (SampleSolutionFile) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "de.phoenix.database.entity.SampleSolutionFile[ id=" + id + " ]";
    }

}
