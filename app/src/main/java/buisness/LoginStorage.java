package buisness;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by ssub3 on 12/30/15.
 */
public class LoginStorage {
    private Context context;
    private SharedPreferences prefs;
    private static final String PREF_USERNAME = "username";
    private static final String PREF_AVATAR = "avatar";

    LoginStorage(Context context) {
        this.context = context;
        prefs = context.getSharedPreferences("APP_PREFS", Context.MODE_PRIVATE);
    }

    public String getUsername() {
        return prefs.getString(PREF_USERNAME, "");
    }

    public void setUsername(String username) {
        prefs.edit().putString(PREF_USERNAME, username).apply();
    }

    public String getAvatar() {
        return prefs.getString(PREF_AVATAR, "");
    }

    public void setAvatar(String avatar) {
        prefs.edit().putString(PREF_AVATAR, avatar).apply();
    }

}
