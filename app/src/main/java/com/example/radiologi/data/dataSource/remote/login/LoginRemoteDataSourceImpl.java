package com.example.radiologi.data.dataSource.remote.login;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.android.volley.Request;
import com.example.radiologi.data.dataSource.remote.RemoteDataSource;
import com.example.radiologi.data.dataSource.remote.response.DataItemLogin;
import com.example.radiologi.networking.BaseVolley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class LoginRemoteDataSourceImpl implements RemoteDataSource.Login {
    private static final String urlLogin="https://dbradiologi.000webhostapp.com/api/users/login";
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
    public LiveData<DataItemLogin> loginUsers(Map<String, String> params) {
        MutableLiveData<DataItemLogin> result = new MutableLiveData<>();
        new BaseVolley(context, Request.Method.POST, urlLogin) {
            @Override
            protected void onSuccess(String response) {
                try{
                    JSONObject object = new JSONObject(response);
                    JSONArray array = object.getJSONArray("data");
                    JSONObject jsonObject1 = array.getJSONObject(0);

                    final String role = jsonObject1.getString("role");
                    final String token = jsonObject1.getString("token");
                    final String nip = jsonObject1.getString("nip");
                    final String name = jsonObject1.getString("nama");
                    final String id = jsonObject1.getString("id");

                    DataItemLogin modelData = new DataItemLogin();
                    modelData.setRole(role);
                    modelData.setToken(token);
                    modelData.setNip(nip);
                    modelData.setNama(name);
                    modelData.setId(id);
                    modelData.setStatus(object.getString("status"));

                    result.postValue(modelData);

                }catch (JSONException e){
                    e.printStackTrace();
                }
            }

            @Override
            protected void onError(String message) {
                Log.d("ERROR", message);
            }

            @Nullable
            @Override
            protected Map<String, String> setParameter() {
                return params;
            }
        };
        return result;
    }
}
