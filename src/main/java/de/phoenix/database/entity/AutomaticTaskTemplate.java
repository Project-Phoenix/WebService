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
@Table(name = "automatic_task_template")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AutomaticTaskTemplate.findAll", query = "SELECT a FROM AutomaticTaskTemplate a"),
    @NamedQuery(name = "AutomaticTaskTemplate.findById", query = "SELECT a FROM AutomaticTaskTemplate a WHERE a.id = :id")})
public class AutomaticTaskTemplate implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Lob
    @Column(name = "text", columnDefinition = "text")
    private String text;
    @JoinColumn(name = "automatic_task_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private AutomaticTask automaticTaskId;

    public AutomaticTaskTemplate() {
    }

    public AutomaticTaskTemplate(Integer id) {
        this.id = id;
    }

    public AutomaticTaskTemplate(Integer id, String text) {
        this.id = id;
        this.text = text;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public AutomaticTask getAutomaticTaskId() {
        return automaticTaskId;
    }

    public void setAutomaticTaskId(AutomaticTask automaticTaskId) {
        this.automaticTaskId = automaticTaskId;
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
        if (!(object instanceof AutomaticTaskTemplate)) {
            return false;
        }
        AutomaticTaskTemplate other = (AutomaticTaskTemplate) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "de.phoenix.database.entity.AutomaticTaskTemplate[ id=" + id + " ]";
    }
    
}
