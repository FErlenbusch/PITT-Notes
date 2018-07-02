package com.frederlen.android.pittnotes.models;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author Fred
 */
public class Note implements Serializable {

    private Long id;

    private byte[] noteData;

    public Note (byte[] noteData) {
        this.id = null;
        this.noteData = noteData;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getNoteData() {
        return noteData;
    }

    public void setNoteData(byte[] noteData) {
        this.noteData = noteData;
    }



    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Note)) {
            return false;
        }
        Note other = (Note) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "fred.pittnotesdb.model.Note[ id=" + id + " ]";
    }

}

