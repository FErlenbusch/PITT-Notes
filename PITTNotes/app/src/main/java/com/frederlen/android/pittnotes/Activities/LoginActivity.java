package com.frederlen.android.pittnotes.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import com.frederlen.android.pittnotes.R;
import com.frederlen.android.pittnotes.models.User;
import com.frederlen.android.pittnotes.RetrofitRestApi.UserEmailService;

import android.os.Bundle;
import android.text.TextUtils;
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

import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;



public class LoginActivity extends AppCompatActivity {

    // UI references.
    private EditText mEmailView;
    private EditText mPasswordView;
    private View mLoginFormView;
    private ProgressBar mSpinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmailView = (EditText) findViewById(R.id.email);

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setTransformationMethod(new PasswordTransformationMethod());
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        Button mEmailRegisterButton = (Button) findViewById(R.id.email_register_button);

        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mEmailRegisterButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mSpinner = (ProgressBar) findViewById(R.id.login_progressBar);

        notLoading();
    }

    private void attemptLogin() {

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        final String password = mPasswordView.getText().toString();

        boolean cancel = false;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            Toast.makeText(getApplicationContext(),"Password is invalid",Toast.LENGTH_LONG).show();
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(),"Email is required",Toast.LENGTH_LONG).show();
            cancel = true;
        } else if (!isEmailValid(email)) {
            Toast.makeText(getApplicationContext(),"Email is invalid",Toast.LENGTH_LONG).show();
            cancel = true;
        }

        if (!cancel) {

            loading();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://frederlen.com:8080")
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .build();
            UserEmailService service = retrofit.create(UserEmailService.class);
            Call<String> call = service.getUserEmail(email);

            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {

                    if (response.isSuccessful()) {
                        User user = new Gson().fromJson(response.body(), User.class);

                        if (!response.body().equals("null")) {
                            if (user.getPassword().equals(password)) {
                                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                                intent.putExtra("USER", new Gson().toJson(user));
                                startActivity(intent);
                            } else {
                                Toast.makeText(getApplicationContext(),"Invalid password",Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(),"User does not exist",Toast.LENGTH_LONG).show();
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

    private boolean isEmailValid(String email) {
        if (!TextUtils.isEmpty(email)) {
            if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                return true;
            }
        }
        return false;
    }

    private boolean isPasswordValid(String password) {

        return password.length() > 3;
    }

    private void loading() {
        mSpinner.setVisibility(View.VISIBLE);
        mLoginFormView.setVisibility(View.GONE);
    }

    private void notLoading() {
        mSpinner.setVisibility(View.GONE);
        mLoginFormView.setVisibility(View.VISIBLE);
    }
}

