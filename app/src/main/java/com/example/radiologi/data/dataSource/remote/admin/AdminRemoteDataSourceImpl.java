package com.example.radiologi.data.dataSource.remote.admin;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.android.volley.Request;
import com.example.radiologi.data.dataSource.remote.RemoteDataSource;
import com.example.radiologi.data.dataSource.remote.response.AdminItemResponse;
import com.example.radiologi.data.dataSource.remote.vo.ApiResponse;
import com.example.radiologi.data.dataSource.remote.vo.StatusResponse;
import com.example.radiologi.networking.BaseVolley;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import static com.example.radiologi.utils.Constants.ADMIN_DATA;
import static com.example.radiologi.utils.Constants.NIP;

public class AdminRemoteDataSourceImpl implements RemoteDataSource.Admin {

    private final Context context;
    private AdminRemoteDataSourceImpl instance;

    public AdminRemoteDataSourceImpl(Context context) {
        this.context = context;
    }

    public AdminRemoteDataSourceImpl getInstance(Context context){
        if (instance == null){
            instance = new AdminRemoteDataSourceImpl(context);
        }
        return instance;
    }

    @Override
    public LiveData<ApiResponse<AdminItemResponse>> getAdminData(String nip) {
        MutableLiveData<ApiResponse<AdminItemResponse>> result = new MutableLiveData<>();
        final Type type = new TypeToken<AdminItemResponse>(){}.getType();
        new BaseVolley<AdminItemResponse>(
                context,
                Request.Method.POST,
                ADMIN_DATA,
                type
        ) {
            @Override
            protected void onLoading() {
                result.postValue(new ApiResponse<>(
                        StatusResponse.EMPTY,
                        null,
                        null
                ));
            }

            @Override
            protected void onSuccess(AdminItemResponse response) {
                result.postValue(new ApiResponse<>(
                        StatusResponse.SUCCESS,
                        response,
                        null
                ));
            }

            @Override
            protected void onError(String message) {
                result.postValue(new ApiResponse<>(
                        StatusResponse.ERROR,
                        null,
                        message
                ));
            }

            @Override
            protected Map<String, String> setParameter() {
                Map<String, String> params = new HashMap<>();
                params.put(NIP, nip);
                return params;
            }
        };
        return result;
    }
}
