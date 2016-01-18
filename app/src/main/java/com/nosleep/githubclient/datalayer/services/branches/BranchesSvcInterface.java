package com.nosleep.githubclient.datalayer.services.branches;

import com.nosleep.githubclient.datalayer.services.repos.RepoSvcInterface;
import com.nosleep.githubclient.utils.ServiceListener;

/**
 * Created by ssub3 on 1/4/16.
 */
public interface BranchesSvcInterface {
    String PATH = "/repos/:owner/:repo/branches";


    void getBranches(String token, String repo, String owner, ServiceListener<Branch[]> listener);
}
