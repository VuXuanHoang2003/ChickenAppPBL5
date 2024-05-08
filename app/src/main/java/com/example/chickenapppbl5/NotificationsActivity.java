package com.example.chickenapppbl5;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.bumptech.glide.Glide;
import com.example.chickenapppbl5.databinding.ActivityCalendarBinding;
import com.example.chickenapppbl5.databinding.ActivityNotificationsBinding;
import com.example.chickenapppbl5.model.AppDatabase;
import com.example.chickenapppbl5.model.ChickenBreed;
import com.example.chickenapppbl5.model.ChickenDAO;
import com.example.chickenapppbl5.model.NotifyItem;
import com.example.chickenapppbl5.model.Post;
import com.example.chickenapppbl5.viewmodel.ChickenApiService;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class NotificationsActivity extends AppCompatActivity {

    ActivityNotificationsBinding binding;
    BottomNavigationView bottomNavigationView;
    private ChickenApiService apiService;

    private FirebaseAuth mAuth;
    private AppDatabase appDatabase;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private List<ChickenBreed> chickenList;
    private ChickenDAO ChickenDAO;
    private RecyclerView rvNotes;
    private RecyclerView rvNotfiy;
    private DatabaseReference myRefRoot;
    private FirebaseFirestore firestore;
    private String uuid;

    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityNotificationsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // Khởi tạo SharedPreferences
        sharedPref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        editor = sharedPref.edit();

// Lấy giá trị uuid từ SharedPreferences
        uuid = sharedPref.getString("uuid", null);

// Kiểm tra nếu uuid bị null
        if (uuid == null) {
            uuid = ""; // Gán uuid là xâu rỗng nếu nó bị null
        }
        mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference().child("notications");
        myRefRoot = database.getReference();

        firestore = FirebaseFirestore.getInstance();
        myRefRoot.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Đây là nơi bạn xử lý dữ liệu khi nó thay đổi. dataSnapshot chứa dữ liệu mới.
                // Ví dụ: đọc giá trị String
                uuid = dataSnapshot.child("uuid").getValue(String.class);
                //Toast.makeText(NotificationsActivity.this, "uuid: ", Toast.LENGTH_SHORT).show();
                if (!dataSnapshot.exists()) {
                    myRefRoot.child("uuid").setValue("");
                }
                else {
                    uuid = dataSnapshot.child("uuid").getValue(String.class);
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Đây là nơi bạn xử lý lỗi khi không thể lấy dữ liệu.
            }
        });

        //Toast.makeText(this, "uuid: "+ uuid, Toast.LENGTH_SHORT).show();
        rvNotfiy = findViewById(R.id.rv_notify);
        rvNotfiy.setLayoutManager(new LinearLayoutManager(this));
        Timer timer = new Timer();
        timer.schedule(new GetNotifyTask(), 0, 5000);

        bottomNavigationView = findViewById(R.id.bottom_navigator);
        bottomNavigationView.setSelectedItemId(R.id.notifications);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.chart:
                        startActivity(new Intent(getApplicationContext(),ChartActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.calendar:
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.settings:
                        startActivity(new Intent(getApplicationContext(),SettingsActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.notifications:
                        return true;
                }
                return false;
            }
        });
    }

    public void addNotify(String title,String uurl, String img,String time, String timenow) {
        String id  = myRef.push().getKey();
        myRef.child(id).setValue(new NotifyItem(id, title, uurl, img, time, timenow)).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d("DEBUG", "Post added successfully");
                } else {
                    Log.d("DEBUG", "Post added failed");
                }
            }
        });
    }
        @Override
        public void onStart() {
            super.onStart();

            FirebaseRecyclerOptions<NotifyItem> options =
                    new FirebaseRecyclerOptions.Builder<NotifyItem>()
                            .setQuery(myRef, NotifyItem.class)
                            .build();
            FirebaseRecyclerAdapter adapter = new FirebaseRecyclerAdapter<NotifyItem, NotifyItemHolder>(options) {
                @Override
                public NotifyItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                    // Create a new instance of the ViewHolder, in this case we are using a custom
                    // layout called R.layout.message for each item
                    View view = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.notify_items, parent, false);

                    return new NotifyItemHolder(view);
                }

                @Override
                protected void onBindViewHolder(NotifyItemHolder holder, int position, NotifyItem model) {
                    // Bind the Post object to the PostHolder
                    // ...
                    holder.tvTitle.setText(model.getTitle());
                    holder.tvUuid.setText(model.getUuid());
                    holder.tvTime.setText("Image of Time : " + model.getTime());
                    holder.tvTimeNow.setText(model.getTimenow());
                    Glide.with(NotificationsActivity.this).load(model.getImg()).into(holder.ivImg);

                }
            };
            rvNotfiy.setAdapter(adapter);
            //rvNotes.setAdapter(adapter);
            adapter.startListening();
        }

        public static class NotifyItemHolder extends RecyclerView.ViewHolder {
            public TextView tvTitle;
            public TextView tvTime;
            public TextView tvUuid;

            public TextView tvTimeNow;
            public ImageView ivImg;
            public LinearLayout llNotify;
            public NotifyItemHolder(View view) {
                super(view);
                tvTitle = view.findViewById(R.id.tv_title);
                tvTitle.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                tvTitle.setSelected(true);
                tvUuid = view.findViewById(R.id.tv_uuid);
                tvTime = view.findViewById(R.id.tv_time);
                ivImg = view.findViewById(R.id.iv_img);
                llNotify = view.findViewById(R.id.layout_notify);
            }
        }

        private String getRandomColor() {
            ArrayList<String> colors = new ArrayList<>();
            colors.add("#35ad68");
            colors.add("#c27ba0");
            colors.add("#baa9aa");
            colors.add("#bfbd97");
            colors.add("#746cc0");

            Random random = new Random();
            return colors.get(random.nextInt(colors.size()));
        }

    public class GetNotifyTask extends TimerTask {
        @Override
        public void run() {
            apiService = new ChickenApiService();
            chickenList = new ArrayList<>();
            apiService.getChickens()
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSingleObserver<List<ChickenBreed>>() {
                        @Override
                        public void onSuccess(@NonNull List<ChickenBreed> chickenBreeds) {
                            Log.d("DEBUG","success");
                            for(ChickenBreed chicken: chickenBreeds){
                                ChickenBreed i = new ChickenBreed(chicken.getId(),chicken.getUuid(), chicken.getUrl(), chicken.getPredict(), chicken.getInfared(), chicken.getLabels(), chicken.getChicken(), chicken.getNon_chicken(), chicken.getTime(), chicken.getHctemp(), chicken.getOther());
                                chickenList.add(i);
                                Log.d("DEBUG",i.getUuid());
                            }

                            AsyncTask.execute(new Runnable() {
                                @Override
                                public void run() {

                                    appDatabase = AppDatabase.getInstance(getApplicationContext());
                                    ChickenDAO = appDatabase.chickenDAO();
                                    for(ChickenBreed chicken:chickenList){
                                        ChickenBreed i = new ChickenBreed(chicken.getId(),chicken.getUuid(), chicken.getUrl(), chicken.getPredict(), chicken.getInfared(), chicken.getLabels(), chicken.getChicken(), chicken.getNon_chicken(), chicken.getTime(), chicken.getHctemp(), chicken.getOther());
                                        if (uuid != null) {
                                            if (Float.parseFloat(i.getHctemp()) > 28.0 && (!uuid.contains(i.getUuid()))) {
                                                Long unixTime = Long.parseLong(i.getTime());
                                                Date date = new Date(unixTime * 1000L);
//                                                Calendar cal = Calendar.getInstance();
//                                                cal.setTime(date);
//                                                int hour = cal.get(Calendar.HOUR_OF_DAY);
//                                                int minute = cal.get(Calendar.MINUTE);
//                                                int day = cal.get(Calendar.DAY_OF_MONTH);
//                                                int month = cal.get(Calendar.MONTH) + 1;
//                                                int year = cal.get(Calendar.YEAR);
//                                                String time = String.format("%02d:%02d", hour, minute);
                                                SimpleDateFormat sdf = new SimpleDateFormat("H:mm:ss d/M/yyyy");
                                                String formattedDate = sdf.format(date);
                                                Date currentTime = new Date(); // Lấy thời gian hiện tại

                                                // Định dạng chuỗi theo định dạng mong muốn
                                                SimpleDateFormat sdf1 = new SimpleDateFormat("d/M/yyyy H:mm:ss");
                                                String formattedDate1 = sdf1.format(currentTime);
                                                addNotify("High temperature problem!",i.getUuid(), i.getPredict(), formattedDate, formattedDate1);
                                                ChickenDAO.insert(i);
                                                uuid = uuid + i.getUuid();
                                                Log.d("DEBUG", "uuid: "+uuid);
                                                uuid = uuid + i.getUuid();
                                                Log.d("DEBUG", "uuid: "+uuid);

// Lưu uuid vào SharedPreferences
                                                editor.putString("uuid", uuid);
                                                editor.apply();
                                            }
                                        }
                                    }
                                    myRefRoot.child("uuid").setValue(uuid);
                                }
                            });
                            //myRefRoot.child("uuid").setValue(uuid);
                        }
                        @Override
                        public void onError(@NonNull Throwable e) {
                            Log.d("DEBUG","Fail"+e.getMessage());
                        }
                    });
        }
    }
    }