package com.nosleep.githubclient.home;

import com.nosleep.githubclient.business.MyRepos;

import java.util.List;

/**
 * Created by ssub3 on 1/5/16.
 */
public interface HomeContract {
    interface WelcomeView {
        void showWelcomeScreen(String username, String avatar);

        void showReposScreen(List<MyRepos.RepoInfo> info);

        void navigateToCommitScreen(String repo, String branch, String owner);
    }

    interface UserAction {
        void getUser();

        void getRepos(boolean forceRefresh);

        void onRepoItemClicked(String repo, String branch, String owner);
    }
}
