package fred.pittnotesdb.Resources;

import fred.pittnotesdb.DAOs.NoteDAO;
import fred.pittnotesdb.model.Note;
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
@Path("/note")
public class PITTNotesNoteResource {

    private static final Logger LOGGER = Logger.getLogger(PITTNotesNoteResource.class.getName());

    private static Gson GSON;

    static {
        GSON = new GsonBuilder()
                .setPrettyPrinting()
                .serializeNulls()
                .create();
    }

    @Inject
    private NoteDAO noteDAO;

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.WILDCARD)
     public Response userGetUsername(@PathParam(value = "id") Long id) {
        Note note = noteDAO.find(id);
        if (note != null) { 
            return Response.ok(GSON.toJson(note)).build();
        } else {
            return Response.ok("null").build();
        }
    }
    
    @GET
    @Path("/findall")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.WILDCARD)
    public Response sessionGetAll() {
        List<Note> notes = noteDAO.findAll();
        if (notes.size() > 0) {
            String json = GSON.toJson(notes);
            return Response.ok(json).build();
        } else {
            return Response.ok("null").build();
        }
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response notePost(String json) {
        try {
            Note note = GSON.fromJson(json, Note.class);
            note = noteDAO.create(note);
            String newlyCreatedNoteJson = GSON.toJson(note);
            return Response.ok(newlyCreatedNoteJson).build();
        } catch (Exception e) {
            String message = "Could not save note into database";
            LOGGER.log(Level.SEVERE, message, e);
            return Response.serverError().entity(message).build();
        }
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response notePut(String json) {
        try {
            Note note = GSON.fromJson(json, Note.class);
            note = noteDAO.update(note);
            String newlyUpdatedNoteJson = GSON.toJson(note);
            return Response.ok(newlyUpdatedNoteJson).build();
        } catch (Exception e) {
            String message = "Could not update note in the database";
            LOGGER.log(Level.SEVERE, message, e);
            return Response.serverError().entity(message).build();
        }
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response noteDelete(String json) {
        Note note = GSON.fromJson(json, Note.class);

        if (noteDAO.delete(note)) {
            return Response.ok().build();
        } else {
            String message = "Could not delete note in the database";
            LOGGER.log(Level.SEVERE, message);
            return Response.serverError().entity(message).build();
        }
    }
}
