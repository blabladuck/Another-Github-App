package com.nosleep.githubclient.business;

/**
 * Created by ssub3 on 1/8/16.
 */
public class FakeOAuthBusiness implements OAuthBusiness {
    Access access;

    FakeOAuthBusiness(Access access) {
        this.access = access;
    }

    @Override
    public void login(String domain, String username, String password, LoginCallback callback) {
        if (access != null) {
            callback.onLoginSuccess(access);
        } else {
            callback.onLoginFailure();
        }
    }

    @Override
    public Access getUserAccessCache() {
        return access;
    }
}
