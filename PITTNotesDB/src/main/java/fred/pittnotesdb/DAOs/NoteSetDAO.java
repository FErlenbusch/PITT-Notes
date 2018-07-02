package fred.pittnotesdb.DAOs;

import com.google.inject.Inject;
import fred.pittnotesdb.model.NoteSet;
import javax.persistence.EntityManager;

/**
 *
 * @author Fred
 */
public class NoteSetDAO extends DAOFacade<NoteSet>{
    
    @Inject
    public NoteSetDAO(EntityManager entityManager) {
        super(NoteSet.class, entityManager, true);
    }
}
