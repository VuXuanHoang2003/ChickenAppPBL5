package com.example.chickenapppbl5;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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

import com.example.chickenapppbl5.databinding.ActivityChickenDayBinding;
import com.example.chickenapppbl5.model.AppDatabase;
import com.example.chickenapppbl5.model.ChickenBreed;
import com.example.chickenapppbl5.model.ChickenDAO;
import com.example.chickenapppbl5.viewmodel.ChickenAdapter;
import com.example.chickenapppbl5.viewmodel.ChickenApiService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
        binding.dayTitle.setText("Chicken Day "+ intent.getStringExtra("day") + "/" + intent.getStringExtra("month") + "/" + intent.getStringExtra("year"));
        Toast.makeText(this, "Chicken Day "+ intent.getStringExtra("day") + "/" + intent.getStringExtra("month") + "/" + intent.getStringExtra("year"), Toast.LENGTH_SHORT);
        binding.rvChickenDayApp.setLayoutManager(new GridLayoutManager(this,1));
        chickenList = new ArrayList<>();
        chickensAdapter = new ChickenAdapter(chickenList,this);
        binding.rvChickenDayApp.setAdapter(chickensAdapter);
        apiService = new ChickenApiService();
        // Convert date from intent to unixtime
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
        // check if internet is unavailable
        if (!isInternetAvailable()){
            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    appDatabase = AppDatabase.getInstance(getApplicationContext());
                    ChickenDAO = appDatabase.chickenDAO();
                    List<ChickenBreed> tempChickenList = new ArrayList<>();
                    tempChickenList = ChickenDAO.getChickensTime(from_time, to_time);
                    Log.d("HEHEHE", "tempChickenList: " + tempChickenList.size());
                    //chickenList.addAll(tempChickenList);
                    chickensAdapter = new ChickenAdapter(tempChickenList, ChickenDayActivity.this);
                    binding.rvChickenDayApp.setAdapter(chickensAdapter);
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
        else {
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
                                    //ChickenDAO.insert(chicken);
                                }
                                //Log.d("HEHEHE",i.getUuid());
                                chickensAdapter.notifyDataSetChanged();
                            }
                            AsyncTask.execute(new Runnable() {
                                @Override
                                public void run() {
//                                    appDatabase = AppDatabase.getInstance(getApplicationContext());
//                                    ChickenDAO = appDatabase.chickenDAO();
//                                    for(ChickenBreed chicken:chickenList){
//                                        ChickenDAO.insertChicken(chicken);
//                                        int count = ChickenDAO.countByUuid(chicken.getUuid());
//                                        if (count <= 0) {
//                                            appDatabase.insertChicken(chicken);
//                                        } else {
//                                            // This chicken is a duplicate
//                                        }
//                                    }
                                }
                            });
                        }
                        @Override
                        public void onError(@NonNull Throwable e) {
                            Log.d("HEHEHE","Fail"+e.getMessage());
                        }
                    });
        }

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

    public boolean isInternetAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    @Override
    public void onChickenClick(int position) {

    }
}