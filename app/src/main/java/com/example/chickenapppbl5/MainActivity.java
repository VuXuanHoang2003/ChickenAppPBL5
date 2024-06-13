package com.example.chickenapppbl5;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chickenapppbl5.databinding.ActivityMainBinding;
import com.example.chickenapppbl5.model.AppDatabase;
import com.example.chickenapppbl5.model.AppDatabaseChart;
import com.example.chickenapppbl5.model.ChickenBreed;
import com.example.chickenapppbl5.model.ChickenDAO;
import com.example.chickenapppbl5.model.ChickenSensor;
import com.example.chickenapppbl5.model.ChickenSensorDAO;
import com.example.chickenapppbl5.viewmodel.Calendar7DaysAdapter;
import com.example.chickenapppbl5.viewmodel.ChickenAdapter;
import com.example.chickenapppbl5.viewmodel.ChickenApiService;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements ChickenAdapter.OnChickenListener{

    BottomNavigationView bottomNavigationView;

    private ChickenApiService apiService;
    private ChickenAdapter chickensAdapter;
    private List<ChickenBreed> chickenList;
    private List<ChickenSensor> chickenList2;
    private ActivityMainBinding binding;
    private ChickenDAO ChickenDAO;
    private ChickenSensorDAO ChickenSensorDAO;
    private AppDatabase appDatabase;
    private AppDatabaseChart appDatabaseChart;
    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    private LocalDate selectedDate;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());

        // Set the content view to the inflated layout
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;

        });

        bottomNavigationView = findViewById(R.id.bottom_navigator);
        bottomNavigationView.setSelectedItemId(R.id.calendar);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.calendar:
                        return true;
                    case R.id.chart:
                        startActivity(new Intent(getApplicationContext(),ChartActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.notifications:
                        startActivity(new Intent(getApplicationContext(),NotificationsActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
        initWidgets();
        apiService = new ChickenApiService();
        chickenList = new ArrayList<>();
        chickenList2 = new ArrayList<>();
        if (isInternetAvailable()) {
            apiService.getAll(0)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSingleObserver<List<ChickenBreed>>() {
                        @Override
                        public void onSuccess(@NonNull List<ChickenBreed> chickenBreeds) {
                            Log.d("HEHEHE","success");
                            for(ChickenBreed chicken: chickenBreeds){
                                ChickenBreed i = new ChickenBreed(chicken.getId(),chicken.getUuid(), chicken.getUrl(), chicken.getPredict(), chicken.getInfared(), chicken.getLabels(), chicken.getChicken(), chicken.getNon_chicken(), chicken.getTime(), chicken.getHctemp(), chicken.getOther());
                                chickenList.add(i);
                                //Log.d("HEHEHE",i.getUuid());
                            }
                            Log.d("HEHEHE", String.valueOf(chickenList.size()));
                            AsyncTask.execute(new Runnable() {
                                @Override
                                public void run() {
                                    appDatabase = AppDatabase.getInstance(getApplicationContext());
                                    ChickenDAO = appDatabase.chickenDAO();
                                    for(ChickenBreed chicken:chickenList){
                                        //ChickenDAO.insertChicken(chicken);
                                        int count = ChickenDAO.countByUuid(chicken.getUuid());
                                        if (count <= 0) {
                                            //appDatabase.insertChicken(chicken);
                                            ChickenDAO.insertChicken(chicken);
                                            Log.d("HEHEHE", "insertedchicken");
                                        } else {
                                            // This chicken is a duplicate
                                        }
                                    }
                                }
                            });
                        }
                        @Override
                        public void onError(@NonNull Throwable e) {
                            Log.d("HEHEHE","Fail"+e.getMessage());
                        }
                    });

            apiService.getSensors()
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSingleObserver<List<ChickenSensor>>() {
                        @Override
                        public void onSuccess(@NonNull List<ChickenSensor> chickenSensors) {
                            Log.d("HEHEHE","success");
                            for(ChickenSensor chicken: chickenSensors){
                                ChickenSensor i = new ChickenSensor(chicken.getTime(), chicken.getFood_weight(), chicken.getWater_weight());
                                chickenList2.add(i);
                                //Log.d("HEHEHE",i.getUuid());
                            }
                            Log.d("HEHEHE", String.valueOf(chickenList2.size()));
                            AsyncTask.execute(new Runnable() {
                                @Override
                                public void run() {
                                    appDatabaseChart = AppDatabaseChart.getInstance(getApplicationContext());
                                    ChickenSensorDAO = appDatabaseChart.chickensensorDAO();
                                    for(ChickenSensor chicken:chickenList2){
                                        //ChickenSensorDAO.insertSensor(chicken);
                                        // check time of the sensor if time is higher than last time of chickenlist2
                                        // if true, insert the sensor
                                        // if false, do nothing
                                        int count = ChickenSensorDAO.countByTime(chicken.getTime());
                                        if (count <= 0) {
                                            //appDatabaseChart.insertSensor(chicken);
                                            ChickenSensorDAO.insertSensor(chicken);
                                            Log.d("HEHEHE", "insertedsensor");
                                        } else {
                                            // This sensor is a duplicate
                                        }
                                    }
                                }
                            });
                        }
                        @Override
                        public void onError(@NonNull Throwable e) {
                            Log.d("HEHEHE","Fail"+e.getMessage());
                        }
                    });
        }
        selectedDate = LocalDate.now();
        setMonthView();
        Intent intent = getIntent();
        binding.btnOpencalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, CalendarByMonthActivity.class);
                startActivity(i);
            }
        });

    }

    public boolean isInternetAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    private void initWidgets()
    {
        calendarRecyclerView = findViewById(R.id.rv_calendar7days);
        monthYearText = findViewById(R.id.tv_monthyear);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setMonthView()
    {
        monthYearText.setText(monthYearFromDate(selectedDate));
        ArrayList<String> daysInMonth = daysInMonthArray(selectedDate);

        Calendar7DaysAdapter calendarAdapter = new Calendar7DaysAdapter(daysInMonth, this::onItemClick);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 1);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private ArrayList<String> daysInMonthArray(LocalDate date) {
        ArrayList<String> daysInMonthArray = new ArrayList<>();
        LocalDate sevenDaysAgo = LocalDate.now().minusDays(7);

        for (int i = 1; i < 8; i++) {
            LocalDate currentDate = sevenDaysAgo.plusDays(i);
            daysInMonthArray.add(String.valueOf(currentDate.getDayOfMonth()) + "/" + String.valueOf(currentDate.getMonthValue()) + "/" + String.valueOf(currentDate.getYear()));
        }

        return daysInMonthArray;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private String monthYearFromDate(LocalDate date)
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy");
        return date.format(formatter);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void previousMonthAction(View view)
    {
        selectedDate = selectedDate.minusMonths(1);
        setMonthView();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void nextMonthAction(View view)
    {
        selectedDate = selectedDate.plusMonths(1);
        setMonthView();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
//    @Override
    public void onItemClick(int position, String dayText)
    {
        if(!dayText.equals(""))
        {
            String[] parts = dayText.split("/");
            dayText = parts[0];
            //String message = "Selected Date " + dayText + " " + monthYearFromDate(selectedDate);
            //Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            Intent i = new Intent(this, ChickenDashboardDayActivity.class);
            // Convert the month to a number
            int month = selectedDate.getMonthValue();
            // Toast month
            //Toast.makeText(this, "Month: " + month, Toast.LENGTH_LONG).show();
            i.putExtra("day", dayText);
            i.putExtra("month", String.valueOf(month));
            i.putExtra("year", String.valueOf(selectedDate.getYear()));
            startActivity(i);
        }
    }


    public void onClick(View view, int position) {
//        final ChickenBreed ck = chickenList.get(position);
//        Intent i = new Intent(this, ChickenItemActivity.class);
//        i.putExtra("id", ck.getId());
//        i.putExtra("uuid", ck.getUuid());
//        i.putExtra("image", ck.getUrl());
//        Log.i("hello", ck.getUuid());
//        Toast.makeText(
//                this,
//                "You clicked " + ck.getUuid(),
//                Toast.LENGTH_SHORT
//        );
//        startActivity(i);
    }
    @Override
    public void onChickenClick(int position) {

    }
}