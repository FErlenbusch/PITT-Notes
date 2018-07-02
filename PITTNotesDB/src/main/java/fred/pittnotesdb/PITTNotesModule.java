package fred.pittnotesdb;

import com.google.inject.AbstractModule;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;

/**
 *
 * @author Fred
 */
public class PITTNotesModule extends AbstractModule{
    
    private PITTNotesConfig pittNotesConfig;

    public PITTNotesModule(PITTNotesConfig pittNotesConfig) {
        this.pittNotesConfig = pittNotesConfig;
    }
    
    
    @Override
    protected void configure() {
        bind(EntityManager.class).toInstance(getEntityManager());
    }
    
    private EntityManager getEntityManager(){
        return Persistence.createEntityManagerFactory(pittNotesConfig.getPU()).createEntityManager();
    }
}
