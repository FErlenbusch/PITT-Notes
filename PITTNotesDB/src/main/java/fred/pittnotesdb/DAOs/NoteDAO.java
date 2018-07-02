package fred.pittnotesdb.DAOs;

import com.google.inject.Inject;
import fred.pittnotesdb.model.Note;
import javax.persistence.EntityManager;

/**
 *
 * @author Fred
 */
public class NoteDAO extends DAOFacade<Note>{
    
    @Inject
    public NoteDAO(EntityManager entityManager) {
        super(Note.class, entityManager, true);
    }
}
