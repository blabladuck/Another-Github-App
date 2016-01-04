package com.nosleep.githubclient.home;

import com.android.volley.Request;

import java.util.HashMap;

import com.nosleep.githubclient.utils.EndPoints;
import com.nosleep.githubclient.utils.ServiceListener;
import com.nosleep.githubclient.datalayer.services.Authorizations;
import com.nosleep.githubclient.utils.VolleyDelegate;

/**
 * Created by Sanjeev on 03/01/16.
 */
public class UserProfileSvcImpl implements UserProfileSvcInterface{

    private VolleyDelegate delegate;

    public UserProfileSvcImpl(VolleyDelegate delegate) {
        this.delegate = delegate;
    }

    @Override
    public void UserProfileSvcInterface(String token, ServiceListener<User> listener) {
        VolleyDelegate.VolleyRequestBuilder<Authorizations> builder = new VolleyDelegate.VolleyRequestBuilder<>();
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer "+token);

        Request<Authorizations> request = builder.method(Request.Method.POST)
                .url(EndPoints.getEndpoint().constructURL(PATH_USER))
                .headers(headers).listener(listener)
                .errorListener(listener)
                .gsonClass(User.class)
                .build();
        delegate.addToRequestQueue(request);
    }
}
