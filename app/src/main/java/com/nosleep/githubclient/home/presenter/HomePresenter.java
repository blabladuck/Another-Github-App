package com.nosleep.githubclient.home.presenter;

import com.nosleep.githubclient.business.MyRepos;
import com.nosleep.githubclient.business.UserProfile;
import com.nosleep.githubclient.home.HomeContract;

import java.util.List;

/**
 * Created by ssub3 on 1/5/16.
 */
public class HomePresenter implements HomeContract.UserAction {
    private HomeContract.WelcomeView homeview;
    private UserProfile userProfile;
    private MyRepos myRepos;

    public HomePresenter(HomeContract.WelcomeView homeview, UserProfile userProfile, MyRepos myRepos) {
        this.homeview = homeview;
        this.userProfile = userProfile;
        this.myRepos = myRepos;
    }

    @Override
    public void getUser() {
        userProfile.getUserProfile(new UserProfile.UserProfileCallback() {
            @Override
            public void onSuccess(UserProfile.UserData user) {
                homeview.showWelcomeScreen(user.name, user.avatarURL);
            }

            @Override
            public void onFailure() {

            }
        });
    }

    public void getRepos(boolean forceRefresh) {
        if (forceRefresh) {
            myRepos.refresh();
        }
        myRepos.getRepos(new MyRepos.RepoLoadCallback() {
            @Override
            public void onLoadRepos(int loadStatus, List<MyRepos.RepoInfo> info) {
                homeview.showReposScreen(info);
            }
        });
    }

    @Override
    public void onRepoItemClicked(String repo, String owner) {
        homeview.navigateToBranchesScreen(repo, owner);
    }

}
