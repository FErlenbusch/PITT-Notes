package com.frederlen.android.pittnotes.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.frederlen.android.pittnotes.R;
import com.frederlen.android.pittnotes.RetrofitRestApi.CourseCreateService;
import com.frederlen.android.pittnotes.RetrofitRestApi.CourseNumberService;
import com.frederlen.android.pittnotes.RetrofitRestApi.UserUpdateService;
import com.frederlen.android.pittnotes.models.Course;
import com.frederlen.android.pittnotes.models.User;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;



public class AddCourseActivity extends AppCompatActivity {

    // UI references.
    private EditText mCourseNameView;
    private EditText mCourseNumberView;
    private View mCourseCreateFormView;
    private ProgressBar mSpinner;
    private User user;
    private int result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);

        mCourseNameView = (EditText) findViewById(R.id.course_name);

        mCourseNumberView = (EditText) findViewById(R.id.course_number);

        mCourseNumberView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptAdd();
                    return true;
                }
                return false;
            }
        });

        Button mCreateAddCourseButton = (Button) findViewById(R.id.create_course_button);

        mCreateAddCourseButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptAdd();
            }
        });

        mCourseCreateFormView = findViewById(R.id.create_course_form);
        mSpinner = (ProgressBar) findViewById(R.id.add_course_progressBar);

        notLoading();

        Intent parentIntent = getIntent();

        if (parentIntent.hasExtra("USER")) {
            user = new Gson().fromJson(parentIntent.getStringExtra("USER"), User.class);
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        String lifecycleContents = new Gson().toJson(user);
        outState.putString("USER", lifecycleContents);
    }


    private void attemptAdd() {

        // Store values at the time of the login attempt.
        final String courseName = mCourseNameView.getText().toString();
        final String courseNumber = mCourseNumberView.getText().toString();

        boolean cancel = false;

        // Check for a valid coursename, if the user entered one.
        if (courseName.isEmpty() || !isCourseNameValid(courseName)) {
            Toast.makeText(getApplicationContext(),"Course Name is invalid",Toast.LENGTH_LONG).show();
            cancel = true;
        }

        // Check for a valid course number.
        if (courseNumber.isEmpty()) {
            Toast.makeText(getApplicationContext(),"All fields are required",Toast.LENGTH_LONG).show();
            cancel = true;
        } else {
            if (isCourseNumberTaken(courseNumber) == -1) {
                cancel = true;
            }
        }

        if (!cancel) {

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://frederlen.com:8080")
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .build();
            CourseCreateService service = retrofit.create(CourseCreateService.class);
            Call<String> call = service.postCourse(
                    new Gson().toJson(new Course(courseName, Integer.parseInt(courseNumber))));

            loading();

            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, retrofit2.Response<String> response) {

                    if (response.isSuccessful()) {

                        if (!response.body().equals("null")) {
                            Course course = new Gson().fromJson(response.body(), Course.class);
                            user.addCourse(course.getId());
                            notLoading();
                            updateUser();
                        } else {
                            Toast.makeText(getApplicationContext(),"Server unable to create course",Toast.LENGTH_LONG).show();
                            notLoading();
                        }
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Toast.makeText(getApplicationContext(),"Server Error",Toast.LENGTH_LONG).show();
                    notLoading();
                }
            });

        }


    }

    private boolean isCourseNameValid(String courseName) {

        return courseName.length() > 5;
    }

    private int isCourseNumberTaken(String courseNumber) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://frederlen.com:8080")
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        CourseNumberService service = retrofit.create(CourseNumberService.class);
        Call<String> call = service.getCourseNumber(courseNumber);

        loading();

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {

                if (response.isSuccessful()) {

                    if (!response.body().equals("null")) {
                        Course course = new Gson().fromJson(response.body(), Course.class);
                        user.addCourse(course.getId());
                        notLoading();
                        updateUser();
                    } else {
                        result = 0;
                    }
                } else {
                    Toast.makeText(getApplicationContext(),"Server Error",Toast.LENGTH_LONG).show();
                    notLoading();
                    result = -1;
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"Server Error",Toast.LENGTH_LONG).show();
                notLoading();
                result = -1;
            }
        });

        return result;
    }

    private void updateUser() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://frederlen.com:8080")
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        UserUpdateService service = retrofit.create(UserUpdateService.class);
        Call<String> call = service.putUser(new Gson().toJson(user));

        loading();

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {

                if (response.isSuccessful()) {

                    if (response.body().equals("null")) {
                        Toast.makeText(getApplicationContext(), "Server unable to update user", Toast.LENGTH_LONG).show();
                    }
                    else {
                        notLoading();

                        user = new Gson().fromJson(response.body(), User.class);
                        Intent intent = new Intent(getBaseContext(), MainActivity.class);
                        intent.putExtra("USER", new Gson().toJson(user));
                        startActivity(intent);
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Server Error", Toast.LENGTH_LONG).show();
                notLoading();
            }
        });
    }

    private void loading() {
        mSpinner.setVisibility(View.VISIBLE);
        mCourseCreateFormView.setVisibility(View.GONE);
    }

    private void notLoading() {
        mSpinner.setVisibility(View.GONE);
        mCourseCreateFormView.setVisibility(View.VISIBLE);
    }
}
