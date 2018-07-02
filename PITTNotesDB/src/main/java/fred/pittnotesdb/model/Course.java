package fred.pittnotesdb.model;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author Fred
 */
@Entity
@Table(name = "COURSES")
@NamedQueries({
        @NamedQuery(
                name = "User.findCourseNumber", 
                query = "SELECT u FROM Course u WHERE u.courseNumber = :coursenumber")
})
public class Course implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "COURSE_ID")
    private Long id;
    
    @Column(name = "COURSENAME")
    private String courseName;
    
    @Column(name = "COURSENUMBER", unique = true)
    private int courseNumber;
    
    @Column(name = "COURSE_SESSIONS")
    @ElementCollection
    private List<Long> courseSessions;

    @Column(name = "TERM")
    private Integer term;

    
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCourseName() {
        return courseName;
    }
    
    
    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public int getCourseNumber() {
        return courseNumber;
    }

    public void setCourseNumber(int courseNumber) {
        this.courseNumber = courseNumber;
    }

    public List<Long> getSessions() {
        return courseSessions;
    }

    public void setSessions(List<Long> courseSessions) {
        this.courseSessions = courseSessions;
    }

    public Integer getTerm() {
        return term;
    }

    public void setTerm(Integer term) {
        this.term = term;
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
        if (!(object instanceof Course)) {
            return false;
        }
        Course other = (Course) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "fred.pittnotesdb.model.Course[ id=" + id + " ]";
    }
    
}
