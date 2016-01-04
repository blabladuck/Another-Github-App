package github.blabladuck.com.another_github_app.login.presenter;

import android.os.Bundle;
import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import business.OAuthBusiness;
import business.services.user.User;

/**
 * Created by Sanjeev on 27/12/15.
 */
public interface LoginContract {


    interface LoginView {
        void navigateToHomeScreen(OAuthBusiness.Access access);

        @IntDef({USERNAME_EMPTY, USERNAME_INVALID, VALID})
        @Retention(RetentionPolicy.SOURCE)
        public @interface InvalidUsernameCode {
        }

        public static final int USERNAME_EMPTY = -1;
        public static final int USERNAME_INVALID = -2;
        public static final int DOMAIN_EMPTY = -3;
        int VALID = 0;


        @IntDef({PASSWORD_INVALID, VALID})
        @Retention(RetentionPolicy.SOURCE)
        public @interface InvalidPasswordCode {
        }

        @IntDef({DOMAIN_EMPTY, VALID})
        @Retention(RetentionPolicy.SOURCE)
        public @interface InvalidDomainCode {
        }


        public static final int PASSWORD_INVALID = -1;

        void showLoginScreen();

        void showWelcomeScreen(String username, String token);

        void showUsernameError(@InvalidUsernameCode int errorcode);

        void showPasswordError(@InvalidPasswordCode int errorcode);

        void showDomainError(@InvalidDomainCode int errorcode);

        void toggleProgressbar(boolean showprogress);

    }

    interface UserAction {

        void checkUserSessionAvailability();

        void attemptLogin(String domain, String username, String password);
    }
}
