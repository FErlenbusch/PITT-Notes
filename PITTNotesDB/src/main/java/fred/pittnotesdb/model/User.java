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
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author Fred
 */
@Entity
@Table(name = "USERS")
@NamedQueries({
        @NamedQuery(
                name = "User.findUserName", 
                query = "SELECT u FROM User u WHERE u.username = :username"),
        @NamedQuery(
                name = "User.findUserEmail", 
                query = "SELECT u FROM User u WHERE u.email = :email")
})
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "USER_ID")
    private Long id;
    
    @Column(name = "USERNAME", unique = true)
    private String username;
    
    @Column(name = "EMAIL", unique = true)
    private String email;
    
    @Column(name = "PASSWORD")
    private String password; 
    
    @Column(name = "USER_COURSES")
    @ElementCollection
    private List<Long> userCourses;

    
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Long> getCourses() {
        return userCourses;
    }

    public void setCourses(List<Long> userCourses) {
        this.userCourses = userCourses;
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
        if (!(object instanceof User)) {
            return false;
        }
        User other = (User) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "fred.pittnotesdb.model.User[ id=" + id + " ]";
    }
    
}
