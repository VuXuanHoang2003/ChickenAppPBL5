package com.example.chickenapppbl5.model;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {ChickenBreed.class},version = 9,exportSchema = false)

public abstract class AppDatabase extends RoomDatabase {
    public abstract ChickenDAO chickenDAO();
    public static AppDatabase instance;
    public  static AppDatabase getInstance(Context context){
        if(instance==null){
            instance = Room.databaseBuilder(context,
                    AppDatabase.class, "ChickenApp").fallbackToDestructiveMigration().build();
        }
        return instance;
    }

    public static void destroyInstance() {
        instance = null;
    }

    //insert data
    public void insertChicken(ChickenBreed chickenBreed){
        new Thread(new Runnable() {
            @Override
            public void run() {
                chickenDAO().insertChicken(chickenBreed);
            }
        }).start();
    }


    //get all data
    public void getAllChicken(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                chickenDAO().getAll();
            }
        }).start();
    }

}
