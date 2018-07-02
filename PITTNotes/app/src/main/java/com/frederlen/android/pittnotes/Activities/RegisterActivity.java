package com.frederlen.android.pittnotes.Activities;

import com.frederlen.android.pittnotes.R;
import com.frederlen.android.pittnotes.RetrofitRestApi.UserNameService;
import com.frederlen.android.pittnotes.RetrofitRestApi.UserCreateService;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.frederlen.android.pittnotes.models.User;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;



public class RegisterActivity extends AppCompatActivity {

    // UI references.
    private EditText mUsernameView;
    private EditText mEmailView;
    private EditText mPasswordView;
    private EditText mConfirmView;
    private View mRegisterFormView;
    private ProgressBar mSpinner;
    private int result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Set up the register form.
        mUsernameView = (EditText) findViewById(R.id.reg_username);

        mEmailView = (EditText) findViewById(R.id.reg_email);

        mPasswordView = (EditText) findViewById(R.id.reg_password);
        mPasswordView.setTransformationMethod(new PasswordTransformationMethod());


        mConfirmView = (EditText) findViewById(R.id.reg_password_confirm);
        mConfirmView.setTransformationMethod(new PasswordTransformationMethod());
        mConfirmView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptRegister();
                    return true;
                }
                return false;
            }
        });

        Button mCreateRegisterButton = (Button) findViewById(R.id.create_register_button);

        mCreateRegisterButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptRegister();
            }
        });

        mRegisterFormView = findViewById(R.id.register_form);
        mSpinner = (ProgressBar) findViewById(R.id.register_progressBar);

        notLoading();
    }

    private void attemptRegister() {

        // Store values at the time of the login attempt.
        final String username = mUsernameView.getText().toString();
        final String email = mEmailView.getText().toString();
        final String password = mPasswordView.getText().toString();
        String confirm = mConfirmView.getText().toString();

        boolean cancel = false;



        // Check for a valid password, if the user entered one.
        if (password.isEmpty() || !isPasswordValid(password)) {
            Toast.makeText(getApplicationContext(),"Password is invalid",Toast.LENGTH_LONG).show();
            cancel = true;
        }

        // Check passwords match, if the user entered one.
        if (confirm.isEmpty() || !confirm.equals(password)) {
            Toast.makeText(getApplicationContext(),"Passwords do not match",Toast.LENGTH_LONG).show();
            cancel = true;
        }

        // Check for a valid email address.
        if (email.isEmpty() || !isEmailValid(email)) {
            Toast.makeText(getApplicationContext(),"All fields are required",Toast.LENGTH_LONG).show();
            cancel = true;
        }

        // Check for a valid user name.

        if (username.isEmpty()) {
            Toast.makeText(getApplicationContext(),"All fields are required",Toast.LENGTH_LONG).show();
            cancel = true;
        } else {
            int flag = isUsernameTaken(username);

            if (flag == 1) {
                Toast.makeText(getApplicationContext(),"Username is taken",Toast.LENGTH_LONG).show();
                cancel = true;
            } else if (flag == -1) {
                cancel = true;
            }
        }

        if (!cancel) {
            loading();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://frederlen.com:8080")
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .build();
            UserCreateService service = retrofit.create(UserCreateService.class);
            Call<String> call = service.postUserName(
                    new Gson().toJson(new User(username, email, password)));

            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, retrofit2.Response<String> response) {

                    if (response.isSuccessful()) {
                        User user = new Gson().fromJson(response.body(), User.class);

                        if (!response.body().equals("null")) {

                            Intent intent = new Intent(getBaseContext(), MainActivity.class);
                            intent.putExtra("USER", new Gson().toJson(user));
                            startActivity(intent);
                        } else {
                            Toast.makeText(getApplicationContext(),"Server unable to create course",Toast.LENGTH_LONG).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    notLoading();
                    Toast.makeText(getApplicationContext(),"Server Error",Toast.LENGTH_LONG).show();
                }
            });
        }


    }

    private boolean isEmailValid(String email) {
        if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return true;
        }
        return false;
    }

    private boolean isPasswordValid(String password) {

        return password.length() > 3;
    }

    private int isUsernameTaken(String username) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://frederlen.com:8080")
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        UserNameService service = retrofit.create(UserNameService.class);
        Call<String> call = service.getUserName(username);

        loading();

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {

                if (response.isSuccessful()) {

                    notLoading();

                    if (!response.body().equals("null")) {
                        result = 1;
                    } else {
                        result = 0;
                    }
                } else {
                    Toast.makeText(getApplicationContext(),"Server Error",Toast.LENGTH_LONG).show();
                    result = -1;
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

                notLoading();

                Toast.makeText(getApplicationContext(),"Server Error",Toast.LENGTH_LONG).show();
                result = -1;
            }
        });

        return result;
    }

    private void loading() {
        mSpinner.setVisibility(View.VISIBLE);
        mRegisterFormView.setVisibility(View.GONE);
    }

    private void notLoading() {
        mSpinner.setVisibility(View.GONE);
        mRegisterFormView.setVisibility(View.VISIBLE);
    }
}
