package com.nosleep.githubclient.business;


import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
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

        public CommitData(int id, String sha, String author, String authorEmail, String committer, String committerEmail, String authorAvatar, String committerAvatar, String message, Calendar commitdate) {
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

    static class BranchCommitsImpl extends BranchCommits implements LoaderManager.LoaderCallbacks<Cursor> {
        private static final String TAG = "BranchCommits";

        private final LoaderManager loaderManager;
        CommitsSvcInterface commitsSvcInterface;
        InMemoryStorage inMemoryStorage;
        ContentResolver contentResolver;
        WeakReference<CommitsLoadCallback> weakCallback;

        BranchCommitsImpl(Context appContext, LoaderManager loaderManager, CommitsSvcInterface commitsSvcInterface,
                          InMemoryStorage inMemoryStorage) {
            this.contentResolver = appContext.getContentResolver();
            this.commitsSvcInterface = commitsSvcInterface;
            this.inMemoryStorage = inMemoryStorage;
            this.loaderManager = loaderManager;
        }

        @Override
        public void getCommits(String repo, String branch, String owner, Calendar since, CommitsLoadCallback callback) {
            weakCallback = new WeakReference<>(callback);
            loaderManager.initLoader(0, null, this);
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

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            return null;
        }

        @Override
        public void onLoadFinished(android.support.v4.content.Loader<Cursor> loader, Cursor data) {

        }

        @Override
        public void onLoaderReset(android.support.v4.content.Loader<Cursor> loader) {

        }

    }
}

