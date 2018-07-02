package fred.pittnotesdb;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;

/**
 *
 * @author Fred
 */
public class PITTNotesConfig extends Configuration{
    
    @JsonProperty
    private String pu;
    
    public String getPU(){
        return pu;
    }
    
}
