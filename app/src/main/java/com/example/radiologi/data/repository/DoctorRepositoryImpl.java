package com.example.radiologi.data.repository;

import androidx.lifecycle.LiveData;

import com.example.radiologi.data.NetworkBoundResource;
import com.example.radiologi.data.dataSource.local.LocalDataSource;
import com.example.radiologi.data.dataSource.remote.RemoteDataSource;
import com.example.radiologi.data.dataSource.remote.response.AdminItemResponse;
import com.example.radiologi.data.dataSource.remote.response.DataItemAdmin;
import com.example.radiologi.data.dataSource.remote.vo.ApiResponse;
import com.example.radiologi.data.entitiy.ItemDoctorEntity;
import com.example.radiologi.utils.AppExecutors;
import com.example.radiologi.utils.vo.Resource;

import java.util.ArrayList;
import java.util.List;

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
    public LiveData<Resource<List<ItemDoctorEntity>>> getDoctorData(String nip, String status) {
        return new NetworkBoundResource<List<ItemDoctorEntity>, AdminItemResponse>(appExecutors){
            @Override
            protected LiveData<List<ItemDoctorEntity>> loadFromDB() {
                return localDataSource.getDoctorData(status);
            }

            @Override
            protected Boolean shouldFetch(List<ItemDoctorEntity> data) {
                return true;
            }

            @Override
            protected LiveData<ApiResponse<AdminItemResponse>> createCall() {
                return remoteDataSource.getDoctorData(nip);
            }

            @Override
            protected void saveCallResult(AdminItemResponse data) {
                final List<DataItemAdmin> dataResponse = data.getData();
                ArrayList<ItemDoctorEntity> listDoctor = new ArrayList<>();

                for (int i=0; i<dataResponse.size(); i++){
                    final DataItemAdmin items = dataResponse.get(i);
                    ItemDoctorEntity doctorEntity = new ItemDoctorEntity(
                            items.getId(),
                            items.getNoregis(),
                            items.getPengirim(),
                            items.getTanglahir(),
                            items.getNamapasien(),
                            items.getDiagnosa(),
                            items.getGender(),
                            items.getTtd(),
                            items.getPenerima(),
                            items.getGambar(),
                            items.getNorekam(),
                            items.getStatus()
                    );
                    listDoctor.add(doctorEntity);
                }
                localDataSource.insertDataDoctor(listDoctor);
            }
        }.asLiveData();
    }
}
