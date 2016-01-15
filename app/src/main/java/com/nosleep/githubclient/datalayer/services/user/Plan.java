
package com.nosleep.githubclient.datalayer.services.user;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Plan {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("space")
    @Expose
    private Integer space;
    @SerializedName("collaborators")
    @Expose
    private Integer collaborators;
    @SerializedName("private_repos")
    @Expose
    private Integer privateRepos;

    /**
     * 
     * @return
     *     The name
     */
    public String getName() {
        return name;
    }

    /**
     * 
     * @param name
     *     The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 
     * @return
     *     The space
     */
    public Integer getSpace() {
        return space;
    }

    /**
     * 
     * @param space
     *     The space
     */
    public void setSpace(Integer space) {
        this.space = space;
    }

    /**
     * 
     * @return
     *     The collaborators
     */
    public Integer getCollaborators() {
        return collaborators;
    }

    /**
     * 
     * @param collaborators
     *     The collaborators
     */
    public void setCollaborators(Integer collaborators) {
        this.collaborators = collaborators;
    }

    /**
     * 
     * @return
     *     The privateRepos
     */
    public Integer getPrivateRepos() {
        return privateRepos;
    }

    /**
     * 
     * @param privateRepos
     *     The private_repos
     */
    public void setPrivateRepos(Integer privateRepos) {
        this.privateRepos = privateRepos;
    }

}
