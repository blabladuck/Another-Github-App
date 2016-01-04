package com.nosleep.githubclient.login.ui;

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
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;


import business.DependencyInjector;
import business.service.ServiceInjector;
import business.storage.StorageInjector;

import com.nosleep.githubclient.login.LoginContract;
import com.nosleep.githubclient.login.business.LoginPresenter;
import com.nosleep.githubclient.login.business.OAuthBusiness;
import com.nosleep.githubclient.utils.MasterTemplateActivity;

import com.nosleep.githubclient.R;
import com.nosleep.githubclient.home.WelcomeUserFragment;


public class LoginActivity extends MasterTemplateActivity implements LoginContract.LoginView {
    private TextInputLayout domainTILayout;
    private TextInputLayout emailTILayout;
    private TextInputLayout passwordTILayout;
    private EditText etEmail;
    private EditText etPassword;
    private EditText etDomain;
    private ProgressBar progressBar;
    private View mLoginFormView;
    private boolean isLoginInProgress;
    private LoginContract.UserAction userAction;
    private static final String SAVED_KEY_LOGIN_PROGRESS = "isLoginInProgress";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ServiceInjector serviceInjector = new ServiceInjector(getApplicationContext());
        StorageInjector storageInjector = new StorageInjector(getApplicationContext());
        DependencyInjector injector = new DependencyInjector(serviceInjector, storageInjector);
        userAction = new LoginPresenter(this, injector.getOAuthBusiness());
        if (!userAction.checkUserSessionAvailability()) {
            showLoginScreen();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(SAVED_KEY_LOGIN_PROGRESS, isLoginInProgress);
        super.onSaveInstanceState(outState);
    }

    private void attemptLogin() {
        isLoginInProgress = true;
        emailTILayout.setError(null);
        passwordTILayout.setError(null);

        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();
        String domain = etDomain.getText().toString();
        userAction.attemptLogin(domain, email, password);
    }

    private boolean checkConnectivity() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        boolean result = activeNetwork != null && activeNetwork.isConnected();
        if (!result) {
            final Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "NO CONNECTION", Snackbar.LENGTH_LONG);
            snackbar.setAction("Settings", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(Settings.ACTION_SETTINGS));
                }
            }).show();
        }
        return result;
    }

    @Override
    public void navigateToHomeScreen(OAuthBusiness.Access access) {

        /*Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra("avatar_url", user.getAvatarUrl());
        intent.putExtra("username", user.getName());
        intent.putExtra("myrepos", user.getReposUrl());
        startActivity(intent);*/
        finish();
    }

    @Override
    public void showLoginScreen() {
        setContentView(R.layout.activity_login);
        domainTILayout = (TextInputLayout) findViewById(R.id.domainTILayout);
        domainTILayout.setErrorEnabled(true);
        emailTILayout = (TextInputLayout) findViewById(R.id.emailTILayout);
        emailTILayout.setErrorEnabled(true);
        passwordTILayout = (TextInputLayout) findViewById(R.id.passwordTILayout);
        passwordTILayout.setErrorEnabled(true);
        etEmail = (EditText) findViewById(R.id.email);
        etPassword = (EditText) findViewById(R.id.password);
        etDomain = (EditText) findViewById(R.id.domain);
        mLoginFormView = findViewById(R.id.login_form);
        progressBar = (ProgressBar) findViewById(R.id.login_progress);

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkConnectivity()) {
                    attemptLogin();
                }
            }
        });

    }

    @Override
    public void showWelcomeScreen(String username, String token) {
        Bundle bundle = new Bundle();
        bundle.putString(WelcomeUserFragment.EXTRA_USER_NAME, username);
        bundle.putString(WelcomeUserFragment.EXTRA_TOKEN, token);
        getSupportFragmentManager().beginTransaction().replace(android.R.id.content, WelcomeUserFragment.newInstance(bundle)).
                commit();
    }

    @Override
    public void showUsernameError(@LoginContract.LoginView.InvalidUsernameCode int errorcode) {
        if (errorcode == LoginContract.LoginView.USERNAME_EMPTY) {
            emailTILayout.setError(getString(R.string.error_field_required));
        } else {
            emailTILayout.setError(getString(R.string.error_invalid_email));
        }
        etEmail.requestFocus();
    }

    @Override
    public void showPasswordError(@LoginContract.LoginView.InvalidPasswordCode int errorcode) {
        passwordTILayout.setError(getString(R.string.error_invalid_password));
        etPassword.requestFocus();
    }

    @Override
    public void showDomainError(@LoginContract.LoginView.InvalidDomainCode int errorcode) {
        if (errorcode == LoginContract.LoginView.DOMAIN_EMPTY) {
            domainTILayout.setError(getString(R.string.error_field_required));
            etDomain.requestFocus();
        }
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}

