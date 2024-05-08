package com.example.chickenapppbl5.model;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;
@Dao
public interface ChickenSensorDAO {
    @Query("SELECT * FROM ChickenSensor")
    List<ChickenSensor> getAll();
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(ChickenSensor...ChickenSensor);
//    @Query("UPDATE Chicken SET url=:url WHERE id=:id")
//    void update(int id, String url);
}
