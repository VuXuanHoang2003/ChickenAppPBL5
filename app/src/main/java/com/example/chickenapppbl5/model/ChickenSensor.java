package com.example.chickenapppbl5.model;

import androidx.room.Entity;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "ChickenSensor")
public class ChickenSensor {
    @SerializedName("time")
    private int time;

    @SerializedName("food_weight")
    private int food_weight;

    public ChickenSensor(int time, int food_weight) {
        this.time = time;
        this.food_weight = food_weight;
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
}
