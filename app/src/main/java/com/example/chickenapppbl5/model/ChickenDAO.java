package com.example.chickenapppbl5.model;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;
@Dao
public interface ChickenDAO {
    @Query("SELECT * FROM Chicken")
    List<ChickenBreed> getAll();
    // select time from_time to_time
    @Query("SELECT * FROM Chicken WHERE time BETWEEN :from_time AND :to_time")
    List<ChickenBreed> getChickensTime(int from_time, int to_time);
    // select where temp > temp
    @Query("SELECT * FROM Chicken WHERE hctemp > :temp")
    List<ChickenBreed> getHighTemp(int temp);
    @Query("SELECT COUNT(*) FROM Chicken WHERE uuid = :uuid")
    int countByUuid(String uuid);
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(ChickenBreed...Chicken);
//    @Query("UPDATE Chicken SET url=:url WHERE id=:id")
//    void update(int id, String url);
}
