package com.nosleep.githubclient.business;

import android.util.Log;

import com.android.volley.VolleyError;
import com.nosleep.githubclient.datalayer.services.authorization.Authorizations;
import com.nosleep.githubclient.datalayer.services.user.User;
import com.nosleep.githubclient.datalayer.services.user.UserSvcInterface;
import com.nosleep.githubclient.datalayer.storage.AppPreferenceStorage;
import com.nosleep.githubclient.datalayer.storage.InMemoryStorage;
import com.nosleep.githubclient.utils.EndPoints;
import com.nosleep.githubclient.utils.ServiceListener;

/**
 * Created by ssub3 on 1/5/16.
 */
class UserProfileBusinessImpl implements UserProfileBusiness {
    private static final String TAG = "UserProfileBusiness";
    UserSvcInterface userSvcInterface;
    AppPreferenceStorage preferenceStorage;
    InMemoryStorage memoryStorage;

    public UserProfileBusinessImpl(UserSvcInterface userSvcInterface, InMemoryStorage memoryStorage, AppPreferenceStorage preferenceStorage) {
        this.userSvcInterface = userSvcInterface;
        this.preferenceStorage = preferenceStorage;
        this.memoryStorage = memoryStorage;
        EndPoints.getEndpoint().setDomain(preferenceStorage.getDomain());
    }

    @Override
    public void getUserProfile(final UserProfileCallback callback) {
        Authorizations authorizations = memoryStorage.getAuthorizations();
        userSvcInterface.getUser(authorizations.getToken(), new ServiceListener<User>() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, error.toString());
            }

            @Override
            public void onResponse(User user) {
                UserProfile profile = new UserProfile(user.getName(), user.getAvatarUrl(), user.getCompany(), user.getEmail());
                callback.onSuccess(profile);
            }
        });
    }
}
