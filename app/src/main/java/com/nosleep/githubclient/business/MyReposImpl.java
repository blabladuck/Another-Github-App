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
public class MyReposImpl implements MyRepos {

    private static final String TAG = "MyRepos";

    private List<RepoInfo> memoryCache;
    private RepoSvcInterface repoSvcInterface;
    private AppPreferenceStorage storage;
    private InMemoryStorage memoryStorage;

    MyReposImpl(RepoSvcInterface repoSvcInterface, InMemoryStorage memoryStorage,AppPreferenceStorage storage) {
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
        }
    }

    @Override
    public void refresh() {
        memoryCache = null;
    }


}
