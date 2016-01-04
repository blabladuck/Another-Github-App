package business.storage;

import android.content.Context;

import com.nosleep.githubclient.datalayer.storage.AppPreferenceStorage;

/**
 * Created by Sanjeev on 03/01/16.
 */
public class StorageInjector {

    private static AppPreferenceStorage prefStorage;

    public StorageInjector(Context context) {
        prefStorage = new AppPreferenceStorage(context);
    }

    public static void setMockPreferenceStorage(AppPreferenceStorage mock) {
        prefStorage = mock;
    }

    public AppPreferenceStorage getPreferenceStorage() {
        return prefStorage;
    }
}
