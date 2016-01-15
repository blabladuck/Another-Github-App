package com.nosleep.githubclient.datalayer.services;

import com.android.volley.Request;

import java.util.HashMap;

import com.nosleep.githubclient.datalayer.services.branches.Branch;
import com.nosleep.githubclient.datalayer.services.branches.BranchesSvcInterface;
import com.nosleep.githubclient.datalayer.services.repos.Repository;
import com.nosleep.githubclient.utils.ServiceListener;
import com.nosleep.githubclient.utils.VolleyDelegate;

/**
 * Created by ssub3 on 1/4/16.
 */
class BranchesSvcImpl implements BranchesSvcInterface {

    VolleyDelegate delegate;

    BranchesSvcImpl(VolleyDelegate delegate) {
        this.delegate = delegate;
    }

    @Override
    public void getBranches(String token, String repo, String owner, ServiceListener<Branch[]> listener) {
        String modified = PATH.replace(":owner", owner).replace(":repo", repo);
        String endpoint = "https://" + "api.github.com" + modified;
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer 76e958180df7d140698e6aabce10bf7ccad8ff22");
        VolleyDelegate.VolleyRequestBuilder<Repository> builder = new VolleyDelegate.VolleyRequestBuilder<>();
        Request<Repository> request = builder
                .method(Request.Method.GET)
                .url(endpoint)
                .headers(headers)
                .gsonClass(Branch[].class)
                .listener(listener)
                .errorListener(listener)
                .build();
        delegate.addToRequestQueue(request);
    }
}
