package com.nosleep.githubclient.datalayer.storage;

import com.nosleep.githubclient.datalayer.services.authorization.Authorizations;
import com.nosleep.githubclient.datalayer.services.branches.Branch;
import com.nosleep.githubclient.datalayer.services.repos.Repository;
import com.nosleep.githubclient.datalayer.services.user.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ssub3 on 1/8/16.
 */
public class InMemoryStorage {

    public InMemoryStorage() {
        authorizations = null;
        repositoryList = new ArrayList<>(0);
        branchList = new ArrayList<>(0);
        user = null;
    }

    private Authorizations authorizations;
    private List<Repository> repositoryList;
    private List<Branch> branchList;
    private User user;
    private String basicAuthHeaderValue;

    public String getBasicAuthHeaderValue() {
        return basicAuthHeaderValue;
    }

    public void setBasicAuthHeaderValue(String basicAuthHeaderValue) {
        this.basicAuthHeaderValue = basicAuthHeaderValue;
    }

    public Authorizations getAuthorizations() {
        return authorizations;
    }

    public void setAuthorizations(Authorizations authorizations) {
        this.authorizations = authorizations;
    }

    public List<Repository> getRepositoryList() {
        return repositoryList;
    }

    public void setRepositoryList(List<Repository> repositoryList) {
        this.repositoryList = repositoryList;
    }

    public List<Branch> getBranchList() {
        return branchList;
    }

    public void setBranchList(String repoName, List<Branch> branchList) {
        //TODO complete this
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
