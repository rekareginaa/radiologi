package com.example.radiologi.data.dataSource.remote;

import androidx.lifecycle.LiveData;

import com.example.radiologi.data.dataSource.remote.response.AdminItemResponse;
import com.example.radiologi.data.dataSource.remote.response.LoginResponse;
import com.example.radiologi.data.dataSource.remote.response.SimpleResponse;
import com.example.radiologi.data.dataSource.remote.response.SimplesResponse;
import com.example.radiologi.data.dataSource.remote.vo.ApiResponse;
import com.example.radiologi.utils.Event;
import com.example.radiologi.utils.vo.Resource;

import java.util.Map;

public interface RemoteDataSource {
    interface Login{
        LiveData<Event<Resource<LoginResponse>>> loginUsers(Map<String, String> params);
    }

    interface Admin{
        LiveData<ApiResponse<AdminItemResponse>> getAdminData(String nip);
        LiveData<Resource<SimplesResponse>> getResponse(Map<String, String> params);
        String getToken();
    }

    interface Doctor{
        LiveData<ApiResponse<AdminItemResponse>> getDoctorData(String nip);
        LiveData<Resource<SimpleResponse>> getResponse(Map<String, String> params);
        String getToken();
    }
}
