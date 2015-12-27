package github.blabladuck.com.another_github_app.login.presenter;

import android.content.Context;
import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Sanjeev on 27/12/15.
 */
public interface LoginContract {

    interface View {


        @IntDef({USERNAME_EMPTY, USERNAME_INVALID, VALID})
        @Retention(RetentionPolicy.SOURCE)
        public @interface InvalidUsernameCode {
        }

        public static final int USERNAME_EMPTY = -1;
        public static final int USERNAME_INVALID = -2;
        int VALID = 0;


        @IntDef({PASSWORD_INVALID, VALID})
        @Retention(RetentionPolicy.SOURCE)
        public @interface InvalidPasswordCode {
        }

        public static final int PASSWORD_INVALID = -1;

        void showUsernameError(@InvalidUsernameCode int errorcode);

        void showPasswordError(@InvalidPasswordCode int errorcode);

        void toggleProgressbar(boolean showprogress);
    }

    interface UserAction {
        void attemptLogin(String username, String password);
    }
}
