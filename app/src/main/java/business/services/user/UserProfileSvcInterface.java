package business.services.user;

import business.services.ServiceListener;

/**
 * Created by Sanjeev on 03/01/16.
 */
public interface UserProfileSvcInterface {
    String PATH_USER = "/user";
    String HEADER_AUTHORIZATION = "authorization";

    void UserProfileSvcInterface(String token, ServiceListener<User> listener);
}
