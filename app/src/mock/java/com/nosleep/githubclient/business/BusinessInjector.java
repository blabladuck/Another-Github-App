package com.nosleep.githubclient.business;

import android.content.Context;

import com.nosleep.githubclient.datalayer.services.ServiceInjector;
import com.nosleep.githubclient.datalayer.storage.StorageInjector;

/**
 * Created by Sanjeev on 27/12/15.
 */
public class BusinessInjector {
    private ServiceInjector serviceInjector;
    private StorageInjector storageInjector;
    private static BusinessInjector instance;
    private OAuthBusiness oAuthBusiness;
    private UserProfile userProfile;
    private MyRepos myRepos;


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

    public OAuthBusiness getOAuthBusiness() {
        if (oAuthBusiness == null) {
            oAuthBusiness = new OAuthBusiness.OAuthBusinessImpl(serviceInjector.getLoginSvcInterface(), storageInjector.getMemoryStorage(), storageInjector.getPreferenceStorage());
        }
        return oAuthBusiness;
    }

    public UserProfile getUserProfile() {
        if (userProfile == null) {
            userProfile = new UserProfile.UserProfileImpl(serviceInjector.getUserSvcInterface(), storageInjector.getMemoryStorage(), storageInjector.getPreferenceStorage());
        }
        return userProfile
    }

    public MyRepos getReposBusiness() {
        if (myRepos == null) {
            myRepos = new MyRepos.MyReposImpl(serviceInjector.getRepoSvcInterface(), storageInjector.getMemoryStorage(), storageInjector.getPreferenceStorage());
        }
        return myRepos;
    }
}
