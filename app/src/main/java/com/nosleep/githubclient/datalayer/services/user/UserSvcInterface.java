package com.nosleep.githubclient.datalayer.services.user;

import com.nosleep.githubclient.utils.ServiceListener;

/**
 * Created by Sanjeev on 03/01/16.
 */
public interface UserSvcInterface {
    String PATH_USER = "/user";

    void getUser(String token, ServiceListener<User> listener);
}
