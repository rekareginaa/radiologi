package com.example.radiologi.data.repository;

import androidx.lifecycle.LiveData;

import com.example.radiologi.data.dataSource.remote.response.LoginResponse;

import java.util.Map;

public interface Repository {
    interface AccountRepository{
        LiveData<LoginResponse> loginUser(Map< String, String> params);
    }
}
