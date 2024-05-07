package com.example.chickenapppbl5.model;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.time.Instant;

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

    @SerializedName("infared")
    @ColumnInfo()
    private String infared;

    @SerializedName("time")
    @ColumnInfo
    private long time;
    @SerializedName("labels")
    @ColumnInfo()
    private String labels;

    @SerializedName("chicken")
    @ColumnInfo
    private String chicken;

    @SerializedName("non-chicken")
    @ColumnInfo
    private String non_chicken;

    public ChickenBreed(long id, String uuid, String url, String predict, String infared, long time, String labels, String chicken, String non_chicken) {
        this.id = id;
        this.uuid = uuid;
        this.url = url;
        this.predict = predict;
        this.infared = infared;
        this.time = time;
        this.labels = labels;
        this.chicken = chicken;
        this.non_chicken = non_chicken;
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

    public String getInfared() {
        return infared;
    }

    public void setInfared(String infared) {
        this.infared = infared;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getNon_chicken() {
        return non_chicken;
    }

    public void setNon_chicken(String non_chicken) {
        this.non_chicken = non_chicken;
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public Instant getInstantTime() {
        return Instant.ofEpochSecond(time);
    }
}
