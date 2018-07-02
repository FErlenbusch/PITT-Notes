package fred.pittnotesdb.Resources;

import fred.pittnotesdb.DAOs.SessionDAO;
import fred.pittnotesdb.model.Session;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.Inject;
import java.util.List;
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
@Path("/session")
public class PITTNotesSessionResource {

    private static final Logger LOGGER = Logger.getLogger(PITTNotesSessionResource.class.getName());

    private static Gson GSON;

    static {
        GSON = new GsonBuilder()
                .setPrettyPrinting()
                .serializeNulls()
                .create();
    }

    @Inject
    private SessionDAO sessionDAO;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.WILDCARD)
    public Response sessionGet(String id) {
        Session session = sessionDAO.find(Long.parseLong(id));
        String json = GSON.toJson(session);
        return Response.ok(json).build();
    }
    
    @GET
    @Path("/{date}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response userGetEmail(@PathParam(value = "date") String date) {
        String convertedDate = GSON.fromJson(date, String.class);
        Session session = sessionDAO.findSessionDate(convertedDate);
        if (session != null) { 
            return Response.ok(GSON.toJson(session)).build();
        } else {
            return Response.ok("null").build();
        }
    }
    
    @GET
    @Path("/findall")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.WILDCARD)
    public Response sessionGetAll() {
        List<Session> sessions = sessionDAO.findAll();
        if (sessions.size() > 0) {
            String json = GSON.toJson(sessions);
            return Response.ok(json).build();
        } else {
            return Response.ok("null").build();
        }
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response sessionPost(String json) {
        try {
            Session session = GSON.fromJson(json, Session.class);
            session = sessionDAO.create(session);
            String newlyCreatedSessionJson = GSON.toJson(session);
            return Response.ok(newlyCreatedSessionJson).build();
        } catch (Exception e) {
            String message = "Could not save session into database";
            LOGGER.log(Level.SEVERE, message, e);
            return Response.serverError().entity(message).build();
        }
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response sessionPut(String json) {
        try {
            Session session = GSON.fromJson(json, Session.class);
            session = sessionDAO.update(session);
            String newlyUpdatedSessionJson = GSON.toJson(session);
            return Response.ok(newlyUpdatedSessionJson).build();
        } catch (Exception e) {
            String message = "Could not update session in the database";
            LOGGER.log(Level.SEVERE, message, e);
            return Response.serverError().entity(message).build();
        }
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response sessionDelete(String json) {
        Session session = GSON.fromJson(json, Session.class);

        if (sessionDAO.delete(session)) {
            return Response.ok().build();
        } else {
            String message = "Could not delete session in the database";
            LOGGER.log(Level.SEVERE, message);
            return Response.serverError().entity(message).build();
        }
    }
}
