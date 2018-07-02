package com.frederlen.android.pittnotes.Activities;



import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.frederlen.android.pittnotes.CourseAdapter;
import com.frederlen.android.pittnotes.CourseAdapter.CourseAdapterOnClickHandler;
import com.frederlen.android.pittnotes.R;
import com.frederlen.android.pittnotes.RetrofitRestApi.GetCoursesService;
import com.frederlen.android.pittnotes.models.Course;
import com.frederlen.android.pittnotes.models.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;


public class MainActivity extends AppCompatActivity implements CourseAdapterOnClickHandler {

    private RecyclerView mRecyclerView;
    private CourseAdapter mCourseAdapter;
    private TextView mErrorMessageDisplay;
    private ProgressBar mSpinner;
    private User user;
    private ArrayList<Course> courses = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_courses);
        mSpinner = (ProgressBar) findViewById(R.id.main_progressBar);
        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        mCourseAdapter = new CourseAdapter(this);
        mRecyclerView.setAdapter(mCourseAdapter);

        loading();

        Button mAddCourseButton = (Button) findViewById(R.id.add_course_button);

        mAddCourseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), AddCourseActivity.class);
                intent.putExtra("USER", new Gson().toJson(user));
                startActivity(intent);
            }
        });

        Intent parentIntent = getIntent();

        if (parentIntent.hasExtra("USER")) {
            user = new Gson().fromJson(parentIntent.getStringExtra("USER"), User.class);

            if (user.getCourses() != null) {
                hideErrorMessage();
                getCourseData(user.getCourses());
            }
        } else if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("USER")) {
                hideErrorMessage();
                String previousUser = savedInstanceState.getString("USER");
                user = new Gson().fromJson(previousUser, User.class);

                if (user.getCourses() != null) {
                    getCourseData(user.getCourses());
                }
            }
        } else {
            user = null;
            showErrorMessage();
        }
    }


    @Override
    public void onClick(Course course) {
        Context context = this;

        Intent intent = new Intent(getBaseContext(), SessionActivity.class);
        intent.putExtra("COURSE", new Gson().toJson(course));
        startActivity(intent);
    }

    private void showErrorMessage() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    private void hideErrorMessage() {
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        String lifecycleContents = new Gson().toJson(user);
        outState.putString("USER", lifecycleContents);
    }

    public void getCourseData(final List<Long> courseIds) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://frederlen.com:8080")
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        GetCoursesService service = retrofit.create(GetCoursesService.class);
        Call<String> call = service.getCourses();

        loading();

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {

                if (response.isSuccessful()) {
                    Type listType = new TypeToken<List<Course>>(){}.getType();
                    List<Course> allCourses = new Gson().fromJson(response.body(), listType);

                    notLoading();

                    if (!response.body().equals("null")) {
                        for (int i = 0; i < courseIds.size(); i++) {
                            for (int j = 0; j < allCourses.size(); j++) {
                                if (courseIds.get(i) == allCourses.get(j).getId()) {
                                    courses.add(allCourses.remove(j));
                                    break;
                                }
                            }
                        }
                        mCourseAdapter.setCourseData(courses);
                    } else {
                        showErrorMessage();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                notLoading();
                showErrorMessage();
            }
        });
    }

    private void loading() {
        mSpinner.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.GONE);
    }

    private void notLoading() {
        mSpinner.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }
}
