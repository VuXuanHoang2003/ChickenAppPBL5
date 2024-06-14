package com.example.chickenapppbl5;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chickenapppbl5.databinding.ActivityChickenDashboardDayBinding;
import com.example.chickenapppbl5.model.AppDatabase;
import com.example.chickenapppbl5.model.AppDatabaseChart;
import com.example.chickenapppbl5.model.ChickenAnalyze;
import com.example.chickenapppbl5.model.ChickenBreed;
import com.example.chickenapppbl5.model.ChickenDAO;
import com.example.chickenapppbl5.model.ChickenSensor;
import com.example.chickenapppbl5.model.ChickenSensorDAO;
import com.example.chickenapppbl5.viewmodel.ChickenApiService;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ChickenDashboardDayActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    private ChickenApiService apiService;
    private List<ChickenBreed> chickenList;
    private List<ChickenAnalyze> chickenList2, chickenList3;
    private ActivityChickenDashboardDayBinding binding;
    private ChickenDAO ChickenDAO;
    private ChickenSensorDAO ChickenSensorDAO;
    private AppDatabase appDatabase;
    private AppDatabaseChart appDatabaseChart;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private FirebaseFirestore firestore;
    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    private LocalDate selectedDate;
    private int totalchickens = 0, number = 0;
    private float totalfood  = 0, totalwater = 0, avgfood = 0, avgwater = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityChickenDashboardDayBinding.inflate(getLayoutInflater());

        // Set the content view to the inflated layout
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;

        });

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        firestore = FirebaseFirestore.getInstance();
        myRef = database.getReference();
        // if cannot find the reference, create a new one

        //Log.d("HEHEHE", "number: " + number);
        Intent intent = getIntent();
        binding.dayTitle.setText("Overall of day "+ intent.getStringExtra("day") + "/" + intent.getStringExtra("month") + "/" + intent.getStringExtra("year"));
        long unixTime = 0;
        try {
            String date = intent.getStringExtra("year") + "-" + intent.getStringExtra("month") + "-" + intent.getStringExtra("day") + " 00:00:00";
            unixTime = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date).getTime();
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        // from time to time 1 day
        // convert unixtime from string to integer
        int from_time = (int) (unixTime / 1000);
        int to_time = from_time + 86400;
        Log.d("HEHEHE", "from_time: " + from_time + " to_time: " + to_time);
        apiService = new ChickenApiService();
        chickenList = new ArrayList<>();
        chickenList2 = new ArrayList<>();
        chickenList3 = new ArrayList<>();
        if (!isInternetAvailable()){
            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    appDatabase = AppDatabase.getInstance(getApplicationContext());
                    appDatabaseChart = AppDatabaseChart.getInstance(getApplicationContext());
                    ChickenDAO = appDatabase.chickenDAO();
                    ChickenSensorDAO = appDatabaseChart.chickensensorDAO();
                    List<ChickenBreed> tempChickenList = new ArrayList<>();
                    List<ChickenSensor> tempChickenList2 = new ArrayList<>();
                    tempChickenList = ChickenDAO.getAll();
                    tempChickenList2 = ChickenSensorDAO.getAllSensor();
                    Log.d("HEHEHE", "tempChickenList: " + tempChickenList.size() + " tempChickenList2: " + tempChickenList2.size());
                    tempChickenList = ChickenDAO.getChickensTime(from_time, to_time);
                    tempChickenList2 = ChickenSensorDAO.getSensorsTime(from_time - 604800, to_time);
                    Log.d("HEHEHE", "tempChickenList: " + tempChickenList.size() + " tempChickenList2: " + tempChickenList2.size());
                    if (tempChickenList.size() != 0) {
                        float minTemp = 34;
                        float maxtemp = 0;
                        for (ChickenBreed chicken: tempChickenList){
                            totalchickens += Integer.parseInt(chicken.getChicken());
                            if (chicken.getHctemp() < minTemp){
                                minTemp = chicken.getHctemp();
                            }
                            if (chicken.getHctemp() > maxtemp){
                                maxtemp = chicken.getHctemp();
                            }
                        }
                        // set time from_time to_time from image has lowest time and highest time and convert unixtime to string H:mm:ss
                        String lowestTime = String.valueOf(tempChickenList.get(0).getTime());
                        String highestTime = String.valueOf(tempChickenList.get(tempChickenList.size() - 1).getTime());
                        lowestTime = convertUnixTime(Long.parseLong(lowestTime));
                        highestTime = convertUnixTime(Long.parseLong(highestTime));
                        binding.tvTime.setText("Time: " + lowestTime + " - " + highestTime);
                        // save number of chickens
                        binding.tvNumberofchickens.setText("Number of chickens: " + totalchickens);
                        // getvalue of number of chickens from firebase
                        // settext need internet to get value
                        binding.tvMissingchickens.setText("Number of missing chickens: " +"No internet available");
                        binding.tvFood.setText("Total amount of food: " + "No internet available");
                        binding.tvWater.setText("Total amount of water: " + "No internet available");
                        binding.tvAveragefood.setText("Average amount of food: " + "No internet available");
                        binding.tvAveragewater.setText("Average amount of water: " + "No internet available");
                        binding.tvNotifyfood.setText("Compared to average food: " + "No internet available");
                        binding.tvNotifywater.setText("Compared to average water: " + "No internet available");
                        // save lowest and highest temperature
                        binding.tvLowtemp.setText("Lowest temperature: " + minTemp);
                        binding.tvHightemp.setText("Highest temperature: " + maxtemp);
                    } else {
                        binding.tvNumberofimages.setText("Number of images: 0");
                        binding.tvTime.setText("Time: --");
                        binding.tvNumberofchickens.setText("Number of chickens: 0");
                        binding.tvMissingchickens.setText("Number of missing chickens: 0");
                        binding.tvLowtemp.setText("Lowest temperature: --");
                        binding.tvHightemp.setText("Highest temperature: --");
                    }
                    // no internet
                    binding.tvNotifyownerchickens.setText("Number of owner's chickens: " + "No internet available");
                    runOnUiThread(new Runnable() {
                        @SuppressLint("NotifyDataSetChanged")
                        @Override
                        public void run() {
                            // if dataset is not changed, still view the old data
                        }
                    });
                }
            });
        }
        else {
            apiService.getAnalyzeTime(from_time - 518400, to_time - 86400)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSingleObserver<ChickenAnalyze>() {
                        @Override
                        public void onSuccess(@NonNull ChickenAnalyze chickenAnalyzes) {
                            Log.d("HEHEHE","success");
                            Log.d("HEHEHE", "from_time: " + (from_time - 518400) + " to_time: " + (to_time - 86400) + " " + chickenAnalyzes.getFood_weight() + " " + chickenAnalyzes.getWater_weight());
                            ChickenAnalyze i = new ChickenAnalyze(chickenAnalyzes.getFood_weight(), chickenAnalyzes.getWater_weight());
                            Log.d("HEHEHE", "food: " + chickenAnalyzes.getFood_weight() + " water: " + chickenAnalyzes.getWater_weight());
                            chickenList2.add(i);
                            avgfood = i.getFood_weight();
                            avgwater = i.getWater_weight();
                            avgfood = avgfood / 7;
                            avgwater = avgwater / 7;
                            AsyncTask.execute(new Runnable() {
                                @Override
                                public void run() {
                                }
                            });
                        }
                        @Override
                        public void onError(@NonNull Throwable e) {
                            Log.d("HEHEHE","Fail"+e.getMessage());
                        }
                    });
            apiService.getAnalyzeTime(from_time, to_time)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSingleObserver<ChickenAnalyze>() {
                        @Override
                        public void onSuccess(@NonNull ChickenAnalyze chickenAnalyzes) {
                            Log.d("HEHEHE","success");
                            Log.d("HEHEHE", "from_time: " + from_time + " to_time: " + to_time);
                            ChickenAnalyze i = new ChickenAnalyze(chickenAnalyzes.getFood_weight(), chickenAnalyzes.getWater_weight());
                            Log.d("HEHEHE", "food: " + i.getFood_weight() + " water: " + i.getWater_weight());
                            chickenList3.add(i);
                            totalfood = i.getFood_weight();
                            totalwater = i.getWater_weight();

                            AsyncTask.execute(new Runnable() {
                                @Override
                                public void run() {
                                }
                            });
                        }
                        @Override
                        public void onError(@NonNull Throwable e) {
                            Log.d("HEHEHE","Fail"+e.getMessage());
                        }
                    });
            apiService.getChickens(from_time, to_time)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSingleObserver<List<ChickenBreed>>() {
                        @Override
                        public void onSuccess(@NonNull List<ChickenBreed> chickenBreeds) {
                            Log.d("HEHEHE","success");
                            for(ChickenBreed chicken: chickenBreeds){
                                ChickenBreed i = new ChickenBreed(chicken.getId(),chicken.getUuid(), chicken.getUrl(), chicken.getPredict(), chicken.getInfared(), chicken.getLabels(), chicken.getChicken(), chicken.getNon_chicken(), chicken.getTime(), chicken.getHctemp(), chicken.getOther());
                                //chickenList.add(i);
                                long unixTime = Long.parseLong(String.valueOf(chicken.getTime()));
                                Date date = new Date(unixTime * 1000L);
                                Calendar cal = Calendar.getInstance();
                                cal.setTime(date);
                                if (cal.get(Calendar.DAY_OF_MONTH) == Integer.parseInt(intent.getStringExtra("day")) && (cal.get(Calendar.MONTH) + 1) == Integer.parseInt(intent.getStringExtra("month"))){
                                    chickenList.add(i);
                                    if (Integer.parseInt(i.getChicken()) > totalchickens) {
                                        totalchickens = Integer.parseInt(i.getChicken());
                                    }
                                }
                                //Log.d("HEHEHE",i.getUuid());
                            }
                            Log.d("HEHEHE", "22chickenList: " + chickenList.size() + " chickenList2: " + chickenList2.size());
                            if (chickenList.size() != 0) {
                                binding.tvNumberofimages.setText("Number of images: " + chickenList.size());
                                // set time from_time to_time from image has lowest time and highest time and convert unixtime to string H:mm:ss
                                String lowestTime = String.valueOf(chickenList.get(0).getTime());
                                String highestTime = String.valueOf(chickenList.get(chickenList.size() - 1).getTime());
                                lowestTime = convertUnixTime(Long.parseLong(lowestTime));
                                highestTime = convertUnixTime(Long.parseLong(highestTime));
                                binding.tvTime.setText("Time: " + lowestTime + " - " + highestTime);
                                // save number of chickens
                                binding.tvNumberofchickens.setText("Number of chickens: " + totalchickens);
                                // getvalue of number of chickens from firebase
                                readDataFirebase();
                                Log.d("HEHEHE", "number: " + number + " totalchickens: " + totalchickens);

                                // save lowest and highest temperature
                                float minTemp = 100;
                                float maxtemp = 0;
                                for (ChickenBreed chicken: chickenList){
                                    if ((chicken.getHctemp() < minTemp) && (chicken.getHctemp() != -1)){
                                        minTemp = chicken.getHctemp();
                                    }
                                    if (chicken.getHctemp() > maxtemp){
                                        maxtemp = chicken.getHctemp();
                                    }
                                }
                                binding.tvLowtemp.setText("Lowest temperature: " + minTemp);
                                binding.tvHightemp.setText("Highest temperature: " + maxtemp);
                            } else {
                                binding.tvNumberofimages.setText("Number of images: 0");
                                binding.tvTime.setText("Time: --");
                                binding.tvNumberofchickens.setText("Number of chickens: 0");
                                readDataFirebase();
                                binding.tvLowtemp.setText("Lowest temperature: --");
                                binding.tvHightemp.setText("Highest temperature: --");
                                // disable button view all images
                                binding.btnViewallimages.setEnabled(false);
                                binding.btnViewallimages.setText("No images available");
                            }
                            Log.d("HEHEHE", "33chickenList: " + chickenList.size() + " chickenList2: " + chickenList2.size());
                            Log.d("HEHEHE", avgfood + " " + avgwater + " " + totalfood + " " + totalwater);
                            if (totalfood != 0 || totalwater != 0 || avgfood != 0 || avgwater != 0) {
                                binding.tvFood.setText("Total amount of food: " + totalfood + "g");
                                binding.tvWater.setText("Total amount of water: " + totalwater + "ml");
                                binding.tvAveragefood.setText("Average amount of food: " + totalfood/totalchickens + "g");
                                binding.tvAveragewater.setText("Average amount of water: " + totalwater/totalchickens + "ml");
                                // if today cannot check
                                if (intent.getStringExtra("day").equals(String.valueOf(Calendar.getInstance().get(Calendar.DAY_OF_MONTH))) && intent.getStringExtra("month").equals(String.valueOf(Calendar.getInstance().get(Calendar.MONTH) + 1)) && intent.getStringExtra("year").equals(String.valueOf(Calendar.getInstance().get(Calendar.YEAR)))) {
                                    binding.tvNotifyfood.setText("Compared to average food: --");
                                    binding.tvNotifyfood.setTextColor(Color.parseColor("#000000"));
                                } else if (totalfood > avgfood) {
                                    binding.tvNotifyfood.setText("Compared to average food: normal");
                                    binding.tvNotifyfood.setTextColor(Color.parseColor("#7ABA78"));
                                } else if (totalfood < avgfood && totalfood > avgfood*(totalchickens-1)/totalchickens){
                                    binding.tvNotifyfood.setText("Compared to average food: signs of eating less");
                                    binding.tvNotifyfood.setTextColor(Color.parseColor("#FA7070"));
                                } else {
                                    binding.tvNotifyfood.setText("Compared to average food: signs of not eating");
                                    binding.tvNotifyfood.setTextColor(Color.parseColor("#FA7070"));
                                }
                                if (intent.getStringExtra("day").equals(String.valueOf(Calendar.getInstance().get(Calendar.DAY_OF_MONTH))) && intent.getStringExtra("month").equals(String.valueOf(Calendar.getInstance().get(Calendar.MONTH) + 1)) && intent.getStringExtra("year").equals(String.valueOf(Calendar.getInstance().get(Calendar.YEAR)))) {
                                    binding.tvNotifywater.setText("Compared to average water: --");
                                    binding.tvNotifywater.setTextColor(Color.parseColor("#000000"));
                                } else if (totalwater > avgwater) {
                                    binding.tvNotifywater.setText("Compared to average water: normal");
                                    binding.tvNotifywater.setTextColor(Color.parseColor("#7ABA78"));
                                } else if (totalwater < avgwater && totalwater > avgwater*(totalchickens-1)/totalchickens){
                                    binding.tvNotifywater.setText("Compared to average food: signs of drinking less");
                                    binding.tvNotifywater.setTextColor(Color.parseColor("#FA7070"));
                                } else {
                                    binding.tvNotifywater.setText("Compared to average food: signs of not drinking");
                                    binding.tvNotifywater.setTextColor(Color.parseColor("#FA7070"));
                                }
                            }
                            else {
                                binding.tvFood.setText("Total amount of food: --");
                                binding.tvWater.setText("Total amount of water: --");
                                binding.tvAveragefood.setText("Average amount of food: --");
                                binding.tvAveragewater.setText("Average amount of water: --");
                                binding.tvNotifyfood.setText("Compared to average food: --");
                                binding.tvNotifywater.setText("Compared to average water: --");
                            }
                            AsyncTask.execute(new Runnable() {
                                @Override
                                public void run() {

                                }
                            });
                        }
                        @Override
                        public void onError(@NonNull Throwable e) {
                            Log.d("HEHEHE","Fail"+e.getMessage());
                        }
                    });
        }
        String day = intent.getStringExtra("day");
        String month = intent.getStringExtra("month");
        String year = intent.getStringExtra("year");
        if (isToday(Integer.parseInt(day), Integer.parseInt(month), Integer.parseInt(year))) {
            binding.btnChangenumber.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // show dialog to change number of chickens
                    AlertDialog.Builder builder = new AlertDialog.Builder(ChickenDashboardDayActivity.this);
                    LayoutInflater inflater = ChickenDashboardDayActivity.this.getLayoutInflater();
                    View dialogView = inflater.inflate(R.layout.change_number, null);
                    builder.setView(dialogView);

                    AlertDialog dialog = builder.create();
                    dialog.setCancelable(true);
                    dialog.show();
                    Button change = dialogView.findViewById(R.id.btn_change);
                    change.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            TextView number = dialogView.findViewById(R.id.et_number);
                            myRef.child("NumberofChickens").setValue(number.getText().toString());
                            dialog.dismiss();
                        }
                    });
                }
            });
        } else {
            binding.btnChangenumber.setEnabled(false);
            binding.btnChangenumber.setText("To change number of chickens, please go to today's dashboard");
        }
        binding.btnViewallimages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ChickenDashboardDayActivity.this, ChickenDayActivity.class);
                i.putExtra("day", intent.getStringExtra("day"));
                i.putExtra("month", intent.getStringExtra("month"));
                i.putExtra("year", intent.getStringExtra("year"));
                if (isInternetAvailable()) {
                    myRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            if (snapshot.hasChild("NumberofChickens")) {
                                String value = snapshot.child("NumberofChickens").getValue().toString();
                                try {
                                    number = Integer.parseInt(value);
                                } catch (NumberFormatException e) {
                                    Log.e("ChickenDashboardDayActivity", "NumberofChickens value is not a valid integer: " + value);
                                }
                            } else {
                                myRef.child("NumberofChickens").setValue("0");
                                number = 0;
                            }
                            i.putExtra("number", String.valueOf(number));
                            Log.d("HEHEHE", "number: " + number); // This will log the updated value of number
                            startActivity(i);
                            //Log.d("HEHEHE", "number: " + number); // This will log the updated value of number
                        }

                        @Override
                        public void onCancelled(DatabaseError error) {
                            Log.d("HEHEHE", "Failed to read value.", error.toException());
                        }
                    });
                } else {
                    i.putExtra("number", "-1");
                    startActivity(i);
                }
            }
        });
    }

    public boolean isToday(int day, int month, int year) {
        LocalDate today = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            today = LocalDate.now();
        }
        LocalDate givenDate = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            givenDate = LocalDate.of(year, month, day);
        }
        return today.equals(givenDate);
    }

    public boolean isInternetAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public String convertUnixTime(long unixTime) {
        Date date = new Date(unixTime * 1000L);
        SimpleDateFormat sdf = new SimpleDateFormat("H:mm:ss");
        return sdf.format(date);
    }

    public void readDataFirebase() {
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.hasChild("NumberofChickens")) {
                    String value = snapshot.child("NumberofChickens").getValue().toString();
                    try {
                        number = Integer.parseInt(value);
                    } catch (NumberFormatException e) {
                        Log.e("ChickenDashboardDayActivity", "NumberofChickens value is not a valid integer: " + value);
                    }
                } else {
                    myRef.child("NumberofChickens").setValue("0");
                    number = 0;
                }
                binding.tvNotifyownerchickens.setText("Number of owner's chickens: " + number);
                if (number >= totalchickens) {
                    binding.tvMissingchickens.setText("Number of missing chickens: " + (number - totalchickens));
                } else {
                    binding.tvMissingchickens.setText("Number of excess chickens: " + (totalchickens - number));
                }
                //Log.d("HEHEHE", "number: " + number); // This will log the updated value of number
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.d("HEHEHE", "Failed to read value.", error.toException());
            }
        });
    }

    public void readOwnerChickensFirebase() {
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.hasChild("NumberofChickens")) {
                    String value = snapshot.child("NumberofChickens").getValue().toString();
                    try {
                        number = Integer.parseInt(value);
                    } catch (NumberFormatException e) {
                        Log.e("ChickenDashboardDayActivity", "NumberofChickens value is not a valid integer: " + value);
                    }
                } else {
                    myRef.child("NumberofChickens").setValue("0");
                    number = 0;
                }
                binding.tvNotifyownerchickens.setText("Number of owner's chickens: " + number);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.d("HEHEHE", "Failed to read value.", error.toException());
            }
        });
    }
}