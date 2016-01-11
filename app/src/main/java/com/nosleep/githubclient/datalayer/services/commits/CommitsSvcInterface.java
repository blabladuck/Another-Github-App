package com.nosleep.githubclient.datalayer.services.commits;

import com.nosleep.githubclient.datalayer.services.repos.Repository;
import com.nosleep.githubclient.utils.ServiceListener;

/**
 * Created by Sanjeev on 10/01/16.
 */
public interface CommitsSvcInterface {
    String PATH = "/repos/:owner/:repo/commits";

    void getCommits(String repo, String branch, String owner, ServiceListener<Commit[]> serviceListener);
}
