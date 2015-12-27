package buisness;

import android.os.Handler;

/**
 * Created by Sanjeev on 27/12/15.
 */
public class FakeServiceImple implements GithubServiceApi{
    @Override
    public void login(String username, String password, final onServiceComplete callback) {
        Handler mockHandler = new Handler();
        mockHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                callback.onSuccess();
            }
        },5000);

    }
}
