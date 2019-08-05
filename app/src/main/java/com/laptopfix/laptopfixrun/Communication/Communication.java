package com.laptopfix.laptopfixrun.Communication;

import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class Communication {

    private static Communication mInstance;
    private RequestQueue request;
    private static Context mContext;

    private Communication(Context context) {
        mContext = context;
        request = getRequestQueue();
    }

    public static synchronized Communication getmInstance(Context context) {
        if(mInstance == null){
            mInstance = new Communication(context);
        }
        return mInstance;
    }

    private RequestQueue getRequestQueue() {
        if(request == null){
            request = Volley.newRequestQueue(mContext.getApplicationContext());
        }
        return request;
    }

    public <T> void addToRequestQueue(Request<T> request){
        getRequestQueue().add(request);
    }

}