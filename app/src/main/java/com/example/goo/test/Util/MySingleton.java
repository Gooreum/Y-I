package com.example.goo.test.Util;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by Goo on 2018-05-02.
 */

public class MySingleton {

    private static MySingleton minstance;
    private RequestQueue requestQueue;
    private static Context mCtx;

    private MySingleton(Context context) {
        mCtx = context;
        requestQueue = getRequestQueue();
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return requestQueue;
    }

    public static synchronized MySingleton getInstance(Context context) {
        if (minstance == null) {
            minstance = new MySingleton(context);
        }
        return minstance;
    }

    public<T> void addToRequestque(Request request){
        requestQueue.add(request);

    }

}
