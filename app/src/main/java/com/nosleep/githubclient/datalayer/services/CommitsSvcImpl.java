package com.nosleep.githubclient.datalayer.services;

import com.android.volley.Request;
import com.nosleep.githubclient.datalayer.services.branches.Branch;
import com.nosleep.githubclient.datalayer.services.commits.Commit;
import com.nosleep.githubclient.datalayer.services.commits.CommitsSvcInterface;
import com.nosleep.githubclient.datalayer.services.repos.Repository;
import com.nosleep.githubclient.utils.ServiceListener;
import com.nosleep.githubclient.utils.VolleyDelegate;

import java.util.HashMap;

/**
 * Created by Sanjeev on 10/01/16.
 */
public class CommitsSvcImpl implements CommitsSvcInterface {

    VolleyDelegate delegate;

    CommitsSvcImpl(VolleyDelegate delegator) {
        this.delegate = delegator;
    }

    @Override
    public void getCommits(String token, String repo, String branch, String owner, ServiceListener<Commit[]> serviceListener) {
        String modified = PATH.replace(":owner", owner).replace(":repo", repo);
        String endpoint = "https://" + "api.github.com" + modified;
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Authorization", token);
        VolleyDelegate.VolleyRequestBuilder<Repository> builder = new VolleyDelegate.VolleyRequestBuilder<>();
        Request<Repository> request = builder
                .method(Request.Method.GET)
                .url(endpoint)
                .headers(headers)
                .gsonClass(Commit[].class)
                .listener(serviceListener)
                .errorListener(serviceListener)
                .build();
        delegate.addToRequestQueue(request);
    }
}
