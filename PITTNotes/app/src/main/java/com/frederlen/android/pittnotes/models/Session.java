package com.frederlen.android.pittnotes.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Fred
 */
public class Session implements Serializable {

    private Long id;

    private String sessionDate;

    private List<Long> sessionNotes;

    private Long ownerId;


    public Session (String sessionDate, Long ownerId) {
        this.id = null;
        this.sessionDate = sessionDate;
        this.sessionNotes = new ArrayList<>();
        this.ownerId = ownerId;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSessionDate() {
        return sessionDate;
    }

    public void setSessionDate(String sessionDate) {
        this.sessionDate = sessionDate;
    }

    public List<Long> getNotes() {
        return sessionNotes;
    }

    public void addNote(Long id) { this.sessionNotes.add(id); }

    public void setNotes(List<Long> sessionNotes) {
        this.sessionNotes = sessionNotes;
    }

    public Long getOwnerId() { return this.ownerId; }

    public void setOwnerId(Long ownerId) { this.ownerId = ownerId; }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Session)) {
            return false;
        }
        Session other = (Session) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "fred.pittnotesdb.model.Session[ id=" + id + " ]";
    }

}

