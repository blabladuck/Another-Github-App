package buisness;


import android.util.Log;

import com.android.volley.VolleyError;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import service.GithubServiceImpl;
import service.User;

/**
 * Created by Sanjeev on 27/12/15.
 */
public class GithubBusinessImpl implements IGithubBusinessInterface {

    private GithubServiceApi Api;
    private LoginStorage storage;

    public GithubBusinessImpl(GithubServiceImpl githubService, LoginStorage loginStorage) {
        this.Api = githubService;
        this.storage = loginStorage;

    }

    @Override
    public void login(String domain, String username, String password, final LoginCallback callback) {
        Api.login(domain, username, password, new GithubServiceApi.onServiceComplete() {
            @Override
            public void onResponse(User response) {
                callback.onLoginSuccess(response);
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.networkResponse != null) {
                    Log.d("ERROR", "error = " + new String(error.networkResponse.data));
                }
                callback.onLoginFailure();
            }
        });

    }

    @Override
    public LoginStorage getLoginStorage() {
        return storage;
    }
}
