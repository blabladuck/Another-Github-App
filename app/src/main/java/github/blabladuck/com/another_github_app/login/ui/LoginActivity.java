package github.blabladuck.com.another_github_app.login.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;


import buisness.DependencyInjector;
import github.blabladuck.com.another_github_app.R;
import github.blabladuck.com.another_github_app.login.presenter.LoginContract;
import github.blabladuck.com.another_github_app.login.presenter.LoginPresenter;


public class LoginActivity extends AppCompatActivity implements LoginContract.View {

    private TextInputLayout emailTILayout;
    private TextInputLayout passwordTILayout;
    private EditText etEmail;
    private EditText etPassword;
    private ProgressBar progressBar;
    private View mLoginFormView;
    private boolean isLoginInProgress;
    private static final String SAVED_KEY_LOGIN_PROGRESS = "isLoginInProgress";
    LoginContract.UserAction userAction;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        userAction = new LoginPresenter(this, DependencyInjector.getGithubBusinessInterface());

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
            toggleProgressbar(isLoginInProgress);
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
        userAction.attemptLogin(email,password);
    }

    private boolean checkConnectivity() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        boolean result = activeNetwork != null && activeNetwork.isConnected();
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

    @Override
    public void showUsernameError(@InvalidUsernameCode int errorcode) {
        if (errorcode == LoginContract.View.USERNAME_EMPTY) {
            emailTILayout.setError(getString(R.string.error_field_required));
        } else {
            emailTILayout.setError(getString(R.string.error_invalid_email));
        }
        etEmail.requestFocus();
    }

    @Override
    public void showPasswordError(@InvalidPasswordCode int errorcode) {
        passwordTILayout.setError(getString(R.string.error_invalid_password));
        etPassword.requestFocus();
    }

    @Override
    public void toggleProgressbar(final boolean show) {
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

