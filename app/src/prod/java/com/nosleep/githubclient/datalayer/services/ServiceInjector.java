package com.nosleep.githubclient.datalayer.services;

import android.content.Context;

import com.nosleep.githubclient.datalayer.services.authorization.LoginSvcInterface;
import com.nosleep.githubclient.datalayer.services.branches.BranchesSvcInterface;
import com.nosleep.githubclient.datalayer.services.commits.CommitsSvcInterface;
import com.nosleep.githubclient.datalayer.services.repos.RepoSvcInterface;
import com.nosleep.githubclient.datalayer.services.user.UserSvcInterface;
import com.nosleep.githubclient.utils.VolleyDelegate;

/**
 * Created by Sanjeev on 03/01/16.
 */
public class ServiceInjector {
    private static VolleyDelegate volleyDelegate;
    private static LoginSvcInterface loginSvcInterface;
    private static UserSvcInterface userSvcInterface;
    private static BranchesSvcInterface branchesSvcInterface;
    private static RepoSvcInterface repoSvcInterface;
    private CommitsSvcImpl commitsSvcInterface;

    public ServiceInjector(Context appContext) {
        if (volleyDelegate == null) {
            volleyDelegate = VolleyDelegate.getInstance(appContext.getApplicationContext());
        }
    }


    public LoginSvcInterface getLoginSvcInterface() {
        if (loginSvcInterface == null) {
            loginSvcInterface = new LoginSvcImpl(volleyDelegate);
        }
        return loginSvcInterface;
    }


    public UserSvcInterface getUserSvcInterface() {
        if (userSvcInterface == null) {
            userSvcInterface = new UserSvcImpl(volleyDelegate);
        }
        return userSvcInterface;
    }


    public RepoSvcInterface getRepoSvcInterface() {
        if (repoSvcInterface == null) {
            repoSvcInterface = new RepoSvcImpl(volleyDelegate);
        }
        return repoSvcInterface;
    }

    public BranchesSvcInterface getBranchesSvcInterface() {
        if (branchesSvcInterface == null) {
            branchesSvcInterface = new BranchesSvcImpl(volleyDelegate);
        }
        return branchesSvcInterface;
    }

    public CommitsSvcInterface getCommitsSvcInterface() {
        if (commitsSvcInterface == null) {
            commitsSvcInterface = new CommitsSvcImpl(volleyDelegate);
        }
        return commitsSvcInterface;
    }

}
