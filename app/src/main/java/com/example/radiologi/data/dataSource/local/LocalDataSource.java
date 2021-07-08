package com.example.radiologi.data.dataSource.local;

import androidx.lifecycle.LiveData;

import com.example.radiologi.data.entitiy.ItemAdminEntity;
import com.example.radiologi.data.entitiy.ItemDoctorEntity;

import java.util.List;

public interface LocalDataSource {
    interface Admin{
        LiveData<List<ItemAdminEntity>>getAdminData(String status);
        void insertDataAdmin(List<ItemAdminEntity> adminEntities);
    }

    interface Doctor{
        LiveData<List<ItemDoctorEntity>> getDoctorData(String status);
        void insertDataDoctor(List<ItemDoctorEntity> doctorEntities);
    }
}
