package com.frederlen.android.pittnotes.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Fred
 */
public class User implements Serializable {

    private Long id;

    private String username;

    private String email;

    private String password;

    private List<Long> userCourses;


    public User (String username, String email, String password) {
        this.id = null;
        this.username = username;
        this.email = email;
        this.password = password;
        this.userCourses = new ArrayList<>();
    }


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

    public void addCourse(Long id) { this.userCourses.add(id); }



    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
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
