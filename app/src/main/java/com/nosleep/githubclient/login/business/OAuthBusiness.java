package com.nosleep.githubclient.login.business;


/**
 * Created by Sanjeev on 27/12/15.
 */
public interface OAuthBusiness {
    class Access {
        public final String username;
        public final String token;
        public final int id;

        public Access(String username, String token, int id) {
            this.username = username;
            this.token = token;
            this.id = id;
        }
    }

    interface LoginCallback {
        void onLoginSuccess(Access access);

        void onLoginFailure();
    }


    void login(String domain, String username, String password, LoginCallback callback);

    Access getUserAccessCache();

}
