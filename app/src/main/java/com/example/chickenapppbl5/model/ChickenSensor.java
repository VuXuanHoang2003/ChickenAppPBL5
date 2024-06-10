package com.example.chickenapppbl5.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "ChickenSensor")
public class ChickenSensor {
    @SerializedName("time")
    @PrimaryKey()
    @NonNull
    private int time;
    @SerializedName("food_weight")
    @ColumnInfo
    private String food_weight;
    @SerializedName("water_weight")
    @ColumnInfo
    private String water_weight;

    public ChickenSensor(int time, String food_weight, String water_weight) {
        this.time = time;
        this.food_weight = food_weight;
        this.water_weight = water_weight;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getFood_weight() {
        return food_weight;
    }

    public void setFood_weight(String food_weight) {
        this.food_weight = food_weight;
    }
    public String getWater_weight() {
        return water_weight;
    }
    public void setWater_weight(String water_weight) {
        this.water_weight = water_weight;
    }
}
