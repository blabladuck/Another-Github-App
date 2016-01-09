package com.nosleep.githubclient.business;

import android.util.Log;

import com.android.volley.VolleyError;
import com.nosleep.githubclient.datalayer.services.authorization.Authorizations;
import com.nosleep.githubclient.datalayer.services.repos.RepoSvcInterface;
import com.nosleep.githubclient.datalayer.services.repos.Repository;
import com.nosleep.githubclient.datalayer.storage.AppPreferenceStorage;
import com.nosleep.githubclient.datalayer.storage.InMemoryStorage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by ssub3 on 1/7/16.
 */
public abstract class MyRepos {
    protected List<RepoInfo> memoryCache;

    public static class RepoInfo {
        public final String name;
        public final String defaultBranch;
        public final String permission;
        public final String owner;
        public final boolean isPrivateAccess;
        public final int numberOfOpenIssues;

        public RepoInfo(String name, String defaultBranch, String permission, String owner, boolean isPrivateAccess, int numberOfOpenIssues) {
            this.name = name;
            this.defaultBranch = defaultBranch;
            this.permission = permission;
            this.owner = owner;
            this.isPrivateAccess = isPrivateAccess;
            this.numberOfOpenIssues = numberOfOpenIssues;
        }
    }

    public interface RepoLoadCallback {
        void onLoadRepos(int loadStatus, List<RepoInfo> info);
    }

    public abstract void getRepos(RepoLoadCallback callback);

    public void refresh() {
        memoryCache = null;
    }


    /**
     * Created by ssub3 on 1/7/16.
     */
    static class MyReposImpl extends MyRepos {

        private static final String TAG = "MyRepos";
        private RepoSvcInterface repoSvcInterface;
        private AppPreferenceStorage storage;
        private InMemoryStorage memoryStorage;

        MyReposImpl(RepoSvcInterface repoSvcInterface, InMemoryStorage memoryStorage, AppPreferenceStorage storage) {
            this.repoSvcInterface = repoSvcInterface;
            this.storage = storage;
            this.memoryStorage = memoryStorage;
        }

        @Override
        public void getRepos(final RepoLoadCallback callback) {
            if (memoryCache == null) {
                Authorizations authorizations = memoryStorage.getAuthorizations();
                repoSvcInterface.getMyRepos(authorizations.getToken(), new RepoSvcInterface.ServiceListener<Repository[]>() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, error.toString());
                    }

                    @Override
                    public void onResponse(Repository[] response) {
                        memoryStorage.setRepositoryList(Arrays.asList(response));
                        memoryCache = new ArrayList<RepoInfo>(response.length);
                        for (Repository repo : response) {
                            String permission;
                            if (repo.getPermissions().getAdmin()) {
                                permission = "Admin";
                            } else if (repo.getPermissions().getPush()) {
                                permission = "Push";
                            } else {
                                permission = "Pull";
                            }
                            RepoInfo info = new RepoInfo(repo.getName(), repo.getDefaultBranch(), permission, repo.getOwner().getLogin(), repo.getPrivate(), repo.getOpenIssues());
                            memoryCache.add(info);
                            callback.onLoadRepos(0, memoryCache);
                        }
                    }
                });
            } else {
                callback.onLoadRepos(0, memoryCache);
            }
        }


    }
}
