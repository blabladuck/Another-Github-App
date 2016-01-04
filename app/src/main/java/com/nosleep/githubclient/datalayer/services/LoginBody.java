
package com.nosleep.githubclient.datalayer.services;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginBody {

    @SerializedName("scopes")
    @Expose
    private List<String> scopes = new ArrayList<String>();
    @SerializedName("note")
    @Expose
    private String note;

    /**
     * @return The scopes
     */
    public List<String> getScopes() {
        return scopes;
    }

    /**
     * @param scopes The scopes
     */
    public void setScopes(List<String> scopes) {
        this.scopes = scopes;
    }

    /**
     * @return The note
     */
    public String getNote() {
        return note;
    }

    /**
     * @param note The note
     */
    public void setNote(String note) {
        this.note = note;
    }

}
