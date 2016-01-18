package com.nosleep.githubclient.business;

import android.util.Log;

import com.android.volley.VolleyError;
import com.nosleep.githubclient.datalayer.services.authorization.Authorizations;
import com.nosleep.githubclient.datalayer.services.branches.Branch;
import com.nosleep.githubclient.datalayer.services.branches.BranchesSvcInterface;
import com.nosleep.githubclient.datalayer.storage.AppPreferenceStorage;
import com.nosleep.githubclient.datalayer.storage.InMemoryStorage;
import com.nosleep.githubclient.utils.ServiceListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class MyBranch {
    protected Map<String, List<BranchInfo>> memoryCache;

    public class BranchInfo {
        public final String name;
        public final String sha;
        public final String url;

        public BranchInfo(String name, String sha, String url) {
            this.name = name;
            this.sha = sha;
            this.url = url;
        }
    }

    public interface BranchesFetchCallback {
        void onLoadBranchesForARepo(int loadStatus, List<BranchInfo> info);
    }

    public abstract void getBranchesForRepo(String repoName, String repoOwnerName, BranchesFetchCallback callback);

    public void refresh() {
        memoryCache = null;
    }

    public static class MyBranchImpl extends MyBranch {

        private static final String TAG = MyBranchImpl.class.getSimpleName();


        private BranchesSvcInterface branchesSvcInterface;
        private AppPreferenceStorage storage;
        private InMemoryStorage memoryStorage;

        MyBranchImpl(BranchesSvcInterface branchesSvcInterface, InMemoryStorage memoryStorage, AppPreferenceStorage storage) {
            this.branchesSvcInterface = branchesSvcInterface;
            this.storage = storage;
            this.memoryStorage = memoryStorage;
        }

        @Override
        public void getBranchesForRepo(final String repoName, String repoOwnerName, final BranchesFetchCallback callback) {
            if (memoryCache == null || !memoryCache.containsKey(repoName)) {
                Authorizations authorizations = memoryStorage.getAuthorizations();
                branchesSvcInterface.getBranches(authorizations.getToken(), repoName, repoOwnerName, new ServiceListener<Branch[]>() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.d(TAG, volleyError.toString());
                    }

                    @Override
                    public void onResponse(Branch[] response) {
                        Log.d(TAG, "onResponse() success");
                        List<Branch> branches = Arrays.asList(response);
                        memoryStorage.setBranchList(repoName, branches);
                        if (memoryCache == null) {
                            memoryCache = new HashMap<>();
                        }
                        List<BranchInfo> branchInfoList = new ArrayList<>();
                        for (Branch branch : branches) {
                            MyBranch.BranchInfo info = new MyBranch.BranchInfo(branch.getName(), branch.getCommit().getSha(), branch.getCommit().getUrl());
                            branchInfoList.add(info);
                        }
                        memoryCache.put(repoName, branchInfoList);
                        callback.onLoadBranchesForARepo(0, branchInfoList);
                    }
                });
            } else {
                callback.onLoadBranchesForARepo(0, memoryCache.get(repoName));
            }
        }

    }
}