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

import com.frederlen.android.pittnotes.NoteAdapter;
import com.frederlen.android.pittnotes.NoteAdapter.NoteAdapterOnClickHandler;
import com.frederlen.android.pittnotes.R;
import com.frederlen.android.pittnotes.RetrofitRestApi.GetNoteSetService;
import com.frederlen.android.pittnotes.models.NoteSet;
import com.frederlen.android.pittnotes.models.Session;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;


public class NoteActivity extends AppCompatActivity implements NoteAdapterOnClickHandler {

    private RecyclerView mRecyclerView;
    private NoteAdapter mNoteAdapter;
    private TextView mErrorMessageDisplay;
    private ProgressBar mSpinner;
    private Session session;
    private ArrayList<NoteSet> notes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_notes);
        mSpinner = (ProgressBar) findViewById(R.id.notes_progressBar);
        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        mNoteAdapter = new NoteAdapter(this);
        mRecyclerView.setAdapter(mNoteAdapter);

        loading();

        Button mAddNoteButton = (Button) findViewById(R.id.add_note_button);

        mAddNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), AddNoteActivity.class);
                intent.putExtra("SESSION", new Gson().toJson(session));
                startActivity(intent);
            }
        });

        Intent parentIntent = getIntent();

        if (parentIntent.hasExtra("SESSION")) {
            session = new Gson().fromJson(parentIntent.getStringExtra("SESSION"), Session.class);

            if (session.getNotes() != null) {
                hideErrorMessage();
                getNoteData(session.getNotes());
            }
        } else if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("SESSION")) {
                hideErrorMessage();
                String previousUser = savedInstanceState.getString("SESSION");
                session = new Gson().fromJson(previousUser, Session.class);

                if (session.getNotes() != null) {
                    getNoteData(session.getNotes());
                }
            }
        } else {
            session = null;
            showErrorMessage();
        }
    }

    @Override
    public void onClick(NoteSet note) {
        Context context = this;

        Intent intent = new Intent(getBaseContext(), NoteViewerActivity.class);
        intent.putExtra("NOTE", new Gson().toJson(note));
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
        String lifecycleContents = new Gson().toJson(session);
        outState.putString("SESSION", lifecycleContents);
    }

    public void getNoteData(final List<Long> noteIds) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://frederlen.com:8080")
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        GetNoteSetService service = retrofit.create(GetNoteSetService.class);
        Call<String> call = service.getNoteSet();

        loading();

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {

                if (response.isSuccessful()) {
                    Type listType = new TypeToken<List<NoteSet>>(){}.getType();
                    List<NoteSet> allNotes = new Gson().fromJson(response.body(), listType);

                    notLoading();

                    if (!response.body().equals("null")) {
                        for (int i = 0; i < noteIds.size(); i++) {
                            for (int j = 0; j < allNotes.size(); j++) {
                                if (noteIds.get(i) == allNotes.get(j).getId()) {
                                    notes.add(allNotes.remove(j));
                                    break;
                                }
                            }
                        }
                        mNoteAdapter.setNoteData(notes);
                    } else {
                        showErrorMessage();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                showErrorMessage();
                notLoading();
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
