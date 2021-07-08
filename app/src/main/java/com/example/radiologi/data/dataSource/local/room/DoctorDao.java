package com.example.radiologi.data.dataSource.local.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.radiologi.data.entitiy.ItemDoctorEntity;

import java.util.List;

@Dao
public interface DoctorDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertDoctorData(List<ItemDoctorEntity> doctorEntities);

    @Query("SELECT * FROM item_doctor_entity WHERE status =:status")
    LiveData<List<ItemDoctorEntity>> getDoctorData(String status);

    @Query("SELECT * FROM item_doctor_entity WHERE status ='1' OR '2'")
    LiveData<List<ItemDoctorEntity>> getDoctorData();

    @Query("DELETE FROM item_doctor_entity")
    void deleteAllDataDoctor();
}
