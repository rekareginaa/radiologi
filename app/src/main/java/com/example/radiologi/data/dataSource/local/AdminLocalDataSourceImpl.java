package com.example.radiologi.data.dataSource.local;

import androidx.lifecycle.LiveData;

import com.example.radiologi.data.dataSource.local.room.AdminDao;
import com.example.radiologi.data.entitiy.ItemAdminEntity;

import java.util.List;

public class AdminLocalDataSourceImpl implements LocalDataSource.Admin{

    private static AdminLocalDataSourceImpl INSTANCE;
    private final AdminDao dbDao;

    public AdminLocalDataSourceImpl(AdminDao dbDao) {
        this.dbDao = dbDao;
    }

    public static AdminLocalDataSourceImpl getInstance(AdminDao dao){
        if (INSTANCE == null){
            INSTANCE = new AdminLocalDataSourceImpl(dao);
        }
        return INSTANCE;
    }

    @Override
    public LiveData<List<ItemAdminEntity>> getAdminData(String status) {
        if (status.equals("0")){
            return dbDao.getAdminData(status);
        }else {
            return dbDao.getAdminData();
        }
    }

    @Override
    public void insertDataAdmin(List<ItemAdminEntity> adminEntities) {
        dbDao.deleteALlDataAdmin();
        dbDao.insertAdminData(adminEntities);
    }
}
