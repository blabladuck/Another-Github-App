package buisness;

import android.util.Base64;

import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;

import java.io.IOException;

import buisness.models.User;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;
import retrofit.http.GET;

public class LoginServiceImpl implements GithubServiceApi {

    private final Retrofit.Builder baseRetrofitBuilder =
            new Retrofit.Builder()
                    .baseUrl(GitHubConstants.GIT_HUB_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create());

    @Override
    public void login(String username, String password, final onServiceComplete<User> callback) {
        String credentials = username + ":" + password;
        String basicAuth = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);

        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.interceptors().add(getLoginServiceInterceptor(basicAuth));

        Retrofit retrofit = baseRetrofitBuilder.client(okHttpClient).build();
        LoginServiceRetrofit service = retrofit.create(LoginServiceRetrofit.class);

        Call<User> userCall = service.getUser();
        userCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Response<User> response, Retrofit retrofit) {
                callback.onSuccess(response.body());
            }

            @Override
            public void onFailure(Throwable t) {
                callback.onFailure();
            }
        });
    }


    private Interceptor getLoginServiceInterceptor(final String authorizationValue) {
        return new Interceptor() {
            @Override
            public com.squareup.okhttp.Response intercept(Chain chain) throws IOException {
                Request original = chain.request();

                Request.Builder requestBuilder = original.newBuilder()
                        .header("Authorization", authorizationValue)
                        .header("Accept", "application/json")
                        .method(original.method(), original.body());

                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        };
    }

    private interface LoginServiceRetrofit {
        @GET("/user")
        Call<User> getUser();
    }
}
