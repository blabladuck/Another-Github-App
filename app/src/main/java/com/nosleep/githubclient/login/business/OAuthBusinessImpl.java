package com.nosleep.githubclient.login.business;


import android.util.Log;

import com.android.volley.VolleyError;

import com.nosleep.githubclient.datalayer.services.Authorizations;
import com.nosleep.githubclient.datalayer.services.LoginSvcInterface;
import com.nosleep.githubclient.utils.ServiceListener;

import com.nosleep.githubclient.datalayer.storage.AppPreferenceStorage;

/**
 * Created by Sanjeev on 27/12/15.
 */
public class OAuthBusinessImpl implements OAuthBusiness {

    private LoginSvcInterface Api;
    private AppPreferenceStorage storage;

    public OAuthBusinessImpl(LoginSvcInterface loginSvcInterface, AppPreferenceStorage storage) {
        this.Api = loginSvcInterface;
        this.storage = storage;
    }

    @Override
    public void login(final String domain, final String username, final String password, final LoginCallback callback) {
        Api.login(domain, username, password, new ServiceListener<Authorizations>() {
            @Override
            public void onResponse(Authorizations response) {
                storage.saveUsername(username);
                storage.saveID(response.getId());
                storage.saveToken(response.getToken());
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
        String username = storage.getUsername();
        String token = storage.getToken();
        int id = storage.getID();
        if (username != null && !username.isEmpty() && token != null && !token.isEmpty()) {
            return new Access(username, token, id);
        }
        return null;
    }


}
