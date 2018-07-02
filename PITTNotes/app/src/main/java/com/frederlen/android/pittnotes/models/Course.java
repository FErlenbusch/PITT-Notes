package com.frederlen.android.pittnotes.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Fred
 */
public class Course implements Serializable {

    private Long id;

    private String courseName;

    private int courseNumber;

    private List<Long> courseSessions;

    private int term;

    public Course (String courseName, int courseNumber) {
        this.id = null;
        this.courseName = courseName;
        this.courseNumber = courseNumber;
        this.courseSessions = new ArrayList<>();
        this.term = 12018;
    }


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

    public void addSession(Long id) { this.courseSessions.add(id); }

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
