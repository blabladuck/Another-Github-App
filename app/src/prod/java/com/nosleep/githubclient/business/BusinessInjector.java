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
    private UserProfileBusiness userProfileBusiness;
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
            oAuthBusiness = new OAuthBusinessImpl(serviceInjector.getLoginSvcInterface(), storageInjector.getMemoryStorage(), storageInjector.getPreferenceStorage());
        }
        return oAuthBusiness;
    }

    public UserProfileBusiness getUserProfileBusiness() {
        if (userProfileBusiness == null) {
            userProfileBusiness = new UserProfileBusinessImpl(serviceInjector.getUserSvcInterface(), storageInjector.getMemoryStorage(), storageInjector.getPreferenceStorage());
        }
        return userProfileBusiness;
    }

    public MyRepos getReposBusiness() {
        if (myRepos == null) {
            myRepos = new MyReposImpl(serviceInjector.getRepoSvcInterface(), storageInjector.getMemoryStorage(), storageInjector.getPreferenceStorage());
        }
        return myRepos;
    }
}
