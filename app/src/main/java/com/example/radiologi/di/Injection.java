package com.example.radiologi.di;

import android.content.Context;

import com.example.radiologi.data.dataSource.remote.RemoteDataSource;
import com.example.radiologi.data.dataSource.remote.login.LoginRemoteDataSourceImpl;
import com.example.radiologi.data.repository.AccountRepositoryImpl;
import com.example.radiologi.data.repository.Repository;

public class Injection {
    public static Repository.AccountRepository provideAccountRepository(Context context){
        RemoteDataSource.Login remoteDataSource = new LoginRemoteDataSourceImpl(context);
        return new AccountRepositoryImpl(remoteDataSource);
    }
}
