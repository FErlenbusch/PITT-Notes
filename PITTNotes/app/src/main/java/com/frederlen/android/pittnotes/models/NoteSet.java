package com.frederlen.android.pittnotes.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Fred
 */
public class NoteSet implements Serializable {

    private Long id;

    private List<Long> setNotes;


    public NoteSet () {
        this.id = null;
        this.setNotes = new ArrayList<>();
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Long> getNotes() {
        return setNotes;
    }

    public void addNote(Long id) { this.setNotes.add(id); }

    public void setNotes(List<Long> setNotes) {
        this.setNotes = setNotes;
    }



    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof NoteSet)) {
            return false;
        }
        NoteSet other = (NoteSet) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "fred.pittnotesdb.model.NoteSet[ id=" + id + " ]";
    }

}
