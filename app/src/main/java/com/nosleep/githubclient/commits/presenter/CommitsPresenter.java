package com.nosleep.githubclient.commits.presenter;

import com.nosleep.githubclient.business.BranchCommits;
import com.nosleep.githubclient.datalayer.services.commits.Commit;

import java.util.List;

/**
 * Created by Sanjeev on 09/01/16.
 */
public class CommitsPresenter implements BranchCommits.CommitsLoadCallback{

    public CommitsPresenter(){

    }
    @Override
    public void onLoadCommits(int loadStatus, List<BranchCommits.CommitData> info) {

    }
}
