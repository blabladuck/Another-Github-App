package business;

/**
 * Created by Sanjeev on 03/01/16.
 */
public interface UserProfile {
    class User {
        public final String name;
        public final String avatarURL;
        public final String company;
        public final String email;

        public User(String name, String avatarURL, String company, String email) {
            this.name = name;
            this.avatarURL = avatarURL;
            this.company = company;
            this.email = email;
        }
    }

    interface UserProfileCallback {
        void onSuccess(User user);

        void onFailure();
    }

    void getUserProfile(String token, UserProfileCallback callback);
}
