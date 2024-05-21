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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;

import com.example.chickenapppbl5.databinding.ActivityChickenDayBinding;
import com.example.chickenapppbl5.databinding.ActivityMainBinding;
import com.example.chickenapppbl5.model.AppDatabase;
import com.example.chickenapppbl5.model.ChickenBreed;
import com.example.chickenapppbl5.model.ChickenDAO;
import com.example.chickenapppbl5.viewmodel.ChickenAdapter;
import com.example.chickenapppbl5.viewmodel.ChickenApiService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        private int month;
    //Map<String, Integer> monthToNumber = new HashMap<>();

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
        //Toast.makeText(this, "Day: " + intent.getStringExtra("day") + " Month: " + intent.getStringExtra("month"), Toast.LENGTH_SHORT).show();
        binding.dayTitle.setText("Chicken Day "+ intent.getStringExtra("day") + "/" + intent.getStringExtra("month") );
        binding.rvChickenDayApp.setLayoutManager(new GridLayoutManager(this,1));
        chickenList = new ArrayList<>();
        chickensAdapter = new ChickenAdapter(chickenList,this);
        binding.rvChickenDayApp.setAdapter(chickensAdapter);
        apiService = new ChickenApiService();
        apiService.getChickens()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<List<ChickenBreed>>() {
                    @Override
                    public void onSuccess(@NonNull List<ChickenBreed> chickenBreeds) {
                        Log.d("DEBUG","success");
                        for(ChickenBreed chicken: chickenBreeds){
                            ChickenBreed i = new ChickenBreed(chicken.getId(),chicken.getUuid(), chicken.getUrl(), chicken.getPredict(), chicken.getInfared(), chicken.getLabels(), chicken.getChicken(), chicken.getNon_chicken(), chicken.getTime(), chicken.getHctemp(), chicken.getOther());
                            //chickenList.add(i);
                            long unixTime = Long.parseLong(chicken.getTime());
                            Date date = new Date(unixTime * 1000L);
                            Calendar cal = Calendar.getInstance();
                            cal.setTime(date);
                            if (cal.get(Calendar.DAY_OF_MONTH) == Integer.parseInt(intent.getStringExtra("day")) && (cal.get(Calendar.MONTH) + 1) == Integer.parseInt(intent.getStringExtra("month"))){
                                chickenList.add(i);
                                //ChickenDAO.insert(chicken);
                            }
                            Log.d("DEBUG",i.getUuid());
                            chickensAdapter.notifyDataSetChanged();
                        }
                        AsyncTask.execute(new Runnable() {
                            @Override
                            public void run() {
                                appDatabase = AppDatabase.getInstance(getApplicationContext());
                                ChickenDAO = appDatabase.chickenDAO();
                                for(ChickenBreed chicken:chickenList){
//                                    long unixTime = Long.parseLong(chicken.getTime());
//                                    Date date = new Date(unixTime * 1000L);
//                                    Calendar cal = Calendar.getInstance();
//                                    cal.setTime(date);
//                                    if (cal.get(Calendar.DAY_OF_MONTH) == Integer.parseInt(intent.getStringExtra("day")) && (cal.get(Calendar.MONTH) + 1) == Integer.parseInt(intent.getStringExtra("month"))){
//                                        ChickenDAO.insert(chicken);
//                                    }
                                    ChickenDAO.insert(chicken);
                                }
                            }
                        });
                    }
                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d("DEBUG","Fail"+e.getMessage());
                    }
                });
//        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(new ItemTouchListener() {
//            @Override
//            public void onMove(int oldPosition, int newPosition) {
//                chickensAdapter.onMove(oldPosition, newPosition);
//            }
//
//            @Override
//            public void swipe(int position, int direction) {
//                chickensAdapter.swipe(position, direction);
//                // delete from api using uuid
//                ChickenBreed chicken = chickenList.get(position);
//                apiService.deleteChicken(chicken.getUuid());
//                chickenList.remove(position);
//                chickensAdapter.notifyItemRemoved(position);
//            }
//        });
//        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
//        itemTouchHelper.attachToRecyclerView(binding.rvChickenDayApp);

    }
    @Override
    public void onChickenClick(int position) {

    }
}