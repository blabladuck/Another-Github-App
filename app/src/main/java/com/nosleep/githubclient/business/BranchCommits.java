package com.nosleep.githubclient.business;


import android.annotation.SuppressLint;
import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;

import com.android.volley.VolleyError;
import com.nosleep.githubclient.datalayer.services.commits.Author_;
import com.nosleep.githubclient.datalayer.services.commits.Commit;
import com.nosleep.githubclient.datalayer.services.commits.Commit_;
import com.nosleep.githubclient.datalayer.services.commits.CommitsSvcInterface;
import com.nosleep.githubclient.datalayer.services.commits.Committer_;
import com.nosleep.githubclient.datalayer.storage.CommitDAO;
import com.nosleep.githubclient.datalayer.storage.DataProvider;
import com.nosleep.githubclient.datalayer.storage.DataProviderContract;
import com.nosleep.githubclient.datalayer.storage.InMemoryStorage;
import com.nosleep.githubclient.utils.ServiceListener;

import java.lang.ref.WeakReference;
import java.util.Calendar;

/**
 * Created by Sanjeev on 10/01/16.
 */
public abstract class BranchCommits {

    public class CommitData {
        public final int id;
        public final String sha;
        public final String author;
        public final String authorEmail;
        public final String committer;
        public final String committerEmail;
        public final String authorAvatar;
        public final String committerAvatar;
        public final String message;
        public final Calendar commitdate;

        public CommitData(int id, String sha, String author, String authorEmail, String authorAvatar, String committer, String committerEmail, String committerAvatar, String message, Calendar commitdate) {
            this.id = id;
            this.sha = sha;
            this.author = author;
            this.authorEmail = authorEmail;
            this.committer = committer;
            this.committerEmail = committerEmail;
            this.authorAvatar = authorAvatar;
            this.committerAvatar = committerAvatar;
            this.message = message;
            this.commitdate = commitdate;
        }
    }

    public interface CommitsLoadCallback {
        void onLoadCommits(int loadStatus, CommitData[] info);
    }

    public abstract void getCommits(String repo, String branch, String owner, Calendar since, CommitsLoadCallback callback);

    static class BranchCommitsImpl extends BranchCommits {
        private static final String TAG = "BranchCommits";

        CommitsSvcInterface commitsSvcInterface;
        InMemoryStorage inMemoryStorage;
        ContentResolver contentResolver;
        WeakReference<CommitsLoadCallback> weakCallback;
        private CommitData[] commitData;
        private ContentObserver dbObserver;
        private AsyncQueryHandler queryHandler;

        BranchCommitsImpl(Context appContext, LoaderManager loaderManager, CommitsSvcInterface commitsSvcInterface,
                          InMemoryStorage inMemoryStorage) {
            this.contentResolver = appContext.getContentResolver();
            this.commitsSvcInterface = commitsSvcInterface;
            this.inMemoryStorage = inMemoryStorage;
            this.dbObserver = new ContentObserver(new Handler(appContext.getMainLooper())) {
                @Override
                public boolean deliverSelfNotifications() {
                    return super.deliverSelfNotifications();
                }

                @Override
                public void onChange(boolean selfChange) {
                    Log.d(TAG, "onChange");
                    commitData = null;
                    queryHandler.startQuery(2, null, DataProviderContract.Commits.CONTENT_URI, null, null, null, null);
                }

                @Override
                public void onChange(boolean selfChange, Uri uri) {
                    Log.d(TAG, "onChange = " + uri);
                    commitData = null;
                    queryHandler.startQuery(2, null, DataProviderContract.Commits.CONTENT_URI, null, null, null, null);
                }
            };
            contentResolver.registerContentObserver(DataProviderContract.Commits.CONTENT_URI_ALL, false, dbObserver);

        }

        @SuppressLint("Handlerleak")
        @Override
        public void getCommits(final String repo, final String branch, final String owner, final Calendar since, CommitsLoadCallback callback) {
            if (commitData != null) {
                callback.onLoadCommits(0, commitData);
                return;
            }
            weakCallback = new WeakReference<>(callback);
            queryHandler = new AsyncQueryHandler(contentResolver) {
                @Override
                protected void onQueryComplete(int token, Object cookie, Cursor cursor) {

                    if (cursor != null && cursor.getCount() > 0) {
                        int column_id = cursor.getColumnIndex(DataProviderContract.Commits._ID);
                        int column_sha = cursor.getColumnIndex(DataProviderContract.Commits.SHA);
                        int column_author_name = cursor.getColumnIndex(DataProviderContract.Commits.AUTHOR_NAME);
                        int column_committer_name = cursor.getColumnIndex(DataProviderContract.Commits.COMMITTER_NAME);
                        int column_author_email = cursor.getColumnIndex(DataProviderContract.Commits.AUTHOR_EMAIL);
                        int column_committer_email = cursor.getColumnIndex(DataProviderContract.Commits.COMMITTER_EMAIL);
                        int column_author_avatar = cursor.getColumnIndex(DataProviderContract.Commits.AUTHOR_AVATAR);
                        int column_committer_avatar = cursor.getColumnIndex(DataProviderContract.Commits.COMMITTER_AVATAR);
                        int column_committer_message = cursor.getColumnIndex(DataProviderContract.Commits.COMMIT_MESSAGE);
                        int column_commit_date = cursor.getColumnIndex(DataProviderContract.Commits.COMMIT_DATE);
                        cursor.moveToFirst();
                        BranchCommitsImpl.this.commitData = new CommitData[cursor.getCount()];
                        for (CommitData data : commitData) {
                            data = new CommitData(cursor.getInt(column_id)
                                    , cursor.getString(column_sha)
                                    , cursor.getString(column_author_name)
                                    , cursor.getString(column_author_email)
                                    , cursor.getString(column_author_avatar)
                                    , cursor.getString(column_committer_name)
                                    , cursor.getString(column_committer_email)
                                    , cursor.getString(column_committer_avatar)
                                    , cursor.getString(column_committer_message)
                                    , null);
                            //,cursor.getString(column_commit_date))))
                        }

                        if (weakCallback != null && weakCallback.get() != null) {
                            weakCallback.get().onLoadCommits(0, commitData);
                        }
                    } else {
                        refreshFromNetwork(repo, branch, owner, since);
                    }

                }
            };

            queryHandler.startQuery(1, null, DataProviderContract.Commits.CONTENT_URI, null, null, null, null);


        }

        private void refreshFromNetwork(String repo, String branch, String owner, Calendar since) {
            String token = inMemoryStorage.getBasicAuthHeaderValue();
            Log.d(TAG, "getCommits() repo = " + repo + " branch = " + branch + "owner = " + owner);
            commitsSvcInterface.getCommits(token, repo, branch, owner, new ServiceListener<Commit[]>() {

                @Override
                public void onResponse(Commit[] response) {
                    Log.d(TAG, "response length = " + response.length);
                    ContentValues[] values = new ContentValues[response.length];
                    for (int i = 0; i < response.length; i++) {
                        values[i] = new ContentValues(9);
                        values[i].put(DataProviderContract.Commits.SHA, response[i].getSha());
                        Log.d(TAG, "sha" + response[i].getSha());
                        Commit_ commit_ = response[i].getCommit();
                        if (commit_ != null) {
                            values[i].put(DataProviderContract.Commits.COMMITTER_NAME, commit_.getCommitter().getName());
                            values[i].put(DataProviderContract.Commits.COMMITTER_EMAIL, commit_.getCommitter().getEmail());
                            values[i].put(DataProviderContract.Commits.COMMIT_MESSAGE, commit_.getMessage());
                            values[i].put(DataProviderContract.Commits.AUTHOR_NAME, commit_.getAuthor().getName());
                            values[i].put(DataProviderContract.Commits.AUTHOR_EMAIL, commit_.getAuthor().getEmail());
                            values[i].put(DataProviderContract.Commits.COMMIT_DATE, commit_.getCommitter().getDate());
                        }
                        Author_ author_ = response[i].getAuthor();
                        if (author_ != null) {
                            values[i].put(DataProviderContract.Commits.AUTHOR_AVATAR, author_.getAvatarUrl());
                        }
                        Committer_ committer_ = response[i].getCommitter();
                        if (committer_ != null) {
                            values[i].put(DataProviderContract.Commits.COMMITTER_EMAIL, committer_.getAvatarUrl());

                        }
                    }
                    CommitDAO dao = new CommitDAO(contentResolver);

                    dao.insert(DataProviderContract.Commits.CONTENT_URI, values);
                }

                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d(TAG, "error = " + error);
                }
            });
        }

    }


}

