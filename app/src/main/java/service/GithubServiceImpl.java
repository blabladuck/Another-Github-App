package service;

import android.content.Context;
import android.util.Base64;
import android.util.Log;

import com.android.volley.Request;

import java.util.HashMap;

import buisness.GithubServiceApi;

/**
 * Created by ssub3 on 12/28/15.
 */
public class GithubServiceImpl implements GithubServiceApi {
    private VolleyDelegator delegator;

    public GithubServiceImpl(Context context) {
        delegator = VolleyDelegator.getInstance(context);
    }

    @Override
    public void login(String domain, String username, String password, onServiceComplete callback) {
        VolleyDelegator.VolleyRequestBuilder<User> builder = new VolleyDelegator.VolleyRequestBuilder<User>();
        HashMap<String,String> headers = new HashMap<>();
        String authorization = "Basic "+Base64.encodeToString((username + ":" + password).getBytes(), Base64.DEFAULT);
        Log.d("GithubServiceImpl",authorization);
        headers.put("Authorization", authorization);
        Request<User> request = builder.method(Request.Method.GET)
                .url("https://api.github.com/user")
                .headers(headers)
                .listener(callback)
                .errorListener(callback)
                .gsonClass(User.class)
                .build();
        delegator.addToRequestQueue(request);
    }
}
