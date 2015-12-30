package buisness;

import buisness.models.User;

/**
 * Created by Sanjeev on 27/12/15.
 */
public interface GithubServiceApi {

    interface onServiceComplete<U> {
        void onSuccess(U response);

        void onFailure();
    }

    void login(String username, String password, onServiceComplete<User> callback);
}
