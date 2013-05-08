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
@Table(name = "automatic_task")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AutomaticTask.findAll", query = "SELECT a FROM AutomaticTask a"),
    @NamedQuery(name = "AutomaticTask.findById", query = "SELECT a FROM AutomaticTask a WHERE a.id = :id"),
    @NamedQuery(name = "AutomaticTask.findByBackend", query = "SELECT a FROM AutomaticTask a WHERE a.backend = :backend")})
public class AutomaticTask implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "backend")
    private String backend;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "automaticTaskId")
    private Collection<AutomaticTestTestFile> automaticTestTestFileCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "automaticTaskId")
    private Collection<AutomaticTaskTemplate> automaticTaskTemplateCollection;
    @JoinColumn(name = "task_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Task taskId;

    public AutomaticTask() {
    }

    public AutomaticTask(Integer id) {
        this.id = id;
    }

    public AutomaticTask(Integer id, String backend) {
        this.id = id;
        this.backend = backend;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBackend() {
        return backend;
    }

    public void setBackend(String backend) {
        this.backend = backend;
    }

    @XmlTransient
    public Collection<AutomaticTestTestFile> getAutomaticTestTestFileCollection() {
        return automaticTestTestFileCollection;
    }

    public void setAutomaticTestTestFileCollection(Collection<AutomaticTestTestFile> automaticTestTestFileCollection) {
        this.automaticTestTestFileCollection = automaticTestTestFileCollection;
    }

    @XmlTransient
    public Collection<AutomaticTaskTemplate> getAutomaticTaskTemplateCollection() {
        return automaticTaskTemplateCollection;
    }

    public void setAutomaticTaskTemplateCollection(Collection<AutomaticTaskTemplate> automaticTaskTemplateCollection) {
        this.automaticTaskTemplateCollection = automaticTaskTemplateCollection;
    }

    public Task getTaskId() {
        return taskId;
    }

    public void setTaskId(Task taskId) {
        this.taskId = taskId;
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
        if (!(object instanceof AutomaticTask)) {
            return false;
        }
        AutomaticTask other = (AutomaticTask) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "de.phoenix.database.entity.AutomaticTask[ id=" + id + " ]";
    }
    
}
