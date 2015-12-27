package github.blabladuck.com.another_github_app.login.presenter;

import buisness.IGithubBusinessInterface;

/**
 * Created by Sanjeev on 27/12/15.
 */
public class LoginPresenter implements LoginContract.UserAction {


    private final LoginContract.View view;
    private final IGithubBusinessInterface gitbuisness;

    public LoginPresenter(LoginContract.View view, IGithubBusinessInterface gitbuisness) {
        this.view = view;
        this.gitbuisness = gitbuisness;
    }

    @Override
    public void attemptLogin(String username, String password) {
        if(isEmailValid(username) && isPasswordValid(password)){
            view.toggleProgressbar(true);
            gitbuisness.login(username, password, new IGithubBusinessInterface.LoginCallback() {
                @Override
                public void onLoginSuccess() {
                    view.toggleProgressbar(false);
                }

                @Override
                public void onLoginFailure() {
                    view.toggleProgressbar(false);
                }
            });
        }
    }

    @LoginContract.View.InvalidUsernameCode
    private boolean isEmailValid(String email) {
        boolean result = true;
        if (email == null && email.isEmpty()) {
            view.showUsernameError(LoginContract.View.USERNAME_EMPTY);
            result = false;
        } else if (!email.contains("@") || !email.contains(".")) {
            view.showUsernameError(LoginContract.View.USERNAME_INVALID);
            result = false;
        }

        return result;
    }

    private boolean isPasswordValid(String password) {
        boolean result = true;
        if (password.length() < 4) {
            result = false;
            view.showPasswordError(LoginContract.View.PASSWORD_INVALID);
        }
        return result;
    }
}
