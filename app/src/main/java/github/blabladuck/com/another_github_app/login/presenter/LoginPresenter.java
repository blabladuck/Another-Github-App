package github.blabladuck.com.another_github_app.login.presenter;

import android.util.Log;

import buisness.IGithubBusinessInterface;
import buisness.LoginStorage;
import service.User;

/**
 * Created by Sanjeev on 27/12/15.
 */
public class LoginPresenter implements LoginContract.UserAction {


    private static final String TAG = "LoginPresenter";
    private final LoginContract.LoginView view;
    private final IGithubBusinessInterface gitbuisness;

    public LoginPresenter(LoginContract.LoginView view, IGithubBusinessInterface gitbuisness) {
        this.view = view;
        this.gitbuisness = gitbuisness;
    }

    @Override
    public void attemptLogin(String domain, final String username, String password) {
        if (isDomainValid(domain) && isEmailValid(username) && isPasswordValid(password)) {
            view.toggleProgressbar(true);
            gitbuisness.login(domain, username, password, new IGithubBusinessInterface.LoginCallback() {
                @Override
                public void onLoginSuccess(User user) {
                    LoginStorage storage = gitbuisness.getLoginStorage();
                    storage.setUsername(user.getName());
                    storage.setAvatar(user.getAvatarUrl());
                    Log.d(TAG, user.getAvatarUrl());
                    view.toggleProgressbar(false);
                    view.navigateToHomeScreen(user);
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
