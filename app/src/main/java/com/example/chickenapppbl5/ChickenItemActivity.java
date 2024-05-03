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
        Glide.with(this).load(intent.getExtras().getString("url")).into(binding.ivChickenurl);
        Glide.with(this).load(intent.getExtras().getString("predict")).into(binding.ivChickenpredict);
        binding.tvChickenid.setText("ID:" + intent.getExtras().getString("id"));
        binding.tvChickenuuid.setText("UUID:" + intent.getExtras().getString("uuid"));
        binding.tvChickenlabel.setText("Label:" + intent.getExtras().getString("labels"));
        binding.tvChickenchicken.setText("Chicken:" + intent.getExtras().getString("chicken"));
        binding.tvChickensickchicken.setText("Sick Chicken:" + intent.getExtras().getString("sickchicken"));
        binding.tvChickenother.setText("Other:" + intent.getExtras().getString("other"));
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}