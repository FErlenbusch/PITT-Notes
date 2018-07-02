package com.frederlen.android.pittnotes.Activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;


import com.frederlen.android.pittnotes.MyDatePickerFragment;
import com.frederlen.android.pittnotes.R;
import com.frederlen.android.pittnotes.RetrofitRestApi.SessionCreateService;
import com.frederlen.android.pittnotes.RetrofitRestApi.GetSessionsService;
import com.frederlen.android.pittnotes.RetrofitRestApi.CourseUpdateService;
import com.frederlen.android.pittnotes.models.Course;
import com.frederlen.android.pittnotes.models.Session;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;



public class AddSessionActivity extends AppCompatActivity {

    // UI references.
    private View mSessionCreateFormView;
    private EditText mDateSelect;
    private ProgressBar mSpinner;
    private Calendar calendar;
    private Course course;
    private String date;
    private int result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_session);

        Button mSessionAddButton = (Button) findViewById(R.id.session_add_button);

        mSessionAddButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (date != null && !date.isEmpty()) {
                    attemptAdd();
                }
            }
        });

        calendar = Calendar.getInstance();

        mDateSelect= (EditText) findViewById(R.id.date_menu);
        final DatePickerDialog.OnDateSetListener selectedDate = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        mDateSelect.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                    new DatePickerDialog(AddSessionActivity.this, selectedDate, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        mSessionCreateFormView = findViewById(R.id.session_add_form);
        mSpinner = (ProgressBar) findViewById(R.id.create_session_progressBar);

        notLoading();

        Intent parentIntent = getIntent();

        if (parentIntent.hasExtra("COURSE")) {
            course = new Gson().fromJson(parentIntent.getStringExtra("COURSE"), Course.class);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        String lifecycleContents = new Gson().toJson(course);
        outState.putString("COURSE", lifecycleContents);
    }

    private void updateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        mDateSelect.setText(sdf.format(calendar.getTime()));
        date = mDateSelect.getText().toString();
    }

    private void attemptAdd() {

        boolean cancel = false;

        int flag = isDateTaken();

        if (flag == 1 || flag == -1) {
            cancel = true;
        }

        if (!cancel) {

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://frederlen.com:8080")
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .build();
            SessionCreateService service = retrofit.create(SessionCreateService.class);
            Call<String> call = service.postSession(
                    new Gson().toJson(new Session(date, course.getId())));

            loading();

            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, retrofit2.Response<String> response) {

                    if (response.isSuccessful()) {

                        if (!response.body().equals("null")) {
                            Session session = new Gson().fromJson(response.body(), Session.class);
                            course.addSession(session.getId());
                            notLoading();
                            updateCourse();
                        } else {
                            Toast.makeText(getApplicationContext(),"Server unable to create class",Toast.LENGTH_LONG).show();
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

    private int isDateTaken() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://frederlen.com:8080")
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        GetSessionsService service = retrofit.create(GetSessionsService.class);
        Call<String> call = service.getSessions();

        loading();

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {

                if (response.isSuccessful()) {

                    notLoading();

                    if (!response.body().equals("null")) {
                        Type listType = new TypeToken<List<Session>>(){}.getType();
                        List<Session> allSessions = new Gson().fromJson(response.body(), listType);

                        for (Session session: allSessions) {
                            if (session.getOwnerId() == course.getId() && session.getSessionDate().equals(date)) {
                                result = 1;
                                break;
                            }
                        }
                    } else {
                        result = 0;
                    }
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

    private void updateCourse() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://frederlen.com:8080")
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        CourseUpdateService service = retrofit.create(CourseUpdateService.class);
        Call<String> call = service.putCourse(new Gson().toJson(course));

        loading();

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {

                if (response.isSuccessful()) {

                    notLoading();

                    if (response.body().equals("null")) {
                        Toast.makeText(getApplicationContext(), "Server unable to update course", Toast.LENGTH_LONG).show();
                    }
                    else {
                        course = new Gson().fromJson(response.body(), Course.class);
                        Intent intent = new Intent(getBaseContext(), SessionActivity.class);
                        intent.putExtra("COURSE", new Gson().toJson(course));
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

    public void showDatePicker(View v) {
        MyDatePickerFragment newFragment = new MyDatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "date picker");
    }

    private void loading() {
        mSpinner.setVisibility(View.VISIBLE);
        mSessionCreateFormView.setVisibility(View.GONE);
    }

    private void notLoading() {
        mSpinner.setVisibility(View.GONE);
        mSessionCreateFormView.setVisibility(View.VISIBLE);
    }
}
