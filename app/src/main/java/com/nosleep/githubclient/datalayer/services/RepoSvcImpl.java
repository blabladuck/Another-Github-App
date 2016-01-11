package com.nosleep.githubclient.datalayer.services;

import com.android.volley.Request;

import java.util.HashMap;

import com.nosleep.githubclient.datalayer.services.repos.RepoSvcInterface;
import com.nosleep.githubclient.datalayer.services.repos.Repository;
import com.nosleep.githubclient.utils.ServiceListener;
import com.nosleep.githubclient.utils.VolleyDelegate;

/**
 * Created by ssub3 on 1/4/16.
 */
class RepoSvcImpl implements RepoSvcInterface {

    VolleyDelegate delegator;

    RepoSvcImpl(VolleyDelegate delegator) {
        this.delegator = delegator;
    }

    @Override
    public void getMyRepos(String token, ServiceListener<Repository[]> listener) {
        String endpoint = "https://" + "api.github.com" + PATH;
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer "+token);
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
