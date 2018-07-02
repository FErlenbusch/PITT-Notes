package fred.pittnotesdb.DAOs;

import com.google.inject.Inject;
import fred.pittnotesdb.model.User;
import java.util.List;
import java.util.function.Predicate;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

/**
 *
 * @author Fred
 */
public class UserDAO extends DAOFacade<User>{
    
    @Inject
    public UserDAO(EntityManager entityManager) {
        super(User.class, entityManager, true);
    }
    
    public User findUserName(String username) {
        TypedQuery<User> userQuery = getEntityManager().createNamedQuery("User.findUserName", User.class);
        userQuery.setParameter("username", username);
        List<User> users = userQuery.getResultList();
        
        for (User user: users) {
            if (user.getUsername().equals(username))
                return user;
        }
        return null;
    }
    
    public User findUserEmail(String email) {
        TypedQuery<User> userQuery = getEntityManager().createNamedQuery("User.findUserEmail", User.class);
        userQuery.setParameter("email", email);
        List<User> users = userQuery.getResultList();
        
        for (User user: users) {
            if (user.getEmail().equals(email))
                return user;
        }
        return null;
    }
}


