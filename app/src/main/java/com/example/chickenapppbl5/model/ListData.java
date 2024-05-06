package com.example.chickenapppbl5.model;

public class ListData {
    private String date;
    private String description;
    private int imageResource;

    public ListData(String date, String description, int imageResource) {
        this.date = date;
        this.description = description;
        this.imageResource = imageResource;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getImageResource() {
        return imageResource;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }
}
