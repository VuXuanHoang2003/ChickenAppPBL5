package com.example.chickenapppbl5;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
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
import com.anychart.chart.common.listener.Event;
import com.anychart.chart.common.listener.ListenersInterface;
import com.anychart.charts.Cartesian;
import com.anychart.charts.Pie;
import com.anychart.core.cartesian.series.Column;
import com.anychart.core.ui.Label;
import com.anychart.enums.Align;
import com.anychart.enums.Anchor;
import com.anychart.enums.HoverMode;
import com.anychart.enums.LegendLayout;
import com.anychart.enums.Position;
import com.anychart.enums.TooltipPositionMode;
import com.example.chickenapppbl5.databinding.ActivityChartBinding;
import com.example.chickenapppbl5.databinding.ActivityMainBinding;
import com.example.chickenapppbl5.model.AppDatabase;
import com.example.chickenapppbl5.model.AppDatabaseChart;
import com.example.chickenapppbl5.model.ChickenSensor;
import com.example.chickenapppbl5.model.ChickenDAO;
import com.example.chickenapppbl5.model.ChickenSensorDAO;
import com.example.chickenapppbl5.viewmodel.ChickenAdapter;
import com.example.chickenapppbl5.viewmodel.ChickenApiService;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ChartActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    private ActivityChartBinding binding;

    private ChickenApiService apiService;
    private List<ChickenSensor> chickenList;
    private ChickenSensorDAO ChickenSensorDAO;
    private AppDatabaseChart appDatabaseChart;

    private int totalsum = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityChartBinding.inflate(getLayoutInflater());

        // Set the content view to the inflated layout
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        binding.btnFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // open ChartFoodActivity
                startActivity(new Intent(getApplicationContext(), ChartFoodActivity.class));
            }
        });
        binding.btnWater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // open ChartWaterActivity
                startActivity(new Intent(getApplicationContext(), ChartWaterActivity.class));
            }
        });
        binding.imgbtnFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // open ChartFoodActivity
                startActivity(new Intent(getApplicationContext(), ChartFoodActivity.class));
            }
        });
        binding.imgbtnWater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // open ChartWaterActivity
                startActivity(new Intent(getApplicationContext(), ChartWaterActivity.class));
            }
        });


        bottomNavigationView = findViewById(R.id.bottom_navigator);
        bottomNavigationView.setSelectedItemId(R.id.chart);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.calendar:
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        overridePendingTransition(0,0);
                        return true;
//                    case R.id.image:
//                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
//                        overridePendingTransition(0,0);
//                        return true;
                    case R.id.chart:
                        return true;
                    case R.id.notifications:
                        startActivity(new Intent(getApplicationContext(),NotificationsActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });

    }
}