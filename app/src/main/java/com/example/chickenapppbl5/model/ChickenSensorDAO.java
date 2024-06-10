package com.example.chickenapppbl5.model;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;
@Dao
public interface ChickenSensorDAO {
    @Query("SELECT * FROM ChickenSensor")
    List<ChickenSensor> getAllSensor();
    @Query("SELECT * FROM ChickenSensor WHERE time BETWEEN :from_time AND :to_time")
    List<ChickenSensor> getSensorsTime(int from_time, int to_time);
    @Query("SELECT COUNT(*) FROM ChickenSensor WHERE time = :time")
    int countByTime(int time);
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertSensor(ChickenSensor...ChickenSensor);
//    @Query("UPDATE Chicken SET url=:url WHERE id=:id")
//    void update(int id, String url);
}
