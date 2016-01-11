package com.nosleep.githubclient.datalayer.services.repos;

import com.android.volley.Response;
import com.nosleep.githubclient.utils.ServiceListener;

/**
 * Created by ssub3 on 1/4/16.
 */
public interface RepoSvcInterface {

    String PATH = "/user/repos";

    void getMyRepos(String token, ServiceListener<Repository[]> listener);
}
