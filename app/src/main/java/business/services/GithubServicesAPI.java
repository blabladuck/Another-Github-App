package business.services;

import android.content.Context;

import business.services.login.Authorizations;
import business.services.login.LoginSvcImpl;
import business.services.login.LoginSvcInterface;
import service.VolleyDelegate;

/**
 * Created by ssub3 on 12/28/15.
 */
public class GithubServicesAPI {
    private LoginSvcInterface loginSvcInterface;

    public GithubServicesAPI(Context context) {
        VolleyDelegate delegate = VolleyDelegate.getInstance(context);
        loginSvcInterface = new LoginSvcImpl(delegate);
    }

    public void login(String domain, String username, String password, ServiceListener<Authorizations> callback) {
        loginSvcInterface.login(domain, username, password, callback);
    }
}
