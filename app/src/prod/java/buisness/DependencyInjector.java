package buisness;

import android.content.Context;

import service.GithubServiceImpl;

/**
 * Created by Sanjeev on 27/12/15.
 */
public class DependencyInjector {

    public static IGithubBusinessInterface getGithubBusinessInterface(Context context) {
        return new GithubBusinessImpl(new GithubServiceImpl(context), new LoginStorage(context));
    }
}
