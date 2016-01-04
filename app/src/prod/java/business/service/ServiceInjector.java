package business.service;

import android.content.Context;

import com.nosleep.githubclient.datalayer.services.LoginSvcImpl;
import com.nosleep.githubclient.datalayer.services.LoginSvcInterface;
import com.nosleep.githubclient.utils.VolleyDelegate;

/**
 * Created by Sanjeev on 03/01/16.
 */
public class ServiceInjector {
    private VolleyDelegate volleyDelegate;
    private static LoginSvcInterface loginSvcInterface;

    public ServiceInjector(Context appContext) {
        volleyDelegate = VolleyDelegate.getInstance(appContext.getApplicationContext());
    }

    public static void setMockLoginSvcInterface(LoginSvcInterface mock) {
        loginSvcInterface = mock;
    }

    public LoginSvcInterface getLoginSvcInterface() {
        if (loginSvcInterface != null) {
            return loginSvcInterface;
        }
        return new LoginSvcImpl(volleyDelegate);
    }
}
