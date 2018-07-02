package com.frederlen.android.pittnotes.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.frederlen.android.pittnotes.R;
import com.frederlen.android.pittnotes.RetrofitRestApi;
import com.frederlen.android.pittnotes.RetrofitRestApi.NoteCreateService;
import com.frederlen.android.pittnotes.RetrofitRestApi.NoteSetCreateService;
import com.frederlen.android.pittnotes.RetrofitRestApi.SessionUpdateService;
import com.frederlen.android.pittnotes.models.NoteSet;
import com.frederlen.android.pittnotes.models.Note;
import com.frederlen.android.pittnotes.models.Session;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;



public class AddNoteActivity extends AppCompatActivity {

    private static final int CAMERA_REQUEST = 1888;

    private View mNoteForm;
    private ImageView mAddNoteImageView;
    private ProgressBar mSpinner;
    private Session session;
    private NoteSet noteSet = new NoteSet();
    private Note note;
    private Uri selectedImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_notes);

        mAddNoteImageView = (ImageView) findViewById(R.id.add_note_imageview);
        mNoteForm = (View) findViewById(R.id.add_note_image_display);
        mSpinner = (ProgressBar) findViewById(R.id.create_note_progressBar);
        Button mNoteAddButton = (Button) findViewById(R.id.select_image);
        Button mAddAnotherButton = (Button) findViewById(R.id.add_another);
        Button mFinishButton = (Button) findViewById(R.id.finish);


        mNoteAddButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto , 1);
            }
        });

        mAddAnotherButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mAddNoteImageView.setImageURI(null);
                note = null;

                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto , 1);
            }
        });

        mFinishButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                createNoteSet();
            }
        });

        notLoading();

        Intent parentIntent = getIntent();

        if (parentIntent.hasExtra("SESSION")) {
            session = new Gson().fromJson(parentIntent.getStringExtra("SESSION"), Session.class);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        if (requestCode == 1) {

            if (resultCode == RESULT_OK) {
                selectedImage = imageReturnedIntent.getData();

                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);

                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] byteArray = stream.toByteArray();

                    uploadNote(byteArray);

                    byte[] bitMapData = note.getNoteData();
                    Bitmap outBitmap = BitmapFactory.decodeByteArray(bitMapData, 0, bitMapData.length);
                    mAddNoteImageView.setImageBitmap(outBitmap);

                } catch (IOException e) {

                    mAddNoteImageView.setImageURI(null);
                }
            }
        }
    }

    public void uploadNote(byte[] inputData) {
        note = new Note(inputData);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://frederlen.com:8080")
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        NoteCreateService service = retrofit.create(NoteCreateService.class);
        Call<String> call = service.postNote(new Gson().toJson(note));

        loading();

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {

                if (response.isSuccessful()) {

                    notLoading();

                    if (response.body().equals("null")) {
                        Toast.makeText(getApplicationContext(), "Server unable to create note", Toast.LENGTH_LONG).show();
                    } else {
                        note = new Gson().fromJson(response.body(), Note.class);
                        noteSet.addNote(note.getId());
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

    private void createNoteSet() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://frederlen.com:8080")
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        NoteSetCreateService service = retrofit.create(NoteSetCreateService.class);
        Call<String> call = service.postNoteSet(new Gson().toJson(noteSet));

        loading();

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {

                if (response.isSuccessful()) {

                    if (response.body().equals("null")) {
                        Toast.makeText(getApplicationContext(), "Server unable to create note set", Toast.LENGTH_LONG).show();
                    }
                    else {
                        noteSet = new Gson().fromJson(response.body(), NoteSet.class);
                        session.addNote(noteSet.getId());

                        updateSession();
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

    private void updateSession() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://frederlen.com:8080")
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        SessionUpdateService service = retrofit.create(SessionUpdateService.class);
        Call<String> call = service.putSession(new Gson().toJson(session));

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {

                if (response.isSuccessful()) {

                    if (response.body().equals("null")) {
                        Toast.makeText(getApplicationContext(), "Server unable to update session", Toast.LENGTH_LONG).show();
                    }
                    else {
                        session = new Gson().fromJson(response.body(), Session.class);
                        Intent intent = new Intent(getBaseContext(), NoteActivity.class);
                        intent.putExtra("SESSION", new Gson().toJson(session));
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
        mNoteForm.setVisibility(View.GONE);
    }

    private void notLoading() {
        mSpinner.setVisibility(View.GONE);
        mNoteForm.setVisibility(View.VISIBLE);
    }
}
