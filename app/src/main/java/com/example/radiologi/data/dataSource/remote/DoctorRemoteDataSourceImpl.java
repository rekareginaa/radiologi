package com.example.radiologi.data.dataSource.remote;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.android.volley.Request;
import com.example.radiologi.data.dataSource.remote.response.AdminItemResponse;
import com.example.radiologi.data.dataSource.remote.response.DataItemUsers;
import com.example.radiologi.data.dataSource.remote.response.SimpleResponse;
import com.example.radiologi.data.dataSource.remote.response.UsersResponse;
import com.example.radiologi.data.dataSource.remote.vo.ApiResponse;
import com.example.radiologi.networking.BaseVolley;
import com.example.radiologi.utils.vo.Resource;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.radiologi.utils.Constants.DOCTOR_DATA;
import static com.example.radiologi.utils.Constants.DOCTOR_UPDATE;
import static com.example.radiologi.utils.Constants.GET_TOKEN;
import static com.example.radiologi.utils.Constants.NIP;
import static com.example.radiologi.utils.Constants.SUCCESS;

public class DoctorRemoteDataSourceImpl implements RemoteDataSource.Doctor{

    private volatile static DoctorRemoteDataSourceImpl instance;

    public DoctorRemoteDataSourceImpl() {
    }

    public static DoctorRemoteDataSourceImpl getInstance(){
        if (instance == null){
            synchronized (DoctorRemoteDataSourceImpl.class){
                instance = new DoctorRemoteDataSourceImpl();
            }
        }
        return instance;
    }

    @Override
    public LiveData<ApiResponse<AdminItemResponse>> getDoctorData(String nip) {
        MutableLiveData<ApiResponse<AdminItemResponse>> result = new MutableLiveData<>();
        final Type type = new TypeToken<AdminItemResponse>(){}.getType();
        new BaseVolley<AdminItemResponse>(
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

    @Override
    public LiveData<Resource<SimpleResponse>> getResponse(Map<String, String> params) {
        MutableLiveData<Resource<SimpleResponse>> result = new MutableLiveData<>();
        Type type = new TypeToken<SimpleResponse>(){}.getType();

        new BaseVolley<SimpleResponse>(
                Request.Method.POST,
                DOCTOR_UPDATE,
                type
        ){
            @Override
            protected void onLoading() {
                result.postValue(Resource.loading(null));
            }

            @Override
            protected void onSuccess(SimpleResponse response) {
                result.postValue(Resource.success(response));
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
                Request.Method.GET,
                GET_TOKEN,
                type
        ){
            @Override
            protected void onLoading() {}

            @Override
            protected void onSuccess(UsersResponse response) {
                List<DataItemUsers> data = response.getData();
                resultToken[0] = data.get(1).getToken();
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
