package com.nosleep.githubclient.business;


import android.app.LoaderManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;

import com.android.volley.VolleyError;
import com.nosleep.githubclient.datalayer.services.commits.Commit;
import com.nosleep.githubclient.datalayer.services.commits.CommitsSvcInterface;
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

        private final LoaderManager loaderManager;
        CommitsSvcInterface commitsSvcInterface;
        InMemoryStorage inMemoryStorage;
        ContentResolver contentResolver;
        WeakReference<CommitsLoadCallback> weakCallback;

        BranchCommitsImpl(Context appContext, LoaderManager loaderManager,CommitsSvcInterface commitsSvcInterface,
                          InMemoryStorage inMemoryStorage) {
            this.contentResolver = appContext.getContentResolver();
            this.commitsSvcInterface = commitsSvcInterface;
            this.inMemoryStorage = inMemoryStorage;
            this.loaderManager = loaderManager;
        }

        @Override
        public void getCommits(String repo, String branch, String owner, Calendar since, CommitsLoadCallback callback) {
            weakCallback = new WeakReference<CommitsLoadCallback>(callback);
            loaderManager.initLoader(0,null,this);
            commitsSvcInterface.getCommits(repo, branch, owner, new ServiceListener<Commit[]>() {

                @Override
                public void onResponse(Commit[] response) {
                    BranchCommits.CommitData[] branchCommits = new BranchCommits.CommitData[response.length];
                    for (int i = 0; i < response.length; i++) {
                        //branchCommits[i] = new BranchCommits.CommitData();
                    }
                }

                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
        }

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            return null;
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            if (weakCallback != null && weakCallback.get() != null) {
                weakCallback.get().onLoadCommits(0, null);
            }
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

        }
    }
}

