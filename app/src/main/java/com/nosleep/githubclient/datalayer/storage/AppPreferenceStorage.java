package com.nosleep.githubclient.datalayer.storage;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by ssub3 on 12/30/15.
 */
public class AppPreferenceStorage {
    private SharedPreferences prefs;
    private static final String PREF_USERNAME = "username";
    private static final String PREF_DOMAIN = "domain";

    public AppPreferenceStorage(Context context) {
        prefs = context.getSharedPreferences("APP_PREFS", Context.MODE_PRIVATE);
    }

    public String getUsername() {
        return prefs.getString(PREF_USERNAME, "");
    }

    public void saveUsername(String username) {
        prefs.edit().putString(PREF_USERNAME, username).apply();
    }

    public void saveDomain(String domain) {
        prefs.edit().putString(PREF_DOMAIN, domain).apply();
    }

    public String getDomain() {
        return prefs.getString(PREF_DOMAIN, "");
    }
}
