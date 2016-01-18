package com.nosleep.githubclient.branches;

import com.nosleep.githubclient.business.MyBranch;

import java.util.List;

public interface BranchesContract {

    interface BranchesView {
        void showBranches(List<MyBranch.BranchInfo> branchInfoList);

        void navigateToCommitScreen(String repoName, String repoOwner, String branchName);
    }

    interface UserAction {
        void getBranches(String repoName, String repoOwnerName);

        void onBranchViewClicked(String repoName, String repoOwner, String name);
    }
}
