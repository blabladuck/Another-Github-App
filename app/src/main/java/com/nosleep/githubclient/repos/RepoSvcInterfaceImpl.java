package com.nosleep.githubclient.repos;

import com.android.volley.Request;

import java.util.HashMap;

import com.nosleep.githubclient.utils.VolleyDelegate;

/**
 * Created by ssub3 on 1/4/16.
 */
public class RepoSvcInterfaceImpl implements RepoSvcInterface {

    VolleyDelegate delegator;

    public RepoSvcInterfaceImpl(VolleyDelegate delegator) {
        this.delegator = delegator;
    }

    @Override
    public void getMyRepos(String token, ServiceListener<Repository[]> listener) {
        String endpoint = "https://" + "api.github.com" + PATH;
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer 76e958180df7d140698e6aabce10bf7ccad8ff22");
        VolleyDelegate.VolleyRequestBuilder<Repository> builder = new VolleyDelegate.VolleyRequestBuilder<>();
        Request<Repository> request = builder
                .method(Request.Method.GET)
                .url(endpoint)
                .headers(headers)
                .gsonClass(Repository[].class)
                .listener(listener)
                .errorListener(listener)
                .build();
        delegator.addToRequestQueue(request);
    }
}
