package fred.pittnotesdb.model;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2018-03-14T15:34:29")
@StaticMetamodel(Session.class)
public class Session_ { 

    public static volatile SingularAttribute<Session, String> sessionDate;
    public static volatile SingularAttribute<Session, Long> id;
    public static volatile ListAttribute<Session, Long> sessionNotes;
    public static volatile SingularAttribute<Session, Long> ownerId;

}