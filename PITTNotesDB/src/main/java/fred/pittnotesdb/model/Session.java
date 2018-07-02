package fred.pittnotesdb.model;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author Fred
 */
@Entity
@Table(name = "SESSIONS")
@NamedQueries({
        @NamedQuery(
                name = "Session.findSessionDate", 
                query = "SELECT u FROM Session u WHERE u.sessionDate = :date")
})
public class Session implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "SESSION_ID")
    private Long id;
    
    @Column(name = "SESSION_DATE")
    private String sessionDate;
    
    @Column(name = "SESSION_NOTES")
    @ElementCollection
    private List<Long> sessionNotes;

    @Column(name = "OWNER_ID")
    private Long ownerId;
    
    
    
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

    public void setNotes(List<Long> sessionNotes) {
        this.sessionNotes = sessionNotes;
    }
    
    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
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
