package business.storage;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by ssub3 on 12/30/15.
 */
public class AppPreferenceStorage {
    private Context context;
    private SharedPreferences prefs;
    private static final String PREF_USERNAME = "username";
    private static final String PREF_ID = "id";
    private static final String PREF_TOKEN = "token";
    private static final String PREF_AVATAR = "avatar";

    AppPreferenceStorage(Context context) {
        this.context = context;
        prefs = context.getSharedPreferences("APP_PREFS", Context.MODE_PRIVATE);
    }

    public String getUsername() {
        return prefs.getString(PREF_USERNAME, "");
    }

    public void saveUsername(String username) {
        prefs.edit().putString(PREF_USERNAME, username).apply();
    }

    public String getAvatar() {
        return prefs.getString(PREF_AVATAR, "");
    }

    public void saveAvatar(String avatar) {
        prefs.edit().putString(PREF_AVATAR, avatar).apply();
    }

    public int getID() {
        return prefs.getInt(PREF_ID, 0);
    }

    public void saveID(int id) {
        prefs.edit().putInt(PREF_ID, id).apply();
    }


    public String getToken() {
        return prefs.getString(PREF_TOKEN, "");
    }

    public void saveToken(String token) {
        prefs.edit().putString(PREF_TOKEN, token).apply();
    }

}
