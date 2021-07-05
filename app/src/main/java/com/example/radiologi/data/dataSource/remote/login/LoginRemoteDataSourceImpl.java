package com.example.radiologi.data.dataSource.remote.login;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.android.volley.Request;
import com.example.radiologi.data.dataSource.local.SharedPreferenceManager;
import com.example.radiologi.data.dataSource.remote.RemoteDataSource;
import com.example.radiologi.data.dataSource.remote.response.DataItemLogin;
import com.example.radiologi.data.dataSource.remote.response.LoginResponse;
import com.example.radiologi.networking.BaseVolley;
import com.example.radiologi.utils.Event;
import com.example.radiologi.utils.vo.Resource;
import com.example.radiologi.utils.vo.Status;
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
    public LiveData<Event<Resource<LoginResponse>>> loginUsers(Map<String, String> params) {
        MutableLiveData<Event<Resource<LoginResponse>>> result = new MutableLiveData<>();
        final Type type = new TypeToken<LoginResponse>(){}.getType();
        new BaseVolley<LoginResponse>(
                context,
                Request.Method.POST,
                LOGIN,
                type
        ){
            @Override
            protected void onLoading() {
                result.postValue(new Event<>(
                        new Resource<>(Status.LOADING, null, null)
                    )
                );
            }

            @Override
            protected void onSuccess(LoginResponse response) {
                final String status = response.getStatus();
                switch (status){
                    case "sukses":
                        Log.d("RESPONSE", status);
                        result.postValue(new Event<>(
                                new Resource<>(Status.SUCCESS, response, null)
                        ));
                        final DataItemLogin user = response.getData().get(0);
                        SharedPreferenceManager.savesStringPreferences(context, "nip", user.getNip());
                        SharedPreferenceManager.saveBooleanPreferences(context, "islogin", true);
                        SharedPreferenceManager.savesStringPreferences(context, "role", user.getRole());
                        SharedPreferenceManager.savesStringPreferences(context, "token", user.getToken());
                        break;
                    case "username":
                        result.postValue(new Event<>(
                                new Resource<>(Status.ERROR, null, "username salah")
                        ));
                        break;
                    case "password":
                        result.postValue(new Event<>(
                                new Resource<>(Status.ERROR, null, "Password Salah")
                        ));
                        break;
                    case "gagal":
                        result.postValue(new Event<>(
                                new Resource<>(Status.ERROR, null, "Login gagal")
                        ));
                        break;
                }
            }

            @Override
            protected void onError(String message) {
                result.postValue(new Event<>(
                        new Resource<>(Status.ERROR, null, message)
                ));
            }

            @Override
            protected Map<String, String> setParameter() {
                return params;
            }
        };
        return result;
    }
}
