package fred.pittnotesdb.Resources;

import fred.pittnotesdb.DAOs.UserDAO;
import fred.pittnotesdb.model.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.Inject;
import fred.pittnotesdb.StrClass;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author fred
 */

@Path("/user")
public class PITTNotesUserResource {

    private static final Logger LOGGER = Logger.getLogger(PITTNotesUserResource.class.getName());

    private static Gson GSON;

    static {
        GSON = new GsonBuilder()
                .setPrettyPrinting()
                .serializeNulls()
                .create();
    }

    @Inject
    private UserDAO userDAO;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.WILDCARD)
    public Response userGet(String inputJson) {
        StrClass id = GSON.fromJson(inputJson, StrClass.class);
        User user = userDAO.find(Long.parseLong(id.str));
        String json = GSON.toJson(user);
        return Response.ok(json).build();
    }
    
    @GET
    @Path("/{username}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.WILDCARD)
    public Response userGetUsername(@PathParam(value = "username") String username) {
        User user = userDAO.findUserName(username);
        if (user != null) { 
            return Response.ok(GSON.toJson(user)).build();
        } else {
            return Response.ok("null").build();
        }
    }
    
    @GET
    @Path("/{email}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response userGetEmail(@PathParam(value = "email") String email) {
        User user = userDAO.findUserEmail(email);
        if (user != null) { 
            return Response.ok(GSON.toJson(user)).build();
        } else {
            return Response.ok("null").build();
        }
    }
    
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response userPost(String json) {
        try {
            User user = GSON.fromJson(json, User.class);
            user = userDAO.create(user);
            String newlyCreatedUserJson = GSON.toJson(user);
            return Response.ok(newlyCreatedUserJson).build();
        } catch (Exception e) {
            String message = "Could not save user into database";
            LOGGER.log(Level.SEVERE, message, e);
            return Response.serverError().entity(message).build();
        }
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response userPut(String json) {
        try {
            User user = GSON.fromJson(json, User.class);
            user = userDAO.update(user);
            String newlyUpdatedUserJson = GSON.toJson(user);
            return Response.ok(newlyUpdatedUserJson).build();
        } catch (Exception e) {
            String message = "Could not update user in the database";
            LOGGER.log(Level.SEVERE, message, e);
            return Response.serverError().entity(message).build();
        }
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response userDelete(String json) {
        User user = GSON.fromJson(json, User.class);

        if (userDAO.delete(user)) {
            return Response.ok().build();
        } else {
            String message = "Could not delete user in the database";
            LOGGER.log(Level.SEVERE, message);
            return Response.serverError().entity(message).build();
        }
    }
}
