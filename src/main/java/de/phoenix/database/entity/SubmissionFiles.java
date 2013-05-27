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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "submissionFiles")
@XmlRootElement
//@formatter:off
@NamedQueries({
    @NamedQuery(name = "SubmissionFiles.findAll", query = "SELECT s FROM SubmissionFiles s"),
    @NamedQuery(name = "SubmissionFiles.findById", query = "SELECT s FROM SubmissionFiles s WHERE s.id = :id"),
    @NamedQuery(name = "SubmissionFiles.findByFilename", query = "SELECT s FROM SubmissionFiles s WHERE s.filename = :filename")})
//@formatter:on
public class SubmissionFiles implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;

    @Basic(optional = false)
    @Lob
    @Column(name = "content", columnDefinition = "text")
    private String content;

    @Basic(optional = false)
    @Column(name = "filename")
    private String filename;

    @JoinColumn(name = "submission_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Submission submissionId;

    public SubmissionFiles() {
    }

    public SubmissionFiles(Integer id) {
        this.id = id;
    }

    public SubmissionFiles(Integer id, String content, String filename) {
        this.id = id;
        this.content = content;
        this.filename = filename;
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

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public Submission getSubmission() {
        return submissionId;
    }

    public void setSubmission(Submission submission) {
        this.submissionId = submission;
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
        if (!(object instanceof SubmissionFiles)) {
            return false;
        }
        SubmissionFiles other = (SubmissionFiles) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "de.phoenix.database.entity.SubmissionFiles[ id=" + id + " ]";
    }

}
