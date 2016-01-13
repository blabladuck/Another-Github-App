package com.nosleep.githubclient.commits.presenter;

import com.nosleep.githubclient.business.BranchCommits;
import com.nosleep.githubclient.business.MyRepos;
import com.nosleep.githubclient.business.UserProfile;
import com.nosleep.githubclient.commits.CommitsContract;
import com.nosleep.githubclient.home.HomeContract;

import java.util.List;

/**
 * Created by Sanjeev on 09/01/16.
 */
public class CommitsPresenter implements CommitsContract.UserAction, BranchCommits.CommitsLoadCallback {
    private CommitsContract.CommitsView view;
    private BranchCommits branchCommits;


    public CommitsPresenter(CommitsContract.CommitsView view, BranchCommits branchCommits) {
        this.view = view;
        this.branchCommits = branchCommits;
    }

    @Override
    public void onLoadCommits(int loadStatus, BranchCommits.CommitData[] info) {
        view.showCommitsDataScreen(info);
    }

    @Override
    public void getCommits(String repo, String branch, String owner) {
        view.showBusyInQueryScreen();
        branchCommits.getCommits(repo,branch,owner,null,this);
    }
}
