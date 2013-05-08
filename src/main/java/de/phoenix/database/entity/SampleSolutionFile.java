/*
 * To change this template, choose Tools | Templates
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
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Meldanor
 */
@Entity
@Table(name = "sample_solution_file")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SampleSolutionFile.findAll", query = "SELECT s FROM SampleSolutionFile s"),
    @NamedQuery(name = "SampleSolutionFile.findById", query = "SELECT s FROM SampleSolutionFile s WHERE s.id = :id"),
    @NamedQuery(name = "SampleSolutionFile.findByType", query = "SELECT s FROM SampleSolutionFile s WHERE s.type = :type")})
public class SampleSolutionFile implements Serializable {
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
    @Column(name = "type")
    private String type;
    @JoinColumn(name = "sample_solution_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private SampleSolution sampleSolutionId;

    public SampleSolutionFile() {
    }

    public SampleSolutionFile(Integer id) {
        this.id = id;
    }

    public SampleSolutionFile(Integer id, String content, String type) {
        this.id = id;
        this.content = content;
        this.type = type;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public SampleSolution getSampleSolutionId() {
        return sampleSolutionId;
    }

    public void setSampleSolutionId(SampleSolution sampleSolutionId) {
        this.sampleSolutionId = sampleSolutionId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
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
