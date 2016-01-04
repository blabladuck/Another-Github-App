package service;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.util.LruCache;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by ssub3 on 12/29/15.
 */
public class VolleyDelegate {
    private static final String TAG = "VolleyDelegate";
    private static VolleyDelegate mInstance;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    private static Context mCtx;

    private VolleyDelegate(Context context) {
        mCtx = context;
        mRequestQueue = getRequestQueue();

        mImageLoader = new ImageLoader(mRequestQueue,
                new ImageLoader.ImageCache() {
                    private final LruCache<String, Bitmap>
                            cache = new LruCache<String, Bitmap>(20);

                    @Override
                    public Bitmap getBitmap(String url) {
                        return cache.get(url);
                    }

                    @Override
                    public void putBitmap(String url, Bitmap bitmap) {
                        cache.put(url, bitmap);
                    }
                });
    }

    public static synchronized VolleyDelegate getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new VolleyDelegate(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    public ImageLoader getImageLoader() {
        return mImageLoader;
    }

    public static class VolleyRequestBuilder<T> {
        private int method;
        private String url;
        private String requestBody;
        private Response.Listener<T> listener;
        private Response.ErrorListener errorListener;
        private HashMap<String, String> headers;
        private HashMap<String, String> params;
        private Class<T> clazz;


        public VolleyRequestBuilder method(int method) {
            this.method = method;
            return this;
        }

        public VolleyRequestBuilder url(String url) {
            this.url = url;
            return this;
        }

        public VolleyRequestBuilder requestBody(String requestBody) {
            this.requestBody = requestBody;
            return this;
        }

        public VolleyRequestBuilder listener(Response.Listener<T> listener) {
            this.listener = listener;
            return this;
        }

        public VolleyRequestBuilder errorListener(Response.ErrorListener errorListener) {
            this.errorListener = errorListener;
            return this;
        }

        public VolleyRequestBuilder gsonClass(Class<T> clazz) {
            this.clazz = clazz;
            return this;
        }


        public VolleyRequestBuilder headers(HashMap<String, String> headers) {
            this.headers = headers;
            return this;
        }

        public VolleyRequestBuilder params(HashMap<String, String> params) {
            this.params = params;
            return this;
        }


        public Request<T> build() {
            return new Request<T>(method, url, errorListener) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    return headers;
                }

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    return params;
                }

                @Override
                protected Response<T> parseNetworkResponse(NetworkResponse response) {
                    try {
                        String json = new String(
                                response.data,
                                HttpHeaderParser.parseCharset(response.headers));
                        Gson gson = new Gson();
                        Log.d(VolleyDelegate.TAG,"<--------"+response.statusCode);
                        Set<String> keyset = response.headers.keySet();
                        Log.d(VolleyDelegate.TAG, "**********************");
                        for (String str : keyset) {
                            Log.d(VolleyDelegate.TAG, str + " = " + response.headers.get(str));
                        }
                        Log.d(VolleyDelegate.TAG, json);
                        Log.d(VolleyDelegate.TAG, "**********************");
                        return Response.success(
                                gson.fromJson(json, clazz),
                                HttpHeaderParser.parseCacheHeaders(response));
                    } catch (UnsupportedEncodingException e) {
                        return Response.error(new ParseError(e));
                    } catch (JsonSyntaxException e) {
                        return Response.error(new ParseError(e));
                    } catch (Exception e) {
                        return Response.error(new VolleyError(response));
                    }
                }

                @Override
                protected void deliverResponse(T response) {
                    listener.onResponse(response);
                }

                @Override
                public void deliverError(VolleyError error) {
                    if (error.networkResponse != null) {
                        Log.d(TAG, "error = " + new String(error.networkResponse.data));
                    } else {
                        Log.d(TAG, "error = " + error);
                    }

                    super.deliverError(error);
                }
            };
        }
    }


}