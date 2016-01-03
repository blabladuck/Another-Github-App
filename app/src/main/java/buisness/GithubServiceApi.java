package buisness;

import com.android.volley.Response;

import service.User;

/**
 * Created by Sanjeev on 27/12/15.
 */
public interface GithubServiceApi {
    interface onServiceComplete extends Response.Listener<User>, Response.ErrorListener {
    }

    void login(String domain, String username, String password, onServiceComplete callback);
}
