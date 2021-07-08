package com.example.radiologi.networking;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class VolleyService {
    private static VolleyService instance;
    private final RequestQueue queue;

    private VolleyService(Context context){
        this.queue = Volley.newRequestQueue(context);
    }

    /**
     * a singleton pattern, initialize class just one time
     * @param ctx needed for application context or activityContext
     */
    public static VolleyService getInstance(Context ctx){
        if (instance == null){
            synchronized (VolleyService.class){
                instance = new VolleyService(ctx);
            }
        }
        return instance;
    }

    public static VolleyService get(){
        return instance;
    }

    public RequestQueue getQueue(){
        return queue;
    }
}
