package com.example.chickenapppbl5;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.example.chickenapppbl5.databinding.ActivityTestBinding;
import com.example.chickenapppbl5.model.ListData;
import com.example.chickenapppbl5.viewmodel.ListAdapter;

import java.util.ArrayList;

public class TestActivity extends AppCompatActivity {

    ActivityTestBinding binding;
    ListAdapter listAdapter;
    ArrayList<ListData> dataArrayList = new ArrayList<>();
    ListData listData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTestBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        int[] imageList = {R.drawable.baseline_favorite_24, R.drawable.baseline_favorite_24, R.drawable.baseline_favorite_24, R.drawable.baseline_favorite_24, R.drawable.baseline_favorite_24, R.drawable.baseline_favorite_24, R.drawable.baseline_favorite_24};
        int[] ingredientList = {R.string.pastaIngredients, R.string.maggiIngredients,R.string.cakeIngredients,R.string.pancakeIngredients,R.string.pizzaIngredients, R.string.burgerIngredients, R.string.friesIngredients};
        int[] descList = {R.string.pastaDesc, R.string.maggieDesc, R.string.cakeDesc,R.string.pancakeDesc,R.string.pizzaDesc, R.string.burgerDesc, R.string.friesDesc};
        String[] nameList = {"Pasta", "Maggi", "Cake", "Pancake", "Pizza","Burgers", "Fries"};
        String[] timeList = {"30 mins", "2 mins", "45 mins","10 mins", "60 mins", "45 mins", "30 mins"};

        for (int i = 0; i < imageList.length; i++){
            listData = new ListData(nameList[i], timeList[i], imageList[i]);
            dataArrayList.add(listData);
        }
        //listAdapter = new ListAdapter(TestActivity.this, dataArrayList);
        binding.listview.setAdapter(listAdapter);
        binding.listview.setClickable(true);

//        binding.listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Intent intent = new Intent(TestActivity.this, com.example.customlistview.DetailedTestActivity.class);
//                intent.putExtra("name", nameList[i]);
//                intent.putExtra("time", timeList[i]);
//                intent.putExtra("ingredients", ingredientList[i]);
//                intent.putExtra("desc", descList[i]);
//                intent.putExtra("image", imageList[i]);
//                startActivity(intent);
//            }
//        });
        binding.btnOpencalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TestActivity.this, CalendarByMonthActivity.class);
                startActivity(i);
            }
        });
    }
}