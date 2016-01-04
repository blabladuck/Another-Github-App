package business.services.login;

import android.util.Base64;
import android.util.Log;

import com.android.volley.Request;

import java.util.HashMap;

import business.services.EndPoints;
import business.services.ServiceListener;
import service.VolleyDelegate;

/**
 * Created by Sanjeev on 03/01/16.
 */
public class LoginSvcImpl implements LoginSvcInterface {

    private VolleyDelegate delegate;

    public LoginSvcImpl(VolleyDelegate delegate) {
        this.delegate = delegate;
    }

    @Override
    public void login(String domain, String username, String password, ServiceListener callback) {
        VolleyDelegate.VolleyRequestBuilder<Authorizations> builder = new VolleyDelegate.VolleyRequestBuilder<>();
        HashMap<String, String> headers = new HashMap<>();
        String authorization = "Basic " + Base64.encodeToString((username + ":" + password).getBytes(), Base64.DEFAULT);
        String accept = "application/vnd.github.v3+json";
        Log.d("GithubServicesAPI", authorization);
        headers.put("Authorization", authorization);
        headers.put("Accept", accept);

        Request<Authorizations> request = builder.method(Request.Method.POST)
                .url(EndPoints.getEndpoint().constructURL(PATH_AUTHORIZATION))
                .headers(headers).requestBody("{\n" +
                        "  \"scopes\": [\n" +
                        "    \"public_repo\",\"repo\"\n" +
                        "  ],\n" +
                        "  \"note\": \"Sanjeev\"\n" +
                        "}")
                .listener(callback)
                .errorListener(callback)
                .gsonClass(Authorizations.class)
                .build();
        delegate.addToRequestQueue(request);
    }

}
