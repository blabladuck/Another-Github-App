package com.nosleep.githubclient.commits;

import com.nosleep.githubclient.business.BranchCommits;

/**
 * Created by Sanjeev on 13/01/16.
 */
public interface CommitsContract {

    interface CommitsView {
        void showBusyInQueryScreen();

        void showCommitsDataScreen(BranchCommits.CommitData[] commits);
    }

    interface UserAction {
        void getCommits(String repo, String branch, String owner);
    }
}
