package com.nosleep.githubclient.datalayer.services;

import com.android.volley.Request;

import java.util.HashMap;

import com.nosleep.githubclient.datalayer.services.user.User;
import com.nosleep.githubclient.datalayer.services.user.UserSvcInterface;
import com.nosleep.githubclient.utils.EndPoints;
import com.nosleep.githubclient.utils.ServiceListener;
import com.nosleep.githubclient.datalayer.services.authorization.Authorizations;
import com.nosleep.githubclient.utils.VolleyDelegate;

/**
 * Created by Sanjeev on 03/01/16.
 */
class UserSvcImpl implements UserSvcInterface {

    private VolleyDelegate delegate;

    UserSvcImpl(VolleyDelegate delegate) {
        this.delegate = delegate;
    }

    @Override
    public void getUser(String token, ServiceListener<User> listener) {
        VolleyDelegate.VolleyRequestBuilder<Authorizations> builder = new VolleyDelegate.VolleyRequestBuilder<>();
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + token);

        Request<Authorizations> request = builder.method(Request.Method.GET)
                .url(EndPoints.getEndpoint().constructURL(PATH_USER))
                .headers(headers).listener(listener)
                .errorListener(listener)
                .gsonClass(User.class)
                .build();
        delegate.addToRequestQueue(request);
    }
}
