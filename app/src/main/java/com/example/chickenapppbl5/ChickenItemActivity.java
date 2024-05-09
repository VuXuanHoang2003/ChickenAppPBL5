package com.example.chickenapppbl5;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
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
        //Glide.with(this).load(intent.getExtras().getString("url")).into(binding.ivChickenurl);
        Glide.with(this).load(intent.getExtras().getString("predict")).into(binding.ivChickenpredict);
        Glide.with(this).load(intent.getExtras().getString("infared")).into(binding.ivChickeninfared);
//        binding.tvChickenid.setText(intent.getExtras().getString("id"));
//        binding.tvChickenuuid.setText(intent.getExtras().getString("uuid"));
//        binding.tvChickenlabel.setText(intent.getExtras().getString("labels"));
        binding.tvChickenchicken.setText(intent.getExtras().getString("chicken"));
        binding.tvTemp.setText(intent.getExtras().getString("hctemp"));
        long unixtime = Long.parseLong(intent.getExtras().getString("time"));
        String date = new java.text.SimpleDateFormat("d/M/yyyy H:mm:ss").format(new java.util.Date(unixtime*1000L));
        binding.tvTime.setText(date);
//        binding.tvChickenother.setText(intent.getExtras().getString("other"));
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}