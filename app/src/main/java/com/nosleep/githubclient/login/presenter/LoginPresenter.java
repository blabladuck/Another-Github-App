package com.nosleep.githubclient.login.presenter;

import com.nosleep.githubclient.business.OAuth;
import com.nosleep.githubclient.login.LoginContract;
import com.nosleep.githubclient.utils.EndPoints;

/**
 * Created by Sanjeev on 27/12/15.
 */
public class LoginPresenter implements LoginContract.UserAction {


    private static final String TAG = "LoginPresenter";
    private final LoginContract.LoginView view;
    private final OAuth oAuth;

    public LoginPresenter(LoginContract.LoginView view, OAuth oAuth) {
        this.view = view;
        this.oAuth = oAuth;
    }


    @Override
    public boolean checkUserSessionAvailability() {
        OAuth.Access access = oAuth.getUserAccessCache();
        if (access != null) {
            view.navigateToHomeScreen(access);
            return true;
        }
        return false;
    }

    @Override
    public void attemptLogin(String domain, final String username, String password) {
        if (isDomainValid(domain) && isEmailValid(username) && isPasswordValid(password)) {
            EndPoints.getEndpoint().setDomain(domain);
            view.toggleProgressbar(true);
            oAuth.login(domain, username, password, new OAuth.LoginCallback() {

                @Override
                public void onLoginSuccess(OAuth.Access access) {
                    view.toggleProgressbar(false);
                    view.navigateToHomeScreen(access);
                }

                @Override
                public void onLoginFailure() {
                    view.toggleProgressbar(false);
                }
            });
        }
    }

    private boolean isDomainValid(String domain) {
        if (domain == null || domain.isEmpty()) {
            view.showDomainError(LoginContract.LoginView.DOMAIN_EMPTY);
            return false;
        }
        return true;
    }

    @LoginContract.LoginView.InvalidUsernameCode
    private boolean isEmailValid(String email) {
        boolean result = true;
        if (email == null || email.isEmpty()) {
            view.showUsernameError(LoginContract.LoginView.USERNAME_EMPTY);
            result = false;
        }
        return result;
    }

    private boolean isPasswordValid(String password) {
        boolean result = true;
        if (password.length() < 4) {
            result = false;
            view.showPasswordError(LoginContract.LoginView.PASSWORD_INVALID);
        }
        return result;
    }
}
