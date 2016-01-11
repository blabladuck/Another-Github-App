package com.nosleep.githubclient.business;


import android.content.Context;

import com.android.volley.VolleyError;
import com.nosleep.githubclient.datalayer.services.commits.Commit;
import com.nosleep.githubclient.datalayer.services.commits.CommitsSvcInterface;
import com.nosleep.githubclient.datalayer.storage.InMemoryStorage;
import com.nosleep.githubclient.utils.ServiceListener;

import java.util.Calendar;
import java.util.List;

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
        void onLoadCommits(int loadStatus, List<CommitData> info);
    }

    public abstract void getCommits(String repo, String branch, String owner, Calendar since, CommitsLoadCallback callback);

    static class BranchCommitsImpl extends BranchCommits {

        CommitsSvcInterface commitsSvcInterface;
        InMemoryStorage inMemoryStorage;
        Context appContext;

        BranchCommitsImpl(Context appContext, CommitsSvcInterface commitsSvcInterface,
                          InMemoryStorage inMemoryStorage) {
            this.appContext = appContext;
            this.commitsSvcInterface = commitsSvcInterface;
            this.inMemoryStorage = inMemoryStorage;
        }

        @Override
        public void getCommits(String repo, String branch, String owner, Calendar since, CommitsLoadCallback callback) {
            commitsSvcInterface.getCommits(repo, branch, owner, new ServiceListener<Commit[]>() {

                @Override
                public void onResponse(Commit[] response) {
                }

                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
        }
    }
}

