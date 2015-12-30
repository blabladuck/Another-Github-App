package buisness;

import buisness.models.User;

/**
 * Created by Sanjeev on 27/12/15.
 */
public interface IGithubBusinessInterface {
    interface LoginCallback {
        void onLoginSuccess(User user);

        void onLoginFailure();
    }

    void login(String username, String password, LoginCallback callback);
}
