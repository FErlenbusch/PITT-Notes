package fred.pittnotesdb.model;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author Fred
 */
@Entity
@Table(name = "NOTESETS")
public class NoteSet implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "NOTESET_ID")
    private Long id;
    
    @Column(name = "SET_NOTES")
    @ElementCollection
    private List<Long> setNotes;

    
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Long> getNotes() {
        return setNotes;
    }

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
        // TODO: Warning - this method won't work in the case the id fields are not set
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
