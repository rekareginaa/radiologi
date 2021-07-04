package com.example.radiologi.data.dataSource.remote;

import androidx.lifecycle.LiveData;

import com.example.radiologi.data.dataSource.remote.response.DataItemLogin;

import java.util.Map;

public interface RemoteDataSource {
    interface Login{
        LiveData<DataItemLogin> loginUsers(Map<String, String> params);
    }
}
