package jtkaiser.imags.database;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by jtkai on 2/26/2018.
 */

public class VolleyHelper {

    private static VolleyHelper sVolleyHelper;
    private RequestQueue requestQueue;
    private static Context mCtx;

    private VolleyHelper(Context context){
        mCtx = context;
        requestQueue = getRequestQueue();
    }

    private RequestQueue getRequestQueue(){
        if(requestQueue == null)
            requestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        return requestQueue;
    }

    public static synchronized VolleyHelper getInstance(Context context){
        if(sVolleyHelper == null){
            sVolleyHelper = new VolleyHelper(context);
        }
        return sVolleyHelper;
    }

    public<T> void addToRequestQueue(Request<T> request){
        getRequestQueue().add(request);
    }
}
