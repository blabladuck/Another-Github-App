package com.nosleep.githubclient.datalayer.services.repos;

import com.android.volley.Response;

/**
 * Created by ssub3 on 1/4/16.
 */
public interface RepoSvcInterface {

    String PATH = "/user/repos";

    interface ServiceListener<T> extends Response.Listener<T>, Response.ErrorListener {

    }

    void getMyRepos(String token, ServiceListener<Repository[]> listener);
}
