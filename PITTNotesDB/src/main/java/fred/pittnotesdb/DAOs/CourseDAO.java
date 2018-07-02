package fred.pittnotesdb.DAOs;

import com.google.inject.Inject;
import fred.pittnotesdb.model.Course;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

/**
 *
 * @author Fred
 */
public class CourseDAO extends DAOFacade<Course>{
    
    @Inject
    public CourseDAO(EntityManager entityManager) {
        super(Course.class, entityManager, true);
    }
    
    public Course findCourseNumber(Long coursenumber) {
        TypedQuery<Course> userQuery = getEntityManager().createNamedQuery("User.findCourseNumber", Course.class);
        userQuery.setParameter("coursenumber", coursenumber);
        List<Course> courses = userQuery.getResultList();
        
        for (Course course: courses) {
            if (course.getCourseNumber() == coursenumber)
                return course;
        }
        return null;
    }
}
