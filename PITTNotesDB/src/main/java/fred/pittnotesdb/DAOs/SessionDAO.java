package fred.pittnotesdb.DAOs;

import com.google.inject.Inject;
import fred.pittnotesdb.model.Session;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

/**
 *
 * @author Fred
 */
public class SessionDAO extends DAOFacade<Session>{
    
    @Inject
    public SessionDAO(EntityManager entityManager) {
        super(Session.class, entityManager, true);
    }
    
    public Session findSessionDate(String date) {
        TypedQuery<Session> userQuery = getEntityManager().createNamedQuery("Session.findSessionDate", Session.class);
        userQuery.setParameter("date", date);
        List<Session> sessions = userQuery.getResultList();
        
        for (Session session: sessions) {
            if (session.getSessionDate().equals(date))
                return session;
        }
        return null;
    }
}
