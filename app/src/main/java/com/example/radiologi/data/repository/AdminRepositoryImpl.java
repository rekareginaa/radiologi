package com.example.radiologi.data.repository;

import androidx.lifecycle.LiveData;

import com.example.radiologi.data.NetworkFetchResource;
import com.example.radiologi.data.dataSource.local.LocalDataSource;
import com.example.radiologi.data.dataSource.remote.RemoteDataSource;
import com.example.radiologi.data.dataSource.remote.response.AdminItemResponse;
import com.example.radiologi.data.dataSource.remote.response.DoctorListResponse;
import com.example.radiologi.data.dataSource.remote.response.SimplesResponse;
import com.example.radiologi.data.dataSource.remote.vo.ApiResponse;
import com.example.radiologi.data.entitiy.ItemAdminEntity;
import com.example.radiologi.utils.AppExecutors;
import com.example.radiologi.utils.mapper.MapperHelper;
import com.example.radiologi.utils.vo.Resource;

import java.util.List;
import java.util.Map;

public class AdminRepositoryImpl implements Repository.AdminRepository{
    private final RemoteDataSource.Admin remoteDataSource;
    private final LocalDataSource.Admin localDataSource;
    private final AppExecutors appExecutors;

    public volatile static AdminRepositoryImpl instance = null;

    public AdminRepositoryImpl(RemoteDataSource.Admin remoteDataSource, LocalDataSource.Admin localDataSource, AppExecutors appExecutors) {
        this.remoteDataSource = remoteDataSource;
        this.localDataSource = localDataSource;
        this.appExecutors = appExecutors;
    }

    public static AdminRepositoryImpl getInstance(
            RemoteDataSource.Admin remoteDataSource,
            LocalDataSource.Admin localDataSource,
            AppExecutors executors
    ){
        if (instance == null){
            synchronized (AdminRepositoryImpl.class){
                instance = new AdminRepositoryImpl(remoteDataSource, localDataSource, executors);
            }
        }
        return instance;
    }

    @Override
    public LiveData<Resource<List<ItemAdminEntity>>> getNewAdminData(String nip, String page) {
        return new NetworkFetchResource<List<ItemAdminEntity>, AdminItemResponse>(){
            @Override
            protected LiveData<ApiResponse<AdminItemResponse>> createCall() {
                return remoteDataSource.getAdminData(nip, page);
            }

            @Override
            protected List<ItemAdminEntity> mapper(AdminItemResponse data) {
                List<ItemAdminEntity> allData = MapperHelper.mapAdminResponseToEntity(data, Integer.parseInt(page));
                appExecutors.diskIO().execute(() -> localDataSource.insertDataAdmin(allData));
                return MapperHelper.mapAdminNewResponseToEntity(data, Integer.parseInt(page));
            }
        }.asLiveData();
    }

    @Override
    public LiveData<List<ItemAdminEntity>> getNewAdminData(String status) {
        return localDataSource.getAdminData(status);
    }

    @Override
    public LiveData<Resource<List<ItemAdminEntity>>> getAdminData(String nip, String page) {
        return new NetworkFetchResource<List<ItemAdminEntity>, AdminItemResponse>(){
            @Override
            protected LiveData<ApiResponse<AdminItemResponse>> createCall() {
                return remoteDataSource.getAdminData(nip, page);
            }

            @Override
            protected List<ItemAdminEntity> mapper(AdminItemResponse data) {
                List<ItemAdminEntity> allData = MapperHelper.mapAdminResponseToEntity(data, Integer.parseInt(page));
                appExecutors.diskIO().execute(() -> localDataSource.insertDataAdmin(allData));
                return allData;
            }
        }.asLiveData();
    }

    @Override
    public LiveData<List<ItemAdminEntity>> getAdminData(String status) {
        return localDataSource.getAdminData(status);
    }


    @Override
    public LiveData<Resource<SimplesResponse>> getResponseUpdate(Map<String, String> params) {
        return remoteDataSource.getResponse(params);
    }

    @Override
    public LiveData<Resource<DoctorListResponse>> getDoctorList() {
        return remoteDataSource.getDoctorList();
    }
}
