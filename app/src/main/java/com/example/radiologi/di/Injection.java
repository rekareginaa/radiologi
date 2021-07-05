package com.example.radiologi.di;

import android.content.Context;

import com.example.radiologi.data.dataSource.local.AdminLocalDataSourceImpl;
import com.example.radiologi.data.dataSource.local.LocalDataSource;
import com.example.radiologi.data.dataSource.local.room.RadiologiDatabase;
import com.example.radiologi.data.dataSource.remote.AdminRemoteDataSourceImpl;
import com.example.radiologi.data.dataSource.remote.RemoteDataSource;
import com.example.radiologi.data.dataSource.remote.LoginRemoteDataSourceImpl;
import com.example.radiologi.data.repository.AccountRepositoryImpl;
import com.example.radiologi.data.repository.AdminRepositoryImpl;
import com.example.radiologi.data.repository.Repository;
import com.example.radiologi.utils.AppExecutors;

public class Injection {
    public static Repository.AccountRepository provideAccountRepository(Context context){
        RemoteDataSource.Login remoteDataSource = LoginRemoteDataSourceImpl.getInstance(context);
        return AccountRepositoryImpl.getInstance(remoteDataSource);
    }

    public static Repository.AdminRepository provideAdminRepository(Context context){
        final RadiologiDatabase database = RadiologiDatabase.getInstance(context);
        final RemoteDataSource.Admin remoteDataSource = AdminRemoteDataSourceImpl.getInstance(context);
        final LocalDataSource.Admin localDataSource = AdminLocalDataSourceImpl.getInstance(database.adminDao());
        final AppExecutors executors = new AppExecutors();

        return AdminRepositoryImpl.getInstance(remoteDataSource, localDataSource, executors);
    }
}
