package fred.pittnotesdb;

import fred.pittnotesdb.Resources.PITTNotesNoteSetResource;
import fred.pittnotesdb.Resources.PITTNotesNoteResource;
import fred.pittnotesdb.Resources.PITTNotesSessionResource;
import fred.pittnotesdb.Resources.PITTNotesCourseResource;
import fred.pittnotesdb.Resources.PITTNotesUserResource;
import com.google.inject.Guice;
import com.google.inject.Injector;
import io.dropwizard.Application;
import io.dropwizard.setup.Environment;

/**
 *
 * @author Fred
 */
public class PITTNotesApp extends Application<PITTNotesConfig>{
    
    @Override
    public void run(PITTNotesConfig pittNotesConfig, Environment environment) throws Exception {
        PITTNotesModule pittNotesModule = new PITTNotesModule(pittNotesConfig);
        Injector injector = Guice.createInjector(pittNotesModule);
       
        PITTNotesUserResource userResource = injector.getInstance(PITTNotesUserResource.class);
        PITTNotesCourseResource courseResource = injector.getInstance(PITTNotesCourseResource.class);
        PITTNotesSessionResource sessionResource = injector.getInstance(PITTNotesSessionResource.class);
        PITTNotesNoteSetResource noteSetResource = injector.getInstance(PITTNotesNoteSetResource.class);
        PITTNotesNoteResource noteResource = injector.getInstance(PITTNotesNoteResource.class);
        
        environment.jersey().register(userResource);
        environment.jersey().register(courseResource);
        environment.jersey().register(sessionResource);
        environment.jersey().register(noteSetResource);
        environment.jersey().register(noteResource);
    }
}
