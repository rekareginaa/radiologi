package com.example.radiologi.data.repository;

import androidx.lifecycle.LiveData;

import com.example.radiologi.data.NetworkBoundResource;
import com.example.radiologi.data.dataSource.local.LocalDataSource;
import com.example.radiologi.data.dataSource.remote.RemoteDataSource;
import com.example.radiologi.data.dataSource.remote.response.AdminItemResponse;
import com.example.radiologi.data.dataSource.remote.response.DataItemAdmin;
import com.example.radiologi.data.dataSource.remote.response.DoctorListResponse;
import com.example.radiologi.data.dataSource.remote.response.SimplesResponse;
import com.example.radiologi.data.dataSource.remote.vo.ApiResponse;
import com.example.radiologi.data.entitiy.ItemAdminEntity;
import com.example.radiologi.utils.AppExecutors;
import com.example.radiologi.utils.vo.Resource;

import java.util.ArrayList;
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
    public LiveData<Resource<List<ItemAdminEntity>>> getAdminData(String nip, String status) {
        return new NetworkBoundResource<List<ItemAdminEntity>, AdminItemResponse>(appExecutors) {
            @Override
            protected LiveData<List<ItemAdminEntity>> loadFromDB() {
                return localDataSource.getAdminData(status);
            }

            @Override
            protected Boolean shouldFetch(List<ItemAdminEntity> data) {
                return true;
            }

            @Override
            protected LiveData<ApiResponse<AdminItemResponse>> createCall() {
                return remoteDataSource.getAdminData(nip);
            }

            @Override
            protected void saveCallResult(AdminItemResponse data) {
                final List<DataItemAdmin> dataResponse = data.getData();
                ArrayList<ItemAdminEntity> listAdmin = new ArrayList<>();

               for (int i= 0; i<dataResponse.size(); i++){
                   final DataItemAdmin items = dataResponse.get(i);
                   ItemAdminEntity adminEntity = new ItemAdminEntity(
                           items.getNoregis(),
                           items.getPengirim(),
                           items.getTanglahir(),
                           items.getNamapasien(),
                           items.getDiagnosa(),
                           items.getGender(),
                           items.getTtd(),
                           items.getPenerima(),
                           items.getId(),
                           items.getGambar(),
                           items.getNorekam(),
                           items.getStatus()
                   );
                   listAdmin.add(adminEntity);
               }
               localDataSource.insertDataAdmin(listAdmin);

            }
        }.asLiveData();
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
