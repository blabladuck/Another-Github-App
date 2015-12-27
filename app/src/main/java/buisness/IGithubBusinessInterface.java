package buisness;

/**
 * Created by Sanjeev on 27/12/15.
 */
public interface IGithubBusinessInterface {
    interface LoginCallback {
        void onLoginSuccess();

        void onLoginFailure();
    }

    void login(String username, String password, LoginCallback callback);
}
