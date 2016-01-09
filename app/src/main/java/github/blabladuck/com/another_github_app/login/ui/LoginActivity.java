package github.blabladuck.com.another_github_app.login.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;


import buisness.DependencyInjector;
import buisness.IGithubBusinessInterface;
import buisness.LoginStorage;
import github.blabladuck.com.another_github_app.R;
import github.blabladuck.com.another_github_app.login.presenter.LoginContract;
import github.blabladuck.com.another_github_app.login.presenter.LoginPresenter;
import service.User;


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
    LoginContract.UserAction userAction;
    private static final String SAVED_KEY_LOGIN_PROGRESS = "isLoginInProgress";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IGithubBusinessInterface businessInterface = DependencyInjector.getGithubBusinessInterface(getApplicationContext());
        LoginStorage storage = businessInterface.getLoginStorage();
        Fragment frag;
        if (storage.getUsername().isEmpty() && storage.getAvatar().isEmpty()) {
            setContentView(R.layout.activity_login);
            userAction = new LoginPresenter(this, DependencyInjector.getGithubBusinessInterface(getApplicationContext()));
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
            if (savedInstanceState != null) {
                isLoginInProgress = savedInstanceState.getBoolean(SAVED_KEY_LOGIN_PROGRESS);
                toggleProgressbar(isLoginInProgress);
            }
        } else {
            Bundle bundle = new Bundle();
            bundle.putString(WelcomeUserFragment.EXTRA_USER_NAME, storage.getUsername());
            bundle.putString(WelcomeUserFragment.EXTRA_AVATAR_URL, storage.getAvatar());
            //bundle.putString(WelcomeUserFragment.EXTRA_REPOS_URL, storage.getAvatar());
            frag = WelcomeUserFragment.newInstance(bundle);
            getSupportFragmentManager().beginTransaction().replace(android.R.id.content, frag).commit();
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
    public void navigateToHomeScreen(User user) {

        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra("avatar_url", user.getAvatarUrl());
        intent.putExtra("username", user.getName());
        intent.putExtra("myrepos", user.getReposUrl());
        startActivity(intent);
        finish();
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

}
