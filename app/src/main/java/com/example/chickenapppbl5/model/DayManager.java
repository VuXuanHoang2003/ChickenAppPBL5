package com.example.chickenapppbl5.model;

import android.os.Build;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class DayManager {
    private List<Day> listDay;
    public DayManager(){
        listDay = new ArrayList<>();
        initializeListDay();
    }
    private void initializeListDay() {
        // Lấy ngày hiện tại
        LocalDate currentDate = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            currentDate = LocalDate.now();
        }
        long startOfDay = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            startOfDay = currentDate.atStartOfDay(ZoneId.systemDefault()).toEpochSecond();
        }
        long endOfDay = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            endOfDay = currentDate.atTime(LocalTime.MAX).atZone(ZoneId.systemDefault()).toEpochSecond();
        }

        // Tạo một đối tượng Day mới cho ngày hiện tại và thêm vào danh sách
        Day newDay = new Day(1, currentDate.toString(), new ArrayList<>(), (int) startOfDay, (int) endOfDay);
        listDay.add(newDay);
    }


    private void updateListDay() {
        // Lấy ngày hiện tại
        LocalDate currentDate = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            currentDate = LocalDate.now();
        }
        long startOfDay = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startOfDay = currentDate.atStartOfDay(ZoneId.systemDefault()).toEpochSecond();
        }
        long endOfDay = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            endOfDay = currentDate.atTime(LocalTime.MAX).atZone(ZoneId.systemDefault()).toEpochSecond();
        }

        if (listDay.isEmpty()) {
            // Nếu danh sách rỗng, tạo một đối tượng Day mới cho ngày hiện tại và thêm vào danh sách
            Day newDay = new Day(1, currentDate.toString(), new ArrayList<>(), (int) startOfDay, (int) endOfDay);
            listDay.add(newDay);
        } else {
            // Lấy ngày của ngày cuối cùng trong danh sách
            LocalDate lastDayDate = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                lastDayDate = LocalDate.parse(listDay.get(listDay.size() - 1).getName());
            }

            // Kiểm tra nếu ngày hiện tại khác với ngày của ngày cuối cùng trong danh sách
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                if (!currentDate.isEqual(lastDayDate)) {
                    // Tạo một đối tượng Day mới cho ngày mới và thêm vào danh sách
                    Day newDay = new Day(listDay.size() + 1, currentDate.toString(), new ArrayList<>(), (int) startOfDay, (int) endOfDay);
                    listDay.add(newDay);
                }
            }
        }
    }

    public List<Day> getListDay() {
        // Kiểm tra và cập nhật danh sách nếu cần
        updateListDay();
        return listDay;
    }
}
