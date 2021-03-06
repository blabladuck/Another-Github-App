package com.nosleep.githubclient.utils;

/**
 * Created by Sanjeev on 03/01/16.
 */
public class EndPoints {
    private String domain;
    private static EndPoints endPoints = new EndPoints();
    private final String SCHEME = "https://";

    public static EndPoints getEndpoint() {
        return endPoints;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }


    public String constructURL(final String _PATH) {
        return SCHEME + domain + _PATH;
    }


}
