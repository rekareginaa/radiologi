package com.example.radiologi.data.dataSource.remote;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.android.volley.Request;
import com.example.radiologi.data.dataSource.remote.response.AdminItemResponse;
import com.example.radiologi.data.dataSource.remote.response.DataItemUsers;
import com.example.radiologi.data.dataSource.remote.response.SimplesResponse;
import com.example.radiologi.data.dataSource.remote.response.UsersResponse;
import com.example.radiologi.data.dataSource.remote.vo.ApiResponse;
import com.example.radiologi.networking.BaseVolley;
import com.example.radiologi.utils.vo.Resource;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.radiologi.utils.Constants.ADMIN_ADD_IMG;
import static com.example.radiologi.utils.Constants.ADMIN_DATA;
import static com.example.radiologi.utils.Constants.EMPTY;
import static com.example.radiologi.utils.Constants.GET_TOKEN;
import static com.example.radiologi.utils.Constants.NIP;
import static com.example.radiologi.utils.Constants.SUCCESS;

public class AdminRemoteDataSourceImpl implements RemoteDataSource.Admin {

    private final Context context;
    @SuppressLint("StaticFieldLeak")
    private volatile static AdminRemoteDataSourceImpl instance;

    public AdminRemoteDataSourceImpl(Context context) {
        this.context = context;
    }

    public static AdminRemoteDataSourceImpl getInstance(Context context){
        if (instance == null){
            synchronized (AdminRemoteDataSourceImpl.class){
                instance = new AdminRemoteDataSourceImpl(context);
            }
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
            }

            @Override
            protected void onSuccess(AdminItemResponse response) {
                final String status = response.getStatus();
                if (status.equals(SUCCESS)){
                    result.postValue(ApiResponse.success(response));
                } else {
                    result.postValue(ApiResponse.error(EMPTY, null));
                }
            }

            @Override
            protected void onError(String message) {
                result.postValue(ApiResponse.error(message, null));
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

    @Override
    public LiveData<Resource<SimplesResponse>> getResponse(Map<String, String> params) {
        MutableLiveData<Resource<SimplesResponse>> result = new MutableLiveData<>();
        Type type = new TypeToken<SimplesResponse>(){}.getType();

        new BaseVolley<SimplesResponse>(
                context,
                Request.Method.POST,
                ADMIN_ADD_IMG,
                type
        ){
            @Override
            protected void onLoading() {
                result.postValue(Resource.loading(null));
            }

            @Override
            protected void onSuccess(SimplesResponse response) {
                if (response.getText().equals("Data Added")){
                    result.postValue(Resource.success(response));
                } else if (response.getText().equals("regis")){
                    result.postValue(Resource.error("Nomor Registrasi Sudah Ada", null));
                }
            }

            @Override
            protected void onError(String message) {
                result.postValue(Resource.error(message, null));
            }

            @Override
            protected Map<String, String> setParameter() {
                return params;
            }
        };
        return result;
    }

    @Override
    public String getToken() {
        final String[] resultToken = {""};
        Type type = new TypeToken<UsersResponse>(){}.getType();
        new BaseVolley<UsersResponse>(
                context,
                Request.Method.GET,
                GET_TOKEN,
                type
        ){
            @Override
            protected void onLoading() {}

            @Override
            protected void onSuccess(UsersResponse response) {
                List<DataItemUsers> data = response.getData();
                resultToken[0] = data.get(0).getToken();
            }

            @Override
            protected void onError(String message) { }

            @Override
            protected Map<String, String> setParameter() {
                return null;
            }
        };

        return resultToken[0];
    }
}
