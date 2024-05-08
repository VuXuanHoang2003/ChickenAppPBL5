package com.example.chickenapppbl5.model;

import androidx.annotation.NonNull;
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
    private int food_weight;
    @SerializedName("water_weight")
    private int water_weight;

    public ChickenSensor(int time, int food_weight, int water_weight) {
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

    public int getFood_weight() {
        return food_weight;
    }

    public void setFood_weight(int food_weight) {
        this.food_weight = food_weight;
    }
    public int getWater_weight() {
        return water_weight;
    }
    public void setWater_weight(int water_weight) {
        this.water_weight = water_weight;
    }
}
