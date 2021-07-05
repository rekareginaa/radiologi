package com.example.radiologi.data.dataSource.remote;

import androidx.lifecycle.LiveData;

import com.example.radiologi.data.dataSource.remote.response.LoginResponse;

import java.util.Map;

public interface RemoteDataSource {
    interface Login{
        LiveData<LoginResponse> loginUsers(Map<String, String> params);
    }
}
