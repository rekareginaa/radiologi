package com.example.radiologi.data.dataSource.local.room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.radiologi.data.entitiy.ItemAdminEntity;


@Database(
        entities = {ItemAdminEntity.class},
        version = 1,
        exportSchema = false
)
public abstract class RadiologiDatabase extends RoomDatabase {

    public abstract AdminDao adminDao();

    private static volatile RadiologiDatabase INSTANCE;

    public static RadiologiDatabase getInstance(Context context){
        if (INSTANCE == null){
            synchronized (RadiologiDatabase.class){
                INSTANCE = Room.databaseBuilder(
                        context.getApplicationContext(),
                        RadiologiDatabase.class,
                        "radiology.db"
                ).build();
            }
        }
        return INSTANCE;
    }
}
