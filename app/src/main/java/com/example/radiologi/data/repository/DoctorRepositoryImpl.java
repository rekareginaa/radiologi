package com.example.radiologi.data.repository;

import static com.example.radiologi.utils.Constants.TOKENS;

import androidx.lifecycle.LiveData;

import com.example.radiologi.data.NetworkFetchResource;
import com.example.radiologi.data.dataSource.local.LocalDataSource;
import com.example.radiologi.data.dataSource.remote.RemoteDataSource;
import com.example.radiologi.data.dataSource.remote.response.AdminItemResponse;
import com.example.radiologi.data.dataSource.remote.response.SimpleResponse;
import com.example.radiologi.data.dataSource.remote.vo.ApiResponse;
import com.example.radiologi.data.entitiy.ItemDoctorEntity;
import com.example.radiologi.utils.AppExecutors;
import com.example.radiologi.utils.mapper.MapperHelper;
import com.example.radiologi.utils.vo.Resource;

import java.util.List;
import java.util.Map;

public class DoctorRepositoryImpl implements Repository.DoctorRepository{
    private final RemoteDataSource.Doctor remoteDataSource;
    private final LocalDataSource.Doctor localDataSource;
    private final AppExecutors appExecutors;

    public DoctorRepositoryImpl(RemoteDataSource.Doctor remoteDataSource, LocalDataSource.Doctor localDataSource, AppExecutors appExecutors) {
        this.remoteDataSource = remoteDataSource;
        this.localDataSource = localDataSource;
        this.appExecutors = appExecutors;
    }

    private volatile static DoctorRepositoryImpl instance = null;

    public static DoctorRepositoryImpl getInstance(
            RemoteDataSource.Doctor remoteDataSource,
            LocalDataSource.Doctor localDataSource,
            AppExecutors appExecutors
    ){
        if (instance == null){
            synchronized (DoctorRepositoryImpl.class){
                instance = new DoctorRepositoryImpl(remoteDataSource, localDataSource, appExecutors);
            }
        }
        return instance;
    }

    @Override
    public LiveData<Resource<List<ItemDoctorEntity>>> getNewDoctorData(String nip, String page) {
        return new NetworkFetchResource<List<ItemDoctorEntity>, AdminItemResponse>(){
            @Override
            protected LiveData<ApiResponse<AdminItemResponse>> createCall() {
                return remoteDataSource.getNewDoctorData(nip, page);
            }

            @Override
            protected List<ItemDoctorEntity> mapper(AdminItemResponse data) {
                List<ItemDoctorEntity> allData = MapperHelper.mapDoctorResponseToEntity(data, Integer.parseInt(page));
                appExecutors.diskIO().execute(() -> localDataSource.insertDataDoctor(allData));
                return MapperHelper.mapNewDoctorResponseToEntity(data, Integer.parseInt(page));
            }
        }.asLiveData();
    }

    @Override
    public LiveData<List<ItemDoctorEntity>> getNewDoctorData(String status) {
        return localDataSource.getDoctorData(status);
    }

    @Override
    public LiveData<Resource<List<ItemDoctorEntity>>> getDoctorData(String nip, String page) {
        return new NetworkFetchResource<List<ItemDoctorEntity>, AdminItemResponse>(){
            @Override
            protected LiveData<ApiResponse<AdminItemResponse>> createCall() {
                return remoteDataSource.getDoctorData(nip, page);
            }

            @Override
            protected List<ItemDoctorEntity> mapper(AdminItemResponse data) {
                List<ItemDoctorEntity> allData = MapperHelper.mapNewDoctorResponseToEntity(data, Integer.parseInt(page));
                appExecutors.diskIO().execute(() -> localDataSource.insertDataDoctor(allData));
                return allData;
            }
        }.asLiveData();
    }

    @Override
    public LiveData<List<ItemDoctorEntity>> getDoctorData(String status) {
        return localDataSource.getDoctorData(status);
    }

    @Override
    public LiveData<Resource<SimpleResponse>> getResponseUpdate(Map<String, String> params) {
        final String tokens = remoteDataSource.getToken();
        params.put(TOKENS, tokens);
        return remoteDataSource.getResponse(params);
    }
}
