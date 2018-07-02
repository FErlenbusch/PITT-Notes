package com.frederlen.android.pittnotes.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.frederlen.android.pittnotes.R;
import com.frederlen.android.pittnotes.SessionAdapter;
import com.frederlen.android.pittnotes.SessionAdapter.SessionAdapterOnClickHandler;
import com.frederlen.android.pittnotes.RetrofitRestApi.GetSessionsService;
import com.frederlen.android.pittnotes.models.Session;
import com.frederlen.android.pittnotes.models.Course;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;


public class SessionActivity extends AppCompatActivity implements SessionAdapterOnClickHandler {

    private RecyclerView mRecyclerView;
    private SessionAdapter mSessionAdapter;
    private TextView mErrorMessageDisplay;
    private ProgressBar mSpinner;
    private Course course;
    private ArrayList<Session> sessions = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_sessions);
        mSpinner = (ProgressBar) findViewById(R.id.session_progressBar);
        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        mSessionAdapter = new SessionAdapter(this);
        mRecyclerView.setAdapter(mSessionAdapter);

        loading();

        Button mAddSessionButton = (Button) findViewById(R.id.add_session_button);

        mAddSessionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), AddSessionActivity.class);
                intent.putExtra("COURSE", new Gson().toJson(course));
                startActivity(intent);
            }
        });

        Intent parentIntent = getIntent();

        if (parentIntent.hasExtra("COURSE")) {
            course = new Gson().fromJson(parentIntent.getStringExtra("COURSE"), Course.class);

            if (course.getSessions() != null) {
                hideErrorMessage();
                getSessionData(course.getSessions());
            }
        } else if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("COURSE")) {
                hideErrorMessage();
                String previousUser = savedInstanceState.getString("COURSE");
                course = new Gson().fromJson(previousUser, Course.class);

                if (course.getSessions() != null) {
                    getSessionData(course.getSessions());
                }
            }
        } else {
            course = null;
            showErrorMessage();
        }
    }


    @Override
    public void onClick(Session session) {
        Context context = this;

        Intent intent = new Intent(getBaseContext(), NoteActivity.class);
        intent.putExtra("SESSION", new Gson().toJson(session));
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
        String lifecycleContents = new Gson().toJson(course);
        outState.putString("COURSE", lifecycleContents);
    }

    public void getSessionData(final List<Long> sessionIds) {
        loading();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://frederlen.com:8080")
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        GetSessionsService service = retrofit.create(GetSessionsService.class);
        Call<String> call = service.getSessions();

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {

                if (response.isSuccessful()) {
                    Type listType = new TypeToken<List<Session>>(){}.getType();
                    List<Session> allSessions = new Gson().fromJson(response.body(), listType);

                    notLoading();
                    if (!response.body().equals("null")) {
                        for (int i = 0; i < sessionIds.size(); i++) {
                            for (int j = 0; j < allSessions.size(); j++) {
                                if (sessionIds.get(i) == allSessions.get(j).getId()) {
                                    sessions.add(allSessions.remove(j));
                                    break;
                                }
                            }
                        }
                        mSessionAdapter.setSessionData(sessions);
                    } else {
                        notLoading();
                        showErrorMessage();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
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
