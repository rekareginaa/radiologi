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

    @Query("SELECT * FROM item_admin_entity WHERE status =:status ")
    LiveData<List<ItemAdminEntity>> getAdminData(String status);

    @Query("SELECT * FROM item_admin_entity WHERE status !='0' ")
    LiveData<List<ItemAdminEntity>> getAdminData();

    @Query("DELETE FROM item_admin_entity")
    void deleteALlDataAdmin();
}
