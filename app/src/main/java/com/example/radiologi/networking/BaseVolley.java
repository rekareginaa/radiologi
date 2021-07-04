package com.example.radiologi.networking;

import android.content.Context;
import android.util.Log;

import com.android.volley.toolbox.StringRequest;

import java.util.Map;

public abstract class BaseVolley {

    private final String urlRequest;
    private final int method;
    private final Context context;

    protected BaseVolley(
            Context context,
            int method,
            String urlRequest
    ) {
        this.urlRequest = urlRequest;
        this.method = method;
        this.context = context;

        doRequest();
    }

    protected void doRequest(){
        StringRequest request = new StringRequest(method, urlRequest,
        response -> {
            Log.d("RESPONSE", response);
            onSuccess(response);
        }, error -> {
            final Throwable cause = error.getCause();
            if (cause != null){
                onError(cause.getLocalizedMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                return setParameter();
            }
        };
        VolleyService.getInstance(context).getQueue().add(request);
    }

    protected abstract void onSuccess(String response);
    protected abstract void onError(String message);
    protected abstract Map<String, String> setParameter();
}
