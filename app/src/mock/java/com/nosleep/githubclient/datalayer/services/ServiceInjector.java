package com.nosleep.githubclient.datalayer.services;

import android.content.Context;

import com.nosleep.githubclient.datalayer.services.authorization.LoginSvcInterface;
import com.nosleep.githubclient.datalayer.services.branches.BranchesSvcInterface;
import com.nosleep.githubclient.datalayer.services.repos.RepoSvcInterface;
import com.nosleep.githubclient.datalayer.services.user.UserSvcInterface;
import com.nosleep.githubclient.utils.VolleyDelegate;

/**
 * Created by Sanjeev on 03/01/16.
 */
public class ServiceInjector {
    private VolleyDelegate volleyDelegate;
    private static LoginSvcInterface loginSvcInterface;
    private static UserSvcInterface userSvcInterface;
    private static BranchesSvcInterface branchesSvcInterface;
    private static RepoSvcInterface repoSvcInterface;

    public ServiceInjector(Context appContext) {
        volleyDelegate = VolleyDelegate.getInstance(appContext.getApplicationContext());
    }

    public static void setMockLoginSvcInterface(LoginSvcInterface mock) {
        loginSvcInterface = mock;
    }


    public static void setMockUserSvcInterface(UserSvcInterface mock) {
        userSvcInterface = mock;
    }


    public static void setMockBranchesSvcInterface(RepoSvcInterface mock) {
        repoSvcInterface = mock;
    }


    public static void setMockBranchesSvcInterface(BranchesSvcInterface mock) {
        branchesSvcInterface = mock;
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

}
