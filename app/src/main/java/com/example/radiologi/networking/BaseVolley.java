package com.example.radiologi.networking;

import android.content.Context;
import android.util.Log;

import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.Map;

public abstract class BaseVolley<T> {

    private final String urlRequest;
    private final int method;
    private final Context context;
    private T responseObj;
    private final Type type;

    protected BaseVolley(
            Context context,
            int method,
            String urlRequest,
            Type type
    ) {
        this.urlRequest = urlRequest;
        this.method = method;
        this.context = context;
        this.type = type;

        doRequest();
    }

    protected void doRequest(){
        onLoading();
        StringRequest request = new StringRequest(method, urlRequest,
        response -> {
            try {
                Log.d("RESPONSE", response);
                Gson gson = new Gson();
                responseObj = gson.fromJson(response, type);
                onSuccess(responseObj);
            }catch (Exception e){
                if (e.getMessage() != null){
                    onError(e.getMessage());
                }
                e.printStackTrace();
            }
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

    protected abstract void onLoading();
    protected abstract void onSuccess(T response);
    protected abstract void onError(String message);
    protected abstract Map<String, String> setParameter();
}
