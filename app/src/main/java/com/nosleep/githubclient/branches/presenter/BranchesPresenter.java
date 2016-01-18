package com.nosleep.githubclient.branches.presenter;

import com.nosleep.githubclient.branches.BranchesContract;
import com.nosleep.githubclient.business.MyBranch;

import java.util.List;

public class BranchesPresenter implements BranchesContract.UserAction {

    private final BranchesContract.BranchesView branchesView;
    private final MyBranch myBranch;

    public BranchesPresenter(BranchesContract.BranchesView branchesView, MyBranch myBranch) {
        this.branchesView = branchesView;
        this.myBranch = myBranch;
    }

    @Override
    public void getBranches(String repoName, String repoOwnerName) {
        myBranch.getBranchesForRepo(repoName, repoOwnerName, new MyBranch.BranchesFetchCallback() {
            @Override
            public void onLoadBranchesForARepo(int loadStatus, List<MyBranch.BranchInfo> info) {
                branchesView.showBranches(info);
            }
        });
    }

    @Override
    public void onBranchViewClicked(String repoName, String repoOwner, String branchName) {
        branchesView.navigateToCommitScreen(repoName, repoOwner, branchName);
    }

}
