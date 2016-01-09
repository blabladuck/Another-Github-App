package com.nosleep.githubclient.datalayer.services.branches;

import com.android.volley.Response;
import com.nosleep.githubclient.datalayer.services.branches.Branch;

/**
 * Created by ssub3 on 1/4/16.
 */
public interface BranchesSvcInterface {
    String PATH = "/repos/:owner/:repo/com.nosleep.githubclient.branches";

    interface ServiceListener<T> extends Response.Listener<T>, Response.ErrorListener {
    }

    void getBranches(String token, String repo, String owner, ServiceListener<Branch[]> listener);
}
