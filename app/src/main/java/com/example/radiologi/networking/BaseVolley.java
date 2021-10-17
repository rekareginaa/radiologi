package com.example.radiologi.networking;

import android.util.Log;

import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.Map;

public abstract class BaseVolley<T> {

    private final String urlRequest;
    private final int method;
    private T responseObj;
    private final Type type;

    protected BaseVolley(
            int method,
            String urlRequest,
            Type type
    ) {
        this.urlRequest = urlRequest;
        this.method = method;
        this.type = type;

        doRequest();
    }

    protected void doRequest(){
        onLoading();
        StringRequest request = new StringRequest(method, urlRequest,
        response -> {
            Log.d("RESPONSE", response);
            try {
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
        VolleyService.get().getQueue().add(request);
    }

    protected abstract void onLoading();
    protected abstract void onSuccess(T response);
    protected abstract void onError(String message);
    protected abstract Map<String, String> setParameter();
}
