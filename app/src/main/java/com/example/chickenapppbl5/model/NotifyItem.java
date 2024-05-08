package com.example.chickenapppbl5.model;

import android.net.Uri;

import androidx.room.Entity;

@Entity(tableName = "Notify")
public class NotifyItem {
    private String id;
    private String title;
    private String uuid;
    private String img;
    private String time;
    private String timenow;

    public NotifyItem(){
    }
    public NotifyItem(String id, String title, String uuid, String img, String time, String timenow) {
        this.id = id;
        this.title = title;
        this.uuid = uuid;
        this.img = img;
        this.time = time;
        this.timenow = timenow;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getUuid() {
        return uuid;
    }
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
    public String getImg() {
        return img;
    }
    public void setImg(String img) {
        this.img = img;
    }
    public String getTime() {
        return time;
    }
    public void setTime(String time) {
        this.time = time;
    }
    public String getTimenow() {
        return timenow;
    }
    public void setTimenow(String timenow) {
        this.timenow = timenow;
    }
}
