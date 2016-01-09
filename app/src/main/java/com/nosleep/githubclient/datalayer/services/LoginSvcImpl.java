package com.nosleep.githubclient.datalayer.services;

import android.util.Base64;
import android.util.Log;

import com.android.volley.Request;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import com.google.gson.Gson;
import com.nosleep.githubclient.datalayer.services.authorization.Authorizations;
import com.nosleep.githubclient.datalayer.services.authorization.LoginBody;
import com.nosleep.githubclient.datalayer.services.authorization.LoginSvcInterface;
import com.nosleep.githubclient.utils.EndPoints;
import com.nosleep.githubclient.utils.ServiceListener;
import com.nosleep.githubclient.utils.VolleyDelegate;

/**
 * Created by Sanjeev on 03/01/16.
 */
class LoginSvcImpl implements LoginSvcInterface {

    private static final String TAG = "LoginSvcImpl";
    private VolleyDelegate delegate;

    private String str = "{\"scopes\": [\"repo\", \"user\"], \"note\": \"test\"}";

    LoginSvcImpl(VolleyDelegate delegate) {
        this.delegate = delegate;
    }

    @Override
    public void login(String username, String password, ServiceListener callback) {
        VolleyDelegate.VolleyRequestBuilder<Authorizations> builder = new VolleyDelegate.VolleyRequestBuilder<>();
        HashMap<String, String> headers = new HashMap<>();
        String authorization = "Basic " + Base64.encodeToString((username + ":" + password).getBytes(), Base64.DEFAULT);
        String accept = "application/vnd.github.v3+json";
        Log.d("GithubServicesAPI", authorization);
        headers.put("Authorization", authorization);
        headers.put("Content-Type", "application/json");
        headers.put("Accept", accept);

        LoginBody body = new LoginBody();
        List<String> scope = new ArrayList<>();
        scope.add("public_repo");
        scope.add("repo");
        scope.add("user");
        body.setScopes(scope);
        body.setNote(Calendar.getInstance().getTime().toString());
        Gson gson = new Gson();
        String str = gson.toJson(body, LoginBody.class);
        Log.d(TAG, str);

        Request<Authorizations> request = builder.method(Request.Method.POST)
                .url(EndPoints.getEndpoint().constructURL(PATH_AUTHORIZATION))
                .headers(headers).requestBody(str)
                .listener(callback)
                .errorListener(callback)
                .gsonClass(Authorizations.class)
                .build();
        delegate.addToRequestQueue(request);
    }

}
