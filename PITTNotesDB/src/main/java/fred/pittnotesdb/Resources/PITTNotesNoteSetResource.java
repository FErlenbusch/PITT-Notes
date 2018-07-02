package fred.pittnotesdb.Resources;

import fred.pittnotesdb.DAOs.NoteSetDAO;
import fred.pittnotesdb.model.NoteSet;
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
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author fred
 */
@Path("/noteset")
public class PITTNotesNoteSetResource {

    private static final Logger LOGGER = Logger.getLogger(PITTNotesNoteSetResource.class.getName());

    private static Gson GSON;

    static {
        GSON = new GsonBuilder()
                .setPrettyPrinting()
                .serializeNulls()
                .create();
    }

    @Inject
    private NoteSetDAO noteSetDAO;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.WILDCARD)
    public Response noteSetGet(String id) {
        NoteSet noteSet = noteSetDAO.find(Long.parseLong(id));
        String json = new Gson().toJson(noteSet);
        return Response.ok(json).build();
    }
    
    @GET
    @Path("/findall")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.WILDCARD)
    public Response sessionGetAll() {
        List<NoteSet> noteSets = noteSetDAO.findAll();
        if (noteSets.size() > 0) {
            String json = GSON.toJson(noteSets);
            return Response.ok(json).build();
        } else {
            return Response.ok("null").build();
        }
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response noteSetPost(String json) {
        try {
            NoteSet noteSet = GSON.fromJson(json, NoteSet.class);
            noteSet = noteSetDAO.create(noteSet);
            String newlyCreatedNoteSetJson = GSON.toJson(noteSet);
            return Response.ok(newlyCreatedNoteSetJson).build();
        } catch (Exception e) {
            String message = "Could not save noteSet into database";
            LOGGER.log(Level.SEVERE, message, e);
            return Response.serverError().entity(message).build();
        }
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response noteSetPut(String json) {
        try {
            NoteSet noteSet = GSON.fromJson(json, NoteSet.class);
            noteSet = noteSetDAO.update(noteSet);
            String newlyUpdatedNoteSetJson = GSON.toJson(noteSet);
            return Response.ok(newlyUpdatedNoteSetJson).build();
        } catch (Exception e) {
            String message = "Could not update noteSet in the database";
            LOGGER.log(Level.SEVERE, message, e);
            return Response.serverError().entity(message).build();
        }
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response noteSetDelete(String json) {
        NoteSet noteSet = GSON.fromJson(json, NoteSet.class);

        if (noteSetDAO.delete(noteSet)) {
            return Response.ok().build();
        } else {
            String message = "Could not delete noteSet in the database";
            LOGGER.log(Level.SEVERE, message);
            return Response.serverError().entity(message).build();
        }
    }
}
