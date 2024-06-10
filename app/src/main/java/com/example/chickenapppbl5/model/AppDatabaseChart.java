package com.example.chickenapppbl5.model;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {ChickenSensor.class},version = 3,exportSchema = false)

public abstract class AppDatabaseChart extends RoomDatabase {
    public abstract ChickenSensorDAO chickensensorDAO();
    public static AppDatabaseChart instance;
    public  static AppDatabaseChart getInstance(Context context){
        if(instance==null){
            instance = Room.databaseBuilder(context,
                    AppDatabaseChart.class, "ChickenApp").fallbackToDestructiveMigration().build();
        }
        return instance;
    }

    public void insertSensor(ChickenSensor chickenSensor){
        new Thread(new Runnable() {
            @Override
            public void run() {
                chickensensorDAO().insertSensor(chickenSensor);
            }
        }).start();
    }
}
