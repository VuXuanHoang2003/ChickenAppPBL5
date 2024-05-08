package com.example.chickenapppbl5.model;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface NotifyItemDAO {
    @Query("SELECT * FROM Notify")
    List<NotifyItem> getAll();
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(NotifyItem...Notify);
}
