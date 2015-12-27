package buisness;


import android.os.Handler;

/**
 * Created by Sanjeev on 27/12/15.
 */
public class GithubBusinessImpl implements IGithubBusinessInterface {

    private GithubServiceApi Api;

    public GithubBusinessImpl(GithubServiceApi Api) {
        this.Api = Api;
    }

    @Override
    public void login(String username, String password, final LoginCallback callback) {
        Api.login(username, password, new GithubServiceApi.onServiceComplete() {
            @Override
            public void onSuccess() {
                callback.onLoginSuccess();
            }

            @Override
            public void onFailure() {
                callback.onLoginFailure();
            }
        });

    }
}
