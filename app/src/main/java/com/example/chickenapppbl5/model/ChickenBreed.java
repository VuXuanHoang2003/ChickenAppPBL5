package com.example.chickenapppbl5.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "Chicken")
public class ChickenBreed {
    @SerializedName("id")
    @PrimaryKey()
    @NonNull
    private long id;
    @SerializedName("uuid")
    @ColumnInfo()
    private String uuid;
    @SerializedName("url")
    @ColumnInfo()
    private String url;

    @SerializedName("predict")
    @ColumnInfo()
    private String predict;

    @SerializedName("labels")
    @ColumnInfo()
    private String labels;

    @SerializedName("chicken")
    @ColumnInfo
    private String chicken;

    @SerializedName("sick_chicken")
    @ColumnInfo
    private String sick_chicken;

    @SerializedName("other")
    @ColumnInfo
    private String other;


    public ChickenBreed(long id, String uuid, String url, String predict, String labels, String chicken, String sick_chicken, String other) {
        this.id = id;
        this.uuid = uuid;
        this.url = url;
        this.predict = predict;
        this.labels = labels;
        this.chicken = chicken;
        this.sick_chicken = sick_chicken;
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

    public String getSick_chicken() {
        return sick_chicken;
    }

    public void setSick_chicken(String sick_chicken) {
        this.sick_chicken = sick_chicken;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }
}
