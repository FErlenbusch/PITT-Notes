package fred.pittnotesdb.model;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2018-03-14T15:34:29")
@StaticMetamodel(Course.class)
public class Course_ { 

    public static volatile SingularAttribute<Course, String> courseName;
    public static volatile SingularAttribute<Course, Integer> courseNumber;
    public static volatile SingularAttribute<Course, Integer> term;
    public static volatile SingularAttribute<Course, Long> id;
    public static volatile ListAttribute<Course, Long> courseSessions;

}