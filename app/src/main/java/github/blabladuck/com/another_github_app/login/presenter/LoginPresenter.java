package github.blabladuck.com.another_github_app.login.presenter;

import android.os.Bundle;

import business.OAuthBusiness;

/**
 * Created by Sanjeev on 27/12/15.
 */
public class LoginPresenter implements LoginContract.UserAction {


    private static final String TAG = "LoginPresenter";
    private final LoginContract.LoginView view;
    private final OAuthBusiness oAuthBusiness;
    private OAuthBusiness.Access accessPojo;

    public LoginPresenter(LoginContract.LoginView view, OAuthBusiness oAuthBusiness) {
        this.view = view;
        this.oAuthBusiness = oAuthBusiness;
    }


    @Override
    public void checkUserSessionAvailability() {
        OAuthBusiness.Access access = oAuthBusiness.getUserAccessCache();
        if(access!=null){
            view.showWelcomeScreen(access.username,access.token);
        }
    }

    @Override
    public void attemptLogin(String domain, final String username, String password) {
        if (isDomainValid(domain) && isEmailValid(username) && isPasswordValid(password)) {
            view.toggleProgressbar(true);
            oAuthBusiness.login(domain, username, password, new OAuthBusiness.LoginCallback() {

                @Override
                public void onLoginSuccess(OAuthBusiness.Access access) {
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
