/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.phoenix.database.entity;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Meldanor
 */
@Entity
@Table(name = "sample_solution")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SampleSolution.findAll", query = "SELECT s FROM SampleSolution s"),
    @NamedQuery(name = "SampleSolution.findById", query = "SELECT s FROM SampleSolution s WHERE s.id = :id"),
    @NamedQuery(name = "SampleSolution.findByAuthor", query = "SELECT s FROM SampleSolution s WHERE s.author = :author")})
public class SampleSolution implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "author")
    private String author;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "sampleSolutionId")
    private Collection<SampleSolutionFile> sampleSolutionFileCollection;
    @JoinColumn(name = "task_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Task taskId;
    @JoinColumn(name = "lecture_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Lecture lectureId;

    public SampleSolution() {
    }

    public SampleSolution(Integer id) {
        this.id = id;
    }

    public SampleSolution(Integer id, String author) {
        this.id = id;
        this.author = author;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @XmlTransient
    public Collection<SampleSolutionFile> getSampleSolutionFileCollection() {
        return sampleSolutionFileCollection;
    }

    public void setSampleSolutionFileCollection(Collection<SampleSolutionFile> sampleSolutionFileCollection) {
        this.sampleSolutionFileCollection = sampleSolutionFileCollection;
    }

    public Task getTaskId() {
        return taskId;
    }

    public void setTaskId(Task taskId) {
        this.taskId = taskId;
    }

    public Lecture getLectureId() {
        return lectureId;
    }

    public void setLectureId(Lecture lectureId) {
        this.lectureId = lectureId;
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
        if (!(object instanceof SampleSolution)) {
            return false;
        }
        SampleSolution other = (SampleSolution) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "de.phoenix.database.entity.SampleSolution[ id=" + id + " ]";
    }
    
}
