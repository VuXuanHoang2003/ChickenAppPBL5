package com.example.chickenapppbl5.model;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Day {
    private int id;
    private String name;
    private List<ChickenBreed> mListChicken;
    private Integer fromTime;
    private Integer toTime;

    public Day(int id, String name, List<ChickenBreed> mListChicken, Integer fromTime, Integer toTime) {
        this.id = id;
        this.name = name;
        this.mListChicken = mListChicken;
        this.fromTime = fromTime;
        this.toTime = toTime;
    }
    public Day() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String currentDate = dateFormat.format(new Date());
        this.name = currentDate.toString();
        this.mListChicken = new ArrayList<>();
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ChickenBreed> getmListChicken() {
        return mListChicken;
    }

    public void setmListChicken(List<ChickenBreed> mListChicken) {
        this.mListChicken = mListChicken;
    }


    public Integer getFromTime() {
        return fromTime;
    }

    public void setFromTime(Integer fromTime) {
        this.fromTime = fromTime;
    }

    public Integer getToTime() {
        return toTime;
    }

    public void setToTime(Integer toTime) {
        this.toTime = toTime;
    }
    public void addApi(ChickenBreed apiInfo) {
        this.mListChicken.add(apiInfo);
    }

    public void removeApi(ChickenBreed apiInfo) {
        this.mListChicken.remove(apiInfo);
    }

    // Phương thức để lấy danh sách các API trong khoảng thời gian cho trước
    public List<ChickenBreed> getApisInRange(long startTime, long endTime) {
        List<ChickenBreed> apisInRange = new ArrayList<>();
        for (ChickenBreed api : mListChicken) {
            Instant instantTime = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                instantTime = api.getInstantTime();
            }
            long epochSeconds = 0;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                epochSeconds = instantTime.getEpochSecond();
            }
            if (epochSeconds >= startTime && epochSeconds <= endTime) {
                apisInRange.add(api);
            }
        }
        return apisInRange;
    }
}
