package com.example.chickenapppbl5.model;

import androidx.room.ColumnInfo;

import com.google.gson.annotations.SerializedName;

public class ChickenAnalyze {
    @SerializedName("food_consumed")
    @ColumnInfo
    private float food_weight;
    @SerializedName("water_consumed")
    @ColumnInfo
    private float water_weight;

    public ChickenAnalyze(float food_weight, float water_weight) {
        this.food_weight = food_weight;
        this.water_weight = water_weight;
    }
    public float getFood_weight() {
        return food_weight;
    }
    public void setFood_weight(float food_weight) {
        this.food_weight = food_weight;
    }
    public float getWater_weight() {
        return water_weight;
    }
    public void setWater_weight(float water_weight) {
        this.water_weight = water_weight;
    }
}
