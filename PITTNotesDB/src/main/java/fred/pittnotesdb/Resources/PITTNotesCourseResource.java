package fred.pittnotesdb.Resources;

import fred.pittnotesdb.DAOs.CourseDAO;
import fred.pittnotesdb.model.Course;
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
@Path("/course")
public class PITTNotesCourseResource {

    private static final Logger LOGGER = Logger.getLogger(PITTNotesCourseResource.class.getName());

    private static Gson GSON;

    static {
        GSON = new GsonBuilder()
                .setPrettyPrinting()
                .serializeNulls()
                .create();
    }

    @Inject
    private CourseDAO courseDAO;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.WILDCARD)
    public Response courseGet(String id) {
        Course course = courseDAO.find(Long.parseLong(id));
        String json = GSON.toJson(course);
        return Response.ok(json).build();
    }
    
    @GET
    @Path("/{coursenumber}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.WILDCARD)
    public Response userGetUsername(@PathParam(value = "coursenumber") Long coursenumber) {
        Course course = courseDAO.findCourseNumber(coursenumber);
        if (course != null) { 
            return Response.ok(GSON.toJson(course)).build();
        } else {
            return Response.ok("null").build();
        }
    }
    
    @GET
    @Path("/findall")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.WILDCARD)
    public Response courseGetAll() {
        List<Course> courses = courseDAO.findAll();
        if (courses.size() > 0) {
            String json = GSON.toJson(courses);
            return Response.ok(json).build();
        } else {
            return Response.ok("null").build();
        }
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response coursePost(String json) {
        try {
            Course course = GSON.fromJson(json, Course.class);
            course = courseDAO.create(course);
            String newlyCreatedCourseJson = GSON.toJson(course);
            return Response.ok(newlyCreatedCourseJson).build();
        } catch (Exception e) {
            String message = "Could not save course into database";
            LOGGER.log(Level.SEVERE, message, e);
            return Response.serverError().entity(message).build();
        }
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response coursePut(String json) {
        try {
            Course course = GSON.fromJson(json, Course.class);
            course = courseDAO.update(course);
            String newlyUpdatedCourseJson = GSON.toJson(course);
            return Response.ok(newlyUpdatedCourseJson).build();
        } catch (Exception e) {
            String message = "Could not update course in the database";
            LOGGER.log(Level.SEVERE, message, e);
            return Response.serverError().entity(message).build();
        }
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response courseDelete(String json) {
        Course course = GSON.fromJson(json, Course.class);

        if (courseDAO.delete(course)) {
            return Response.ok().build();
        } else {
            String message = "Could not delete course in the database";
            LOGGER.log(Level.SEVERE, message);
            return Response.serverError().entity(message).build();
        }
    }
}
