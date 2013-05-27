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
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table(name = "exerciseSheet")
@XmlRootElement
//@formatter:off
@NamedQueries({
    @NamedQuery(name = "ExerciseSheet.findAll", query = "SELECT e FROM ExerciseSheet e"),
    @NamedQuery(name = "ExerciseSheet.findByGroupId", query = "SELECT e FROM ExerciseSheet e WHERE e.exerciseSheetPK.groupId = :groupId"),
    @NamedQuery(name = "ExerciseSheet.findByExerciseSheetId", query = "SELECT e FROM ExerciseSheet e WHERE e.exerciseSheetPK.exerciseSheetId = :exerciseSheetId"),
    @NamedQuery(name = "ExerciseSheet.findByReleaseDate", query = "SELECT e FROM ExerciseSheet e WHERE e.releaseDate = :releaseDate"),
    @NamedQuery(name = "ExerciseSheet.findByExpirationDate", query = "SELECT e FROM ExerciseSheet e WHERE e.expirationDate = :expirationDate"),
    @NamedQuery(name = "ExerciseSheet.findByVisible", query = "SELECT e FROM ExerciseSheet e WHERE e.visible = :visible")})
//@formatter:on
public class ExerciseSheet implements Serializable {

    private static final long serialVersionUID = 1L;

    @EmbeddedId
    protected ExerciseSheetPK exerciseSheetPK;

    @Column(name = "releaseDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date releaseDate;

    @Column(name = "expirationDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date expirationDate;

    @Column(name = "visible")
    private Boolean visible;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "exerciseSheet")
    private List<Submission> submissionList;

    @JoinColumn(name = "exercise_sheet_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private ExerciseSheetPool exerciseSheetPool;

    @JoinColumn(name = "group_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Group group;

    public ExerciseSheet() {
    }

    public ExerciseSheet(ExerciseSheetPK exerciseSheetPK) {
        this.exerciseSheetPK = exerciseSheetPK;
    }

    public ExerciseSheet(int groupId, int exerciseSheetId) {
        this.exerciseSheetPK = new ExerciseSheetPK(groupId, exerciseSheetId);
    }

    public ExerciseSheetPK getExerciseSheetPK() {
        return exerciseSheetPK;
    }

    public void setExerciseSheetPK(ExerciseSheetPK exerciseSheetPK) {
        this.exerciseSheetPK = exerciseSheetPK;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public Boolean getVisible() {
        return visible;
    }

    public void show() {
        this.visible = true;
    }

    public void hide() {
        this.visible = false;
    }

    @XmlTransient
    public List<Submission> getSubmissions() {
        return submissionList;
    }

    public void setSubmissionList(List<Submission> submissions) {
        this.submissionList = submissions;
    }

    public ExerciseSheetPool getExerciseSheetPool() {
        return exerciseSheetPool;
    }

    public void setExerciseSheetPool(ExerciseSheetPool exerciseSheetPool) {
        this.exerciseSheetPool = exerciseSheetPool;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (exerciseSheetPK != null ? exerciseSheetPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are
        // not set
        if (!(object instanceof ExerciseSheet)) {
            return false;
        }
        ExerciseSheet other = (ExerciseSheet) object;
        if ((this.exerciseSheetPK == null && other.exerciseSheetPK != null) || (this.exerciseSheetPK != null && !this.exerciseSheetPK.equals(other.exerciseSheetPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "de.phoenix.database.entity.ExerciseSheet[ exerciseSheetPK=" + exerciseSheetPK + " ]";
    }

}
