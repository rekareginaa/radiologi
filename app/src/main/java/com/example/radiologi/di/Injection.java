package com.example.radiologi.di;

import android.content.Context;

import com.example.radiologi.data.dataSource.local.AdminLocalDataSourceImpl;
import com.example.radiologi.data.dataSource.local.DoctorLocalDataSourceImpl;
import com.example.radiologi.data.dataSource.local.LocalDataSource;
import com.example.radiologi.data.dataSource.local.room.RadiologiDatabase;
import com.example.radiologi.data.dataSource.remote.AdminRemoteDataSourceImpl;
import com.example.radiologi.data.dataSource.remote.DoctorRemoteDataSourceImpl;
import com.example.radiologi.data.dataSource.remote.RemoteDataSource;
import com.example.radiologi.data.dataSource.remote.LoginRemoteDataSourceImpl;
import com.example.radiologi.data.repository.AccountRepositoryImpl;
import com.example.radiologi.data.repository.AdminRepositoryImpl;
import com.example.radiologi.data.repository.DoctorRepositoryImpl;
import com.example.radiologi.data.repository.Repository;
import com.example.radiologi.utils.AppExecutors;

public class Injection {
    public static Repository.AccountRepository provideAccountRepository(){
        RemoteDataSource.Login remoteDataSource = LoginRemoteDataSourceImpl.getInstance();
        return AccountRepositoryImpl.getInstance(remoteDataSource);
    }

    public static Repository.AdminRepository provideAdminRepository(Context context){
        final RadiologiDatabase database = RadiologiDatabase.getInstance(context);
        final RemoteDataSource.Admin remoteDataSource = AdminRemoteDataSourceImpl.getInstance();
        final LocalDataSource.Admin localDataSource = AdminLocalDataSourceImpl.getInstance(database.adminDao());
        final AppExecutors executors = new AppExecutors();

        return AdminRepositoryImpl.getInstance(remoteDataSource, localDataSource, executors);
    }

    public static Repository.DoctorRepository provideDoctorRepository(Context context){
        final RadiologiDatabase database = RadiologiDatabase.getInstance(context);
        final RemoteDataSource.Doctor remoteDataSource = DoctorRemoteDataSourceImpl.getInstance();
        final LocalDataSource.Doctor localDataSource = DoctorLocalDataSourceImpl.getInstance(database.doctorDao());
        final AppExecutors executors = new AppExecutors();

        return DoctorRepositoryImpl.getInstance(remoteDataSource, localDataSource, executors);
    }
}
