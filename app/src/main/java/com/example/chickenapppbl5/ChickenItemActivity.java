package com.example.chickenapppbl5;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.chickenapppbl5.databinding.ActivityChickenItemBinding;

public class ChickenItemActivity extends AppCompatActivity {
    private ActivityChickenItemBinding binding;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChickenItemBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent = getIntent();
        Glide.with(this).load(intent.getExtras().getString("predict")).diskCacheStrategy(DiskCacheStrategy.ALL).into(binding.ivChickenpredict);
        Glide.with(this).load(intent.getExtras().getString("infared")).diskCacheStrategy(DiskCacheStrategy.ALL).into(binding.ivChickeninfared);
        Log.d("HEHEHE", "time" + intent.getExtras().getString("time"));
        binding.tvChickenchicken.setText(intent.getExtras().getString("chicken"));
        binding.tvTemp.setText(intent.getExtras().getString("hctemp"));
        long unixtime = Long.parseLong(String.valueOf(intent.getExtras().getInt("time")));
        String date = new java.text.SimpleDateFormat("d/M/yyyy H:mm:ss").format(new java.util.Date(unixtime*1000L));
        binding.tvTime.setText(date);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}