package com.example.chickenapppbl5;

import android.content.Intent;
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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chickenapppbl5.databinding.ActivityMainBinding;
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

public class MainActivity extends AppCompatActivity implements ChickenAdapter.OnChickenListener{

    BottomNavigationView bottomNavigationView;

    private ChickenApiService apiService;
    private ChickenAdapter chickensAdapter;
    private List<ChickenBreed> chickenList;
    private ActivityMainBinding binding;
    private ChickenDAO ChickenDAO;
    private AppDatabase appDatabase;
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
        binding.rvChickenApp.setLayoutManager(new GridLayoutManager(this,2));
        chickenList=new ArrayList<>();
        chickensAdapter=new ChickenAdapter(chickenList,this);
        binding.rvChickenApp.setAdapter(chickensAdapter);
        apiService=new ChickenApiService();
        apiService.getChickens()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<List<ChickenBreed>>() {
                    @Override
                    public void onSuccess(@NonNull List<ChickenBreed> chickenBreeds) {
                        Log.d("DEBUG","success");
                        for(ChickenBreed chicken: chickenBreeds){
                            ChickenBreed i= new ChickenBreed(chicken.getId(),chicken.getUuid(), chicken.getUrl(), chicken.getPredict(), chicken.getLabels(), chicken.getChicken(), chicken.getSick_chicken(), chicken.getOther());
                            chickenList.add(i);
                            Log.d("DEBUG",i.getUuid());
                            chickensAdapter.notifyDataSetChanged();
                        }
                        AsyncTask.execute(new Runnable() {
                            @Override
                            public void run() {
                                appDatabase = AppDatabase.getInstance(getApplicationContext());
                                ChickenDAO = appDatabase.contactDAO();
                                for(ChickenBreed chicken:chickenList){
                                    ChickenBreed i = new ChickenBreed(chicken.getId(),chicken.getUuid(), chicken.getUrl(), chicken.getPredict(), chicken.getLabels(), chicken.getChicken(), chicken.getSick_chicken(), chicken.getOther());
                                    ChickenDAO.insert(i);
                                }
                            }
                        });
                    }
                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d("DEBUG","Fail"+e.getMessage());
                    }
                });



        bottomNavigationView = findViewById(R.id.bottom_navigator);
        bottomNavigationView.setSelectedItemId(R.id.image);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.calendar:
                        startActivity(new Intent(getApplicationContext(),CalendarActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.chart:
                        startActivity(new Intent(getApplicationContext(),ChartActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.image:
                        return true;
                    case R.id.settings:
                        startActivity(new Intent(getApplicationContext(),SettingsActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });

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