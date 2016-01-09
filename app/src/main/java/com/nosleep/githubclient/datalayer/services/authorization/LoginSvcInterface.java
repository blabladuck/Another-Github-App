package com.nosleep.githubclient.datalayer.services.authorization;

import com.nosleep.githubclient.utils.ServiceListener;

/**
 * Created by Sanjeev on 03/01/16.
 */
public interface LoginSvcInterface {

    String PATH_AUTHORIZATION = "/authorizations";

    void login(String username, String password, ServiceListener<Authorizations> callback);

}
