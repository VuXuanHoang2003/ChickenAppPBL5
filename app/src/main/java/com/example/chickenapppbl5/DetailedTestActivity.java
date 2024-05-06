package com.example.customlistview;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.chickenapppbl5.R;
import com.example.chickenapppbl5.databinding.ActivityDetailedTestBinding;


public class DetailedTestActivity extends AppCompatActivity {

    ActivityDetailedTestBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailedTestBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = this.getIntent();
        if (intent != null){
            String name = intent.getStringExtra("name");
            String time = intent.getStringExtra("time");
            int ingredients = intent.getIntExtra("ingredients", R.string.maggiIngredients);
            int desc = intent.getIntExtra("desc", R.string.maggieDesc);
            int image = intent.getIntExtra("image", R.drawable.baseline_favorite_24);

            binding.detailName.setText(name);
//            binding.detailTime.setText(time);
//            binding.detailDesc.setText(desc);
//            binding.detailIngredients.setText(ingredients);
            binding.detailImage.setImageResource(image);
        }
    }
}