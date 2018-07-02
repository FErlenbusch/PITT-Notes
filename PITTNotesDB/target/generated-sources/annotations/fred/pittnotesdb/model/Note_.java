package fred.pittnotesdb.model;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2018-03-14T15:34:29")
@StaticMetamodel(Note.class)
public class Note_ { 

    public static volatile SingularAttribute<Note, String> fileName;
    public static volatile SingularAttribute<Note, byte[]> noteData;
    public static volatile SingularAttribute<Note, Long> id;

}