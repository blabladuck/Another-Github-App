package github.blabladuck.com.another_github_app;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;

public class LoginActivity extends AppCompatActivity {

    private TextInputLayout emailTILayout;
    private TextInputLayout passwordTILayout;
    private EditText etEmail;
    private EditText etPassword;
    private ProgressBar progressBar;
    private View mLoginFormView;
    private boolean isLoginInProgress;
    private static final String SAVED_KEY_LOGIN_PROGRESS = "isLoginInProgress";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailTILayout = (TextInputLayout) findViewById(R.id.emailTILayout);
        emailTILayout.setErrorEnabled(true);
        passwordTILayout = (TextInputLayout) findViewById(R.id.passwordTILayout);
        passwordTILayout.setErrorEnabled(true);
        etEmail = (EditText) findViewById(R.id.email);
        etPassword = (EditText) findViewById(R.id.password);
        mLoginFormView = findViewById(R.id.login_form);
        progressBar = (ProgressBar) findViewById(R.id.login_progress);

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkConnectivity()) {
                    attemptLogin();
                }
            }
        });
        if (savedInstanceState != null) {
            isLoginInProgress = savedInstanceState.getBoolean(SAVED_KEY_LOGIN_PROGRESS);
            showProgress(isLoginInProgress);
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(SAVED_KEY_LOGIN_PROGRESS, isLoginInProgress);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
    }


    private void attemptLogin() {
        isLoginInProgress = true;
        emailTILayout.setError(null);
        passwordTILayout.setError(null);

        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();

        boolean isInValidPassword = false;
        boolean isInvalidEmail = false;

        if (TextUtils.isEmpty(password) || !isPasswordValid(password)) {
            passwordTILayout.setError(getString(R.string.error_invalid_password));
            isInValidPassword = true;
        }

        if (TextUtils.isEmpty(email)) {
            emailTILayout.setError(getString(R.string.error_field_required));
            isInvalidEmail = true;
        } else if (!isEmailValid(email)) {
            emailTILayout.setError(getString(R.string.error_invalid_email));
            isInvalidEmail = true;
        }

        if (isInValidPassword) {
            etPassword.requestFocus();
        } else if (isInvalidEmail) {
            etEmail.requestFocus();
        } else {
            showProgress(true);

        }
    }

    private boolean checkConnectivity() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        boolean result = activeNetwork !=null && activeNetwork.isConnected();
        if (!result) {
            final Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "NO CONNECTION", Snackbar.LENGTH_LONG);
            snackbar.setAction("Settings", new OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(Settings.ACTION_SETTINGS));
                }
            }).show();
        }
        return result;
    }

    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }


    private void showProgress(final boolean show) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(passwordTILayout.getWindowToken(), 0);

        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });

        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        progressBar.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });

    }


}

