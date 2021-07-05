package com.example.radiologi.data.dataSource.remote.login;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.android.volley.Request;
import com.example.radiologi.data.dataSource.remote.RemoteDataSource;
import com.example.radiologi.data.dataSource.remote.response.LoginResponse;
import com.example.radiologi.networking.BaseVolley;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Map;

import static com.example.radiologi.utils.Constants.LOGIN;

public class LoginRemoteDataSourceImpl implements RemoteDataSource.Login {
    private final Context context;

    @SuppressLint("StaticFieldLeak")
    private static LoginRemoteDataSourceImpl instance;

    public static LoginRemoteDataSourceImpl getInstance(Context context){
        if (instance == null){
            instance = new LoginRemoteDataSourceImpl(context);
        }
        return instance;
    }

    public LoginRemoteDataSourceImpl(Context context) {
        this.context = context;
    }

    @Override
    public LiveData<LoginResponse> loginUsers(Map<String, String> params) {
        MutableLiveData<LoginResponse> result = new MutableLiveData<>();
        final Type type = new TypeToken<LoginResponse>(){}.getType();
        new BaseVolley<LoginResponse>(
                context,
                Request.Method.POST,
                LOGIN,
                type
        ){
            @Override
            protected void onSuccess(LoginResponse response) {
                result.postValue(response);
            }

            @Override
            protected void onError(String message) {
                if (message != null){
                    Log.d("ERROR", message);
                }
            }

            @Override
            protected Map<String, String> setParameter() {
                return params;
            }
        };
        return result;
    }
}
