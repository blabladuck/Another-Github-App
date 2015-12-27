package buisness;

/**
 * Created by Sanjeev on 27/12/15.
 */
public class DependencyInjector {

    public static IGithubBusinessInterface getGithubBusinessInterface() {
        return new GithubBusinessImpl(new FakeServiceImple());
    }
}
