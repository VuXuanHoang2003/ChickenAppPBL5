package com.example.chickenapppbl5.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "Chicken")
public class ChickenBreed {
    @SerializedName("id")
    @ColumnInfo
    private long id;
    @SerializedName("uuid")
    @PrimaryKey()
    @NonNull
    private String uuid;
    @SerializedName("url")
    @ColumnInfo()
    private String url;

    @SerializedName("predict")
    @ColumnInfo()
    private String predict;

    @SerializedName("infared")
    @ColumnInfo()
    private String infared;

    @SerializedName("labels")
    @ColumnInfo()
    private String labels;

    @SerializedName("chicken")
    @ColumnInfo
    private String chicken;

    @SerializedName("non-chicken")
    @ColumnInfo
    private String non_chicken;

    @SerializedName("time")
    @ColumnInfo
    private int time;

    @SerializedName("highest_chicken_temp")
    @ColumnInfo
    private float hctemp;

    @SerializedName("other")
    @ColumnInfo
    private String other;


    public ChickenBreed(long id, String uuid, String url, String predict, String infared, String labels, String chicken, String non_chicken, int time, float hctemp, String other) {
        this.id = id;
        this.uuid = uuid;
        this.url = url;
        this.predict = predict;
        this.infared = infared;
        this.labels = labels;
        this.chicken = chicken;
        this.non_chicken = non_chicken;
        this.time = time;
        this.hctemp = hctemp;
        this.other = other;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPredict() {
        return predict;
    }

    public void setPredict(String predict) {
        this.predict = predict;
    }
    public String getInfared() {
        return infared;
    }
    public void setInfared(String infared) {
        this.infared = infared;
    }

    public String getLabels() {
        return labels;
    }

    public void setLabels(String labels) {
        this.labels = labels;
    }

    public String getChicken() {
        return chicken;
    }

    public void setChicken(String chicken) {
        this.chicken = chicken;
    }

    public String getNon_chicken() {
        return non_chicken;
    }

    public void setNon_chicken(String non_chicken) {
        this.non_chicken = non_chicken;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public float getHctemp() {
        return hctemp;
    }

    public void setHctemp(float hctemp) {
        this.hctemp = hctemp;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }
}
