package com.example.chickenapppbl5;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
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
import com.example.chickenapppbl5.model.ChickenBreed;
import com.example.chickenapppbl5.model.ChickenDAO;
import com.example.chickenapppbl5.viewmodel.Calendar7DaysAdapter;
import com.example.chickenapppbl5.viewmodel.ChickenAdapter;
import com.example.chickenapppbl5.viewmodel.ChickenApiService;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ChickenAdapter.OnChickenListener{

    BottomNavigationView bottomNavigationView;

    private ChickenApiService apiService;
    private ChickenAdapter chickensAdapter;
    private List<ChickenBreed> chickenList;
    private ActivityMainBinding binding;
    private ChickenDAO ChickenDAO;
    private AppDatabase appDatabase;

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
//                        startActivity(new Intent(getApplicationContext(),CalendarActivity.class));
//                        overridePendingTransition(0,0);
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
            String message = "Selected Date " + dayText + " " + monthYearFromDate(selectedDate);
            //Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            Intent i = new Intent(this, ChickenDayActivity.class);
            // Convert the month to a number
            int month = selectedDate.getMonthValue();
            // Toast month
            //Toast.makeText(this, "Month: " + month, Toast.LENGTH_LONG).show();
            i.putExtra("day", dayText);
            i.putExtra("month", String.valueOf(month));
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