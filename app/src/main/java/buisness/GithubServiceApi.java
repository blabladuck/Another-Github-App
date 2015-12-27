package buisness;

/**
 * Created by Sanjeev on 27/12/15.
 */
public interface GithubServiceApi {
    interface onServiceComplete {
        void onSuccess();

        void onFailure();
    }

    void login(String username, String password, onServiceComplete callback);
}
