package com.nosleep.githubclient.datalayer.storage;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Sanjeev on 13/01/16.
 */
public class CommitDAO {
    private static final String TAG = "CommitDAO";
    private ContentResolver resolver;

    public CommitDAO(ContentResolver resolver) {
        this.resolver = resolver;
    }

    public void insert(final Uri uri, final ContentValues[] values) {
        final Thread thread = new Thread() {
            @Override
            public void run() {
                if (values.length > 1) {
                    Log.d(TAG, "bulkInsert = "+resolver.bulkInsert(uri, values));
                } else {
                    Log.d(TAG, "insert = "+resolver.insert(uri, values[0]));
                }
            }
        };
        thread.start();

    }

    public void update(final Uri uri, @Nullable final ContentValues values,
                       @Nullable final String where, @Nullable final String[] selectionArgs) {
        final Thread thread = new Thread() {
            @Override
            public void run() {
                resolver.update(uri, values, where, selectionArgs);
            }
        };
        thread.start();
    }


}
