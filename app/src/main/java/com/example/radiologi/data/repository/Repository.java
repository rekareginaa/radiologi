package com.example.radiologi.data.repository;

import androidx.lifecycle.LiveData;

import com.example.radiologi.data.dataSource.remote.response.DataItemLogin;

import java.util.Map;

public interface Repository {
    interface AccountRepository{
        LiveData<DataItemLogin> loginUser(Map< String, String> params);
    }
}
