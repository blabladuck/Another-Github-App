package com.nosleep.githubclient.business;

/**
 * Created by ssub3 on 1/5/16.
 */
public interface UserProfileBusiness {

    class UserProfile {
        public final String name;
        public final String avatarURL;
        public final String company;
        public final String email;

        public UserProfile(String name, String avatarURL, String company, String email) {
            this.name = name;
            this.avatarURL = avatarURL;
            this.company = company;
            this.email = email;
        }
    }

    interface UserProfileCallback {
        void onSuccess(UserProfile user);

        void onFailure();
    }

    void getUserProfile(UserProfileCallback callback);
}
