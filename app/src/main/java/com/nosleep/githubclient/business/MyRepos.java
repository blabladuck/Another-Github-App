package com.nosleep.githubclient.business;

import java.util.List;

/**
 * Created by ssub3 on 1/7/16.
 */
public interface MyRepos {
    class RepoInfo {
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

    interface RepoLoadCallback {
        void onLoadRepos(int loadStatus, List<RepoInfo> info);
    }

    void getRepos(RepoLoadCallback callback);

    void refresh();
}
