package com.example.chickenapppbl5;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.chickenapppbl5.databinding.ActivityChickenInfoBinding;
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

public class Chicken_Info extends AppCompatActivity implements ChickenAdapter.OnChickenListener {
    private ActivityChickenInfoBinding binding;
    private ChickenApiService apiService;
    private ChickenAdapter chickensAdapter;
    private List<ChickenBreed> chickenList;
    private com.example.chickenapppbl5.model.ChickenDAO ChickenDAO;
    private AppDatabase appDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        binding = ActivityChickenInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        chickenList = new ArrayList<>();
        chickensAdapter = new ChickenAdapter(chickenList, (ChickenAdapter.OnChickenListener) this);
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
                            ChickenBreed i= new ChickenBreed(chicken.getId(),chicken.getUuid(),
                                    chicken.getUrl(), chicken.getPredict(),chicken.getInfared()
                                    ,chicken.getTime(), chicken.getLabels(), chicken.getChicken(),chicken.getNon_chicken());
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
                                    ChickenBreed i = new ChickenBreed(chicken.getId(),chicken.getUuid(), chicken.getUrl(), chicken.getPredict()
                                            ,chicken.getInfared(),chicken.getTime(), chicken.getLabels(), chicken.getChicken(),chicken.getNon_chicken());
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