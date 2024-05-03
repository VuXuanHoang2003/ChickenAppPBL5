package com.example.chickenapppbl5.model;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {ChickenBreed.class},version = 3,exportSchema = false)

public abstract class AppDatabase extends RoomDatabase {
    public abstract ChickenDAO contactDAO();
    public static AppDatabase instance;
    public  static AppDatabase getInstance(Context context){
        if(instance==null){
            instance = Room.databaseBuilder(context,
                    AppDatabase.class, "ChickenApp").fallbackToDestructiveMigration().build();
        }
        return instance;
    }
}
