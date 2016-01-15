package com.nosleep.githubclient.datalayer.storage;

import android.content.Context;

import com.nosleep.githubclient.datalayer.storage.AppPreferenceStorage;

/**
 * Created by Sanjeev on 03/01/16.
 */
public class StorageInjector {

    private static AppPreferenceStorage prefStorage;
    private static InMemoryStorage memoryStorage;

    public StorageInjector(Context context) {
        if (prefStorage == null) {
            prefStorage = new AppPreferenceStorage(context);
        }

        if (memoryStorage == null) {
            memoryStorage = new InMemoryStorage();
        }

    }

    public static void setMockPreferenceStorage(AppPreferenceStorage mock) {
        prefStorage = mock;
    }

    public AppPreferenceStorage getPreferenceStorage() {
        return prefStorage;
    }

    public InMemoryStorage getMemoryStorage() {
        return memoryStorage;
    }

    public static void setMockMemoryStorage(InMemoryStorage memoryStorage) {
        StorageInjector.memoryStorage = memoryStorage;
    }
}
