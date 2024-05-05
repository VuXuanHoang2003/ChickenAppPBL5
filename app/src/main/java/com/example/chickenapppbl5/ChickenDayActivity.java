package com.example.chickenapppbl5;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.chickenapppbl5.databinding.ActivityChickenDayBinding;
import com.example.chickenapppbl5.databinding.ActivityMainBinding;
import com.example.chickenapppbl5.model.AppDatabase;
import com.example.chickenapppbl5.model.ChickenBreed;
import com.example.chickenapppbl5.model.ChickenDAO;
import com.example.chickenapppbl5.viewmodel.ChickenAdapter;
import com.example.chickenapppbl5.viewmodel.ChickenApiService;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ChickenDayActivity extends AppCompatActivity implements ChickenAdapter.OnChickenListener {

    ActivityChickenDayBinding binding;
    private ChickenApiService apiService;
    private ChickenAdapter chickensAdapter;
    private List<ChickenBreed> chickenList;
    private ChickenDAO ChickenDAO;
    private AppDatabase appDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityChickenDayBinding.inflate(getLayoutInflater());

        // Set the content view to the inflated layout
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;

        });
        Intent intent = getIntent();
        binding.dayTitle.setText("Chicken Day "+ intent.getStringExtra("day") + "/" + intent.getStringExtra("month") );
        binding.rvChickenDayApp.setLayoutManager(new GridLayoutManager(this,2));
        chickenList=new ArrayList<>();
        chickensAdapter=new ChickenAdapter(chickenList,this);
        binding.rvChickenDayApp.setAdapter(chickensAdapter);
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

    }
    @Override
    public void onChickenClick(int position) {

    }
}