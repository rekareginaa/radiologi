package com.example.radiologi.data.dataSource.remote;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.android.volley.Request;
import com.example.radiologi.data.dataSource.remote.response.AdminItemResponse;
import com.example.radiologi.data.dataSource.remote.vo.ApiResponse;
import com.example.radiologi.networking.BaseVolley;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import static com.example.radiologi.utils.Constants.DOCTOR_DATA;
import static com.example.radiologi.utils.Constants.NIP;
import static com.example.radiologi.utils.Constants.SUCCESS;

public class DoctorRemoteDataSourceImpl implements RemoteDataSource.Doctor{

    private final Context context;
    @SuppressLint("StaticFieldLeak")
    private volatile static DoctorRemoteDataSourceImpl instance;

    public DoctorRemoteDataSourceImpl(Context context) {
        this.context = context;
    }

    public static DoctorRemoteDataSourceImpl getInstance(Context context){
        if (instance == null){
            synchronized (DoctorRemoteDataSourceImpl.class){
                instance = new DoctorRemoteDataSourceImpl(context);
            }
        }
        return instance;
    }

    @Override
    public LiveData<ApiResponse<AdminItemResponse>> getDoctorData(String nip) {
        MutableLiveData<ApiResponse<AdminItemResponse>> result = new MutableLiveData<>();
        final Type type = new TypeToken<AdminItemResponse>(){}.getType();
        new BaseVolley<AdminItemResponse>(
                context,
                Request.Method.POST,
                DOCTOR_DATA,
                type
        ) {
            @Override
            protected void onLoading() {}

            @Override
            protected void onSuccess(AdminItemResponse response) {
                final String status = response.getStatus();
                if (status.equals(SUCCESS)){
                    result.postValue(ApiResponse.success(response));
                } else {
                    result.postValue(ApiResponse.error(response.getStatus(), null));
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
}
