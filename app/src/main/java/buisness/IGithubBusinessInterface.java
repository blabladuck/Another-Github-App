package buisness;

import service.User;

/**
 * Created by Sanjeev on 27/12/15.
 */
public interface IGithubBusinessInterface {
    interface LoginCallback {
        void onLoginSuccess(User user);

        void onLoginFailure();
    }


    void login(String domain, String username, String password, LoginCallback callback);

    LoginStorage getLoginStorage();
}
