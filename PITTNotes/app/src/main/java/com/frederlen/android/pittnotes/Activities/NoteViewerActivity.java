package com.frederlen.android.pittnotes.Activities;

import com.frederlen.android.pittnotes.R;
import com.frederlen.android.pittnotes.RetrofitRestApi.NoteService;
import com.frederlen.android.pittnotes.models.Note;
import com.frederlen.android.pittnotes.models.NoteSet;
import com.google.gson.Gson;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class NoteViewerActivity extends AppCompatActivity {

    private View mNoteForm;
    private ImageView mNoteImageView;
    private ProgressBar mSpinner;
    private NoteSet noteSet;
    private Note note;
    private List<Note> notes = new ArrayList<>();
    private int pos = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_viewer);

        mNoteImageView = (ImageView) findViewById(R.id.note_viewer_image_viewer);
        mNoteForm = (View) findViewById(R.id.note_viewer_form);
        mSpinner = (ProgressBar) findViewById(R.id.note_viewer_progressBar);
        Button mNotePrevButton = (Button) findViewById(R.id.prev_button);
        Button mNoteNextButton = (Button) findViewById(R.id.next_button);

        Intent parentIntent = getIntent();

        if (parentIntent.hasExtra("NOTE")) {
            noteSet = new Gson().fromJson(parentIntent.getStringExtra("NOTE"), NoteSet.class);
        }

        loadNotes();

        mNoteNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextImage();
            }
        });

        mNotePrevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prevImage();
            }
        });
    }

    private void loadNotes() {
        for (int i = 0; i < noteSet.getNotes().size(); i++) {
            getImage(noteSet.getNotes().get(i));
        }
    }

    private void setViewer() {
        byte[] bitMapData = notes.get(pos).getNoteData();
        Bitmap outBitmap = BitmapFactory.decodeByteArray(bitMapData, 0, bitMapData.length);
        mNoteImageView.setImageBitmap(outBitmap);
    }

    private void nextImage() {
        if (++pos == noteSet.getNotes().size()) {
            pos = 0;
        }

        setViewer();
    }

    private void prevImage() {
        if (--pos < 0) {
            pos = noteSet.getNotes().size() - 1;
        }

        setViewer();
    }

    public void getImage(Long id) {

        String output = null;
        note = null;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://frederlen.com:8080")
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        NoteService service = retrofit.create(NoteService.class);
        Call<String> call = service.getNote(id);

        if (notes.size() == 0) {
            loading();
        }

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {

                if (response.isSuccessful()) {

                    if (response.body().equals("null")) {

                        Toast.makeText(getApplicationContext(), "Server unable to get note", Toast.LENGTH_LONG).show();
                        notLoading();
                    } else {

                        note = new Gson().fromJson(response.body(), Note.class);
                        notes.add(note);

                        if(notes.size() == noteSet.getNotes().size()) {
                            notLoading();
                            setViewer();
                        }
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
        mNoteForm.setVisibility(View.GONE);
    }

    private void notLoading() {
        mSpinner.setVisibility(View.GONE);
        mNoteForm.setVisibility(View.VISIBLE);
    }
}

