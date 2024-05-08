package com.example.chickenapppbl5.viewmodel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.chickenapppbl5.R;
import com.example.chickenapppbl5.model.ListData;

public class ListAdapter extends ArrayAdapter<String> {
    public ListAdapter(@NonNull Context context, ArrayList<String> dataArrayList) {
        super(context, R.layout.list_calendar, dataArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {
        String listData = getItem(position);

        if (view == null){
            view = LayoutInflater.from(getContext()).inflate(R.layout.list_calendar, parent, false);
        }

        ImageView listImage = view.findViewById(R.id.listImage);
        TextView listName = view.findViewById(R.id.listName);
        //TextView listTime = view.findViewById(R.id.listTime);

        listImage.setImageResource(R.drawable.baseline_calendar_month_24);
        listName.setText(listData);

        return view;
    }
}