package com.example.radiologi.data.dataSource.local;

import androidx.lifecycle.LiveData;

import com.example.radiologi.data.dataSource.local.room.DoctorDao;
import com.example.radiologi.data.entitiy.ItemDoctorEntity;

import java.util.List;

public class DoctorLocalDataSourceImpl implements LocalDataSource.Doctor {

    private static DoctorLocalDataSourceImpl INSTANCE;
    private final DoctorDao dbDao;

    public DoctorLocalDataSourceImpl(DoctorDao dbDao) {
        this.dbDao = dbDao;
    }

    public static DoctorLocalDataSourceImpl getInstance(DoctorDao dao){
        if (INSTANCE == null){
            INSTANCE = new DoctorLocalDataSourceImpl(dao);
        }
        return INSTANCE;
    }

    @Override
    public LiveData<List<ItemDoctorEntity>> getDoctorData(String status) {
        if (status.equals("0")){
            return dbDao.getDoctorData(status);
        }else {
            return dbDao.getDoctorData();
        }
    }

    @Override
    public void insertDataDoctor(List<ItemDoctorEntity> doctorEntities) {
        dbDao.insertDoctorData(doctorEntities);
    }
}
