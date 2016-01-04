package business.services.login;

import business.services.ServiceListener;

/**
 * Created by Sanjeev on 03/01/16.
 */
public interface LoginSvcInterface {

    String PATH_AUTHORIZATION = "/authorizations";

    void login(String domain, String username, String password, ServiceListener<Authorizations> callback);

}
