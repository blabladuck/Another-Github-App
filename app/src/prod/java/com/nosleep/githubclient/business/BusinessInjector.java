package com.nosleep.githubclient.business;

import android.content.Context;
import android.support.v4.app.LoaderManager;

import com.nosleep.githubclient.datalayer.services.ServiceInjector;
import com.nosleep.githubclient.datalayer.storage.StorageInjector;

/**
 * Created by Sanjeev on 27/12/15.
 */
public class BusinessInjector {
    private ServiceInjector serviceInjector;
    private StorageInjector storageInjector;
    private static BusinessInjector instance;
    private OAuth oAuth;
    private UserProfile userProfile;
    private MyRepos myRepos;
    private BranchCommits branchCommits;
    private MyBranch myBranch;


    public static synchronized BusinessInjector getInstance(Context context) {
        if (instance == null) {
            instance = new BusinessInjector(context.getApplicationContext());
        }
        return instance;
    }

    private BusinessInjector(Context context) {
        serviceInjector = new ServiceInjector(context);
        storageInjector = new StorageInjector(context);
    }

    public OAuth getOAuthBusiness() {
        if (oAuth == null) {
            oAuth = new OAuth.OAuthImpl(serviceInjector.getLoginSvcInterface(), storageInjector.getMemoryStorage(), storageInjector.getPreferenceStorage());
        }
        return oAuth;
    }

    public UserProfile getUserProfile() {
        if (userProfile == null) {
            userProfile = new UserProfile.UserProfileImpl(serviceInjector.getUserSvcInterface(), storageInjector.getMemoryStorage(), storageInjector.getPreferenceStorage());
        }
        return userProfile;
    }

    public MyRepos getReposBusiness() {
        if (myRepos == null) {
            myRepos = new MyRepos.MyReposImpl(serviceInjector.getRepoSvcInterface(), storageInjector.getMemoryStorage(), storageInjector.getPreferenceStorage());
        }
        return myRepos;
    }

    public BranchCommits getBranchCommits(Context appContext, LoaderManager loaderManager) {
        if (branchCommits == null) {
            branchCommits = new BranchCommits.BranchCommitsImpl(appContext.getApplicationContext(), loaderManager, serviceInjector.getCommitsSvcInterface(), storageInjector.getMemoryStorage());
        }
        return branchCommits;
    }

    public MyBranch getBranchesBusiness() {
        if (myBranch == null) {
            myBranch = new MyBranch.MyBranchImpl(serviceInjector.getBranchesSvcInterface(), storageInjector.getMemoryStorage(), storageInjector.getPreferenceStorage());
        }
        return myBranch;
    }
}
