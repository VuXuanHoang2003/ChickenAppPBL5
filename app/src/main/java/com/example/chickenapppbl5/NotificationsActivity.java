package com.example.chickenapppbl5;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.chickenapppbl5.databinding.ActivityNotificationsBinding;
import com.example.chickenapppbl5.model.AppDatabase;
import com.example.chickenapppbl5.model.ChickenBreed;
import com.example.chickenapppbl5.model.ChickenDAO;
import com.example.chickenapppbl5.viewmodel.ChickenAdapter;
import com.example.chickenapppbl5.viewmodel.ChickenApiService;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class NotificationsActivity extends AppCompatActivity implements ChickenAdapter.OnChickenListener {

    ActivityNotificationsBinding binding;
    private ChickenApiService apiService;
    private ChickenAdapter chickensAdapter;
    private List<ChickenBreed> chickenList;
    private ChickenDAO ChickenDAO;
    private BottomNavigationView  bottomNavigationView;
    private AppDatabase appDatabase;

    private int month;
    //Map<String, Integer> monthToNumber = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityNotificationsBinding.inflate(getLayoutInflater());

        // Set the content view to the inflated layout
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;

        });
        bottomNavigationView = findViewById(R.id.bottom_navigator);
        bottomNavigationView.setSelectedItemId(R.id.notifications);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.calendar:
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.chart:
                        startActivity(new Intent(getApplicationContext(),ChartActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.notifications:
                        return true;
                }
                return false;
            }
        });
        binding.rvNotify.setLayoutManager(new GridLayoutManager(this, 1));
        chickenList = new ArrayList<>();
        chickensAdapter = new ChickenAdapter(chickenList,this);
        binding.rvNotify.setAdapter(chickensAdapter);
        apiService = new ChickenApiService();
        //check if internet is unavailable
        if (!isInternetAvailable()){
            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    appDatabase = AppDatabase.getInstance(getApplicationContext());
                    ChickenDAO = appDatabase.chickenDAO();
                    List<ChickenBreed> tempChickenList = new ArrayList<>();
                    tempChickenList = ChickenDAO.getHighTemp(34);
                    Log.d("HEHEHE", "tempChickenList: " + tempChickenList.size());
                    //chickenList.addAll(tempChickenList);
                    chickensAdapter = new ChickenAdapter(tempChickenList, NotificationsActivity.this);
                    //binding.rvNotify.setAdapter(chickensAdapter);
                    chickensAdapter.notifyDataSetChanged();
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
        else{
            apiService.getHighTemp(34)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSingleObserver<List<ChickenBreed>>() {
                        @Override
                        public void onSuccess(@NonNull List<ChickenBreed> chickenBreeds) {
                            Log.d("HEHEHE","success");
                            for(ChickenBreed chicken: chickenBreeds){
                                ChickenBreed i = new ChickenBreed(chicken.getId(),chicken.getUuid(), chicken.getUrl(), chicken.getPredict(), chicken.getInfared(), chicken.getLabels(), chicken.getChicken(), chicken.getNon_chicken(), chicken.getTime(), chicken.getHctemp(), chicken.getOther());
                                    if (Integer.parseInt(i.getChicken()) > 0) {
                                        chickenList.add(i);
                                    }
                                    //ChickenDAO.insert(chicken);
                                //Log.d("HEHEHE",i.getUuid());
                                chickensAdapter.notifyDataSetChanged();
                            }
                            AsyncTask.execute(new Runnable() {
                                @Override
                                public void run() {
                                    appDatabase = AppDatabase.getInstance(getApplicationContext());
                                    ChickenDAO = appDatabase.chickenDAO();
                                    for(ChickenBreed chicken:chickenList){
                                        if (Integer.parseInt(chicken.getChicken()) > 0) {
                                            ChickenDAO.insertChicken(chicken);
                                        }
                                        int count = ChickenDAO.countByUuid(chicken.getUuid());
                                        if (count <= 0) {
                                            appDatabase.insertChicken(chicken);
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
        }


    }

    public boolean isInternetAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    @Override
    public void onChickenClick(int position) {

    }
}