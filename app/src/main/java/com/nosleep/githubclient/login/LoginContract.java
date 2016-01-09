package com.nosleep.githubclient.login;

import android.support.annotation.IntDef;

import com.nosleep.githubclient.business.OAuth;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Sanjeev on 27/12/15.
 */
public interface LoginContract {


    interface LoginView {

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

        void showUsernameError(@InvalidUsernameCode int errorcode);

        void showPasswordError(@InvalidPasswordCode int errorcode);

        void showDomainError(@InvalidDomainCode int errorcode);

        void toggleProgressbar(boolean showprogress);

        void navigateToHomeScreen(OAuth.Access access);

    }

    interface UserAction {

        boolean checkUserSessionAvailability();

        void attemptLogin(String domain, String username, String password);


    }
}
