package business.services;

import com.android.volley.Response;

/**
 * Created by Sanjeev on 03/01/16.
 */
public interface ServiceListener <T> extends Response.Listener<T>, Response.ErrorListener{
}
