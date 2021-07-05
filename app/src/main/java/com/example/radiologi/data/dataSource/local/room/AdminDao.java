package com.example.radiologi.data.dataSource.local.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.radiologi.data.entitiy.ItemAdminEntity;

import java.util.List;

@Dao
public interface AdminDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAdminData(List<ItemAdminEntity> adminEntities);

    @Query("SELECT * FROM itemadminentity WHERE status =:status")
    LiveData<List<ItemAdminEntity>> getAdminData(String status);

    @Query("SELECT * FROM itemadminentity WHERE status ='1' OR '2'")
    LiveData<List<ItemAdminEntity>> getAdminData();

    @Query("DELETE FROM itemadminentity")
    void deleteALlDataAdmin();
}
