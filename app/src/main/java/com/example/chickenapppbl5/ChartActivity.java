package com.example.chickenapppbl5;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.chickenapppbl5.databinding.ActivityChartBinding;
import com.example.chickenapppbl5.model.AppDatabaseChart;
import com.example.chickenapppbl5.model.ChickenSensor;
import com.example.chickenapppbl5.model.ChickenSensorDAO;
import com.example.chickenapppbl5.viewmodel.ChickenApiService;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Calendar;
import java.util.List;

public class ChartActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    private ActivityChartBinding binding;

    private ChickenApiService apiService;
    private List<ChickenSensor> chickenList;
    private ChickenSensorDAO ChickenSensorDAO;
    private AppDatabaseChart appDatabaseChart;

    private EditText et_chart;
    int year;
    int month;
    int day;

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
        Calendar calendar = Calendar.getInstance();
        binding.etChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // open ChartFoodActivity
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(ChartActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // settext when date is selected
                        binding.etChart.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });
        binding.btnFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateDate()) {
                    Intent intent = new Intent(getApplicationContext(), ChartFoodActivity.class);
                    intent.putExtra("selectedDate", binding.etChart.getText().toString());
                    startActivity(intent);
                }
            }
        });
        binding.btnWater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateDate()) {
                    Intent intent = new Intent(getApplicationContext(), ChartWaterActivity.class);
                    intent.putExtra("selectedDate", binding.etChart.getText().toString());
                    startActivity(intent);
                }
            }
        });
        binding.imgbtnFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateDate()) {
                    Intent intent = new Intent(getApplicationContext(), ChartFoodActivity.class);
                    intent.putExtra("selectedDate", binding.etChart.getText().toString());
                    startActivity(intent);
                }
            }
        });
        binding.imgbtnWater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateDate()) {
                    Intent intent = new Intent(getApplicationContext(), ChartWaterActivity.class);
                    intent.putExtra("selectedDate", binding.etChart.getText().toString());
                    startActivity(intent);
                }
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

    // void check if the date is selected and validate return true
    public boolean validateDate() {
        if (binding.etChart.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please select a date", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            // get the selected date
            String selectedDate = binding.etChart.getText().toString();
            // split the date
            String[] parts = selectedDate.split("/");
//            // get the day
//            String day = parts[0];
//            // get the month
//            String month = parts[1];
//            // get the year
//            String year = parts[2];
            // validate the date
            if (Integer.parseInt(parts[0]) > 31 || Integer.parseInt(parts[0]) < 1) {
                Toast.makeText(this, "Invalid day", Toast.LENGTH_SHORT).show();
                return false;
            } else if (Integer.parseInt(parts[1]) > 12 || Integer.parseInt(parts[1]) < 1) {
                Toast.makeText(this, "Invalid month", Toast.LENGTH_SHORT).show();
                return false;
            } else if (Integer.parseInt(parts[2]) > Calendar.getInstance().get(Calendar.YEAR) || Integer.parseInt(parts[2]) < 2000) {
                Toast.makeText(this, "Invalid year", Toast.LENGTH_SHORT).show();
                return false;
            } else {
                return true;
            }
        }
    }
}