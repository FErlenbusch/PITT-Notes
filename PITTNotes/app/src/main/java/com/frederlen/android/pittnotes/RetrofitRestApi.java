package com.frederlen.android.pittnotes;

import com.frederlen.android.pittnotes.models.Note;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

/**
 * Created by Fred on 3/13/18.
 */

public class RetrofitRestApi {

    // ------------------- USER -------------------
    public interface UserEmailService {
        @GET("/user/{email}")
        Call<String> getUserEmail(@Path("email") String email);
    }

    public interface  UserNameService {
        @GET("/user/{username}")
        Call<String> getUserName(@Path("username") String username);
    }

    public interface  UserCreateService {
        @POST("/user")
        @Headers("content-type: application/json")
        Call<String> postUserName(@Body String body);
    }

    public interface  UserUpdateService {
        @PUT("/user")
        @Headers("content-type: application/json")
        Call<String> putUser(@Body String body);
    }


    // ------------------- COURSE -------------------
    public interface GetCoursesService {
        @GET("/course/findall")
        Call<String> getCourses();
    }

    public interface  CourseNumberService {
        @GET("/course/{coursenumber}")
        Call<String> getCourseNumber(@Path("coursenumber") String coursenumber);
    }

    public interface  CourseCreateService {
        @POST("/course")
        @Headers("content-type: application/json")
        Call<String> postCourse(@Body String body);
    }

    public interface  CourseUpdateService {
        @PUT("/course")
        @Headers("content-type: application/json")
        Call<String> putCourse(@Body String body);
    }


    // ------------------- SESSION -------------------
    public interface SessionDateService {
        @GET("/session/{date}")
        Call<String> getSessionDate(@Path("date") String date);
    }

    public interface GetSessionsService {
        @GET("/session/findall")
        Call<String> getSessions();
    }

    public interface  SessionCreateService {
        @POST("/session")
        @Headers("content-type: application/json")
        Call<String> postSession(@Body String body);
    }

    public interface  SessionUpdateService {
        @PUT("/session")
        @Headers("content-type: application/json")
        Call<String> putSession(@Body String body);
    }


    // ------------------- NOTESET -------------------
    public interface GetNoteSetService {
        @GET("/noteset/findall")
        Call<String> getNoteSet();
    }

    public interface  NoteSetCreateService {
        @POST("/noteset")
        @Headers("content-type: application/json")
        Call<String> postNoteSet(@Body String body);
    }

    public interface  NoteSetUpdateService {
        @PUT("/noteset")
        @Headers("content-type: application/json")
        Call<String> putNoteSet(@Body String body);
    }


    // ------------------- NOTE -------------------
    public interface NoteService {
        @GET("/note/{id}")
        Call<String> getNote(@Path("id") Long id);
    }

    public interface  NoteCreateService {
        @POST("/note")
        @Headers("content-type: application/json")
        Call<String> postNote(@Body String body);
    }

    public interface  NoteUpdateService {
        @PUT("/note")
        @Headers("content-type: application/json")
        Call<String> putNote(@Body String body);
    }
}
