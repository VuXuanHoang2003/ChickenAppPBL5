package com.example.chickenapppbl5;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.anychart.APIlib;
import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Column;
import com.anychart.core.ui.Label;
import com.anychart.enums.Anchor;
import com.anychart.enums.HoverMode;
import com.anychart.enums.Position;
import com.anychart.enums.TooltipPositionMode;
import com.example.chickenapppbl5.model.AppDatabaseChart;
import com.example.chickenapppbl5.model.ChickenSensor;
import com.example.chickenapppbl5.model.ChickenSensorDAO;
import com.example.chickenapppbl5.viewmodel.ChickenApiService;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ChartFoodActivity extends AppCompatActivity {

    private ChickenApiService apiService;
    private List<ChickenSensor> chickenList;
    private ChickenSensorDAO ChickenSensorDAO;
    private AppDatabaseChart appDatabaseChart;
    private int totalsum = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chart_food);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Intent intent = getIntent();
        apiService = new ChickenApiService();
        chickenList = new ArrayList<>();


        AnyChartView anyChartView = findViewById(R.id.any_chart_view);
        APIlib.getInstance().setActiveAnyChartView(anyChartView);


        Cartesian cartesian = AnyChart.column();
        List<DataEntry> data2 = new ArrayList<>();
        apiService.getSensors()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<List<ChickenSensor>>() {
                    @Override
                    public void onSuccess(@NonNull List<ChickenSensor> chickenSensors) {
                        Log.d("DEBUG","success");
                        for(ChickenSensor chicken: chickenSensors){
                            ChickenSensor i = new ChickenSensor(chicken.getTime(), chicken.getFood_weight(), chicken.getWater_weight());
                            chickenList.add(i);
                            long unixtime = Long.parseLong(i.getTime());
                            String date = new java.text.SimpleDateFormat("d/M/yyyy H:mm:ss").format(new java.util.Date(unixtime*1000L));
                            int tmp = Math.round(Float.parseFloat(i.getFood_weight()));
                            data2.add(new ValueDataEntry(date,tmp));
                            totalsum += tmp;
                            //Log.d("DEBUG", i.getTime());
                        }
                        // Convert unixtime to timedate

                        long time1 = Long.parseLong(chickenList.get(0).getTime());
                        long time2 = Long.parseLong(chickenList.get(chickenList.size()-1).getTime());
                        long daysBetween = 0;
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                            LocalDateTime dt1 = LocalDateTime.ofInstant(Instant.ofEpochSecond(time1), ZoneId.systemDefault());
                            LocalDateTime dt2 = LocalDateTime.ofInstant(Instant.ofEpochSecond(time2), ZoneId.systemDefault());
                            daysBetween = ChronoUnit.DAYS.between(dt1.toLocalDate(), dt2.toLocalDate()) + 1;
                        }
                        Column column = cartesian.column(data2);

                        column.tooltip()
                                .titleFormat("{%X}")
                                .position(Position.CENTER_BOTTOM)
                                .anchor(Anchor.CENTER_BOTTOM)
                                .offsetX(0d)
                                .offsetY(5d)
                                .format("{%Value}{groupsSeparator: }g");

                        cartesian.animation(true);
                        cartesian.title("Amount of food chicken consumed in all days");

                        cartesian.yScale().minimum(0d);

                        cartesian.yAxis(0).labels().format("{%Value}{groupsSeparator: }g");

                        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);
                        cartesian.interactivity().hoverMode(HoverMode.BY_X);

                        cartesian.xAxis(0).title("Hour");
                        cartesian.yAxis(0).title("Food consumed");

                        Label textMarker = cartesian.label(3);
                        textMarker.text("Total: " + totalsum + "g");
                        textMarker.fontSize(14d);
                        textMarker.fontColor("#000000");
                        textMarker.offsetX("60%");
                        textMarker.offsetY("15%");
                        textMarker.anchor("bottom");

                        Label textMarker1 = cartesian.label(4);
                        textMarker1.text("Average: " + totalsum /daysBetween + "g");
                        textMarker1.fontSize(14d);
                        textMarker1.fontColor("#000000");
                        textMarker1.offsetX("60%");
                        textMarker1.offsetY("20%");
                        textMarker1.anchor("bottom");


                        anyChartView.setChart(cartesian);

                        Cartesian cartesian1 = AnyChart.column();


                        AsyncTask.execute(new Runnable() {
                            @Override
                            public void run() {
                                appDatabaseChart = AppDatabaseChart.getInstance(getApplicationContext());
                                ChickenSensorDAO = appDatabaseChart.chickensensorDAO();
                                for(ChickenSensor chicken:chickenList){
                                    ChickenSensor i = new ChickenSensor(chicken.getTime(), chicken.getFood_weight(), chicken.getWater_weight());
                                    int tmp = Math.round(Float.parseFloat(i.getFood_weight()));
                                    data2.add(new ValueDataEntry(i.getFood_weight(),tmp));
                                    //Log.d("DEBUG", "sum:" + sum[0]);;
                                    ChickenSensorDAO.insert(i);
                                    //Log.d("DEBUG", i.getFood_weight());
                                }
                                //totalsum = sum[0];
                            }
                        });
                    }
                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d("DEBUG","Fail"+e.getMessage());
                    }
                });
    }
}