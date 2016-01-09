package com.nosleep.githubclient.business;


import android.util.Base64;
import android.util.Log;

import com.android.volley.VolleyError;

import com.nosleep.githubclient.datalayer.services.authorization.Authorizations;
import com.nosleep.githubclient.datalayer.services.authorization.LoginSvcInterface;
import com.nosleep.githubclient.datalayer.storage.InMemoryStorage;
import com.nosleep.githubclient.utils.ServiceListener;

import com.nosleep.githubclient.datalayer.storage.AppPreferenceStorage;

/**
 * Created by Sanjeev on 27/12/15.
 */
class OAuthBusinessImpl implements OAuthBusiness {

    private LoginSvcInterface Api;
    private AppPreferenceStorage preferenceStorage;
    private InMemoryStorage memoryStorage;

    OAuthBusinessImpl(LoginSvcInterface loginSvcInterface, InMemoryStorage memoryStorage, AppPreferenceStorage preferenceStorage) {
        this.Api = loginSvcInterface;
        this.preferenceStorage = preferenceStorage;
        this.memoryStorage = memoryStorage;
    }

    @Override
    public void login(final String domain, final String username, final String password, final LoginCallback callback) {
        Api.login(username, password, new ServiceListener<Authorizations>() {
            @Override
            public void onResponse(Authorizations response) {
                memoryStorage.setAuthorizations(response);
                memoryStorage.setBasicAuthHeaderValue("Basic " + Base64.encodeToString((username + ":" + password).getBytes(), Base64.DEFAULT));
                preferenceStorage.saveUsername(username);
                preferenceStorage.saveDomain(domain);
                Access access = new Access(username, response.getToken(), response.getId());
                callback.onLoginSuccess(access);
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.networkResponse != null) {
                    Log.d("ERROR", "error = " + new String(error.networkResponse.data));
                }
                callback.onLoginFailure();
            }
        });

    }

    @Override
    public Access getUserAccessCache() {
        Authorizations authorizations = memoryStorage.getAuthorizations();
        if (authorizations == null) {
            return null;
        }
        String username = preferenceStorage.getUsername();
        String token = authorizations.getToken();
        int id = authorizations.getId();
        if (username != null && !username.isEmpty() && token != null && !token.isEmpty()) {
            return new Access(username, token, id);
        }
        return null;
    }


}
