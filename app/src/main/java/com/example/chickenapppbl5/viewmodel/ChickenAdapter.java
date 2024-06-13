package com.example.chickenapppbl5.viewmodel;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.chickenapppbl5.ChickenItemActivity;
import com.example.chickenapppbl5.R;
import com.example.chickenapppbl5.model.ChickenBreed;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

public class ChickenAdapter extends RecyclerView.Adapter<ChickenAdapter.ViewHolder> {
    public static List<ChickenBreed> chickensList;
    public int value;
    private OnChickenListener onChickenListener;

    private ChickenAdapter chickensAdapter;
    public ChickenAdapter(List<ChickenBreed> chickensList, int value, OnChickenListener onChickenListener){
        this.chickensList=chickensList;
        this.value=value;
        this.onChickenListener=onChickenListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chicken_item, parent, false);

        return new ViewHolder(view,onChickenListener);
    }

    @SuppressLint("ResourceType")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        ChickenBreed chickenBreed = chickensList.get(position);
        // compare chickenBreed.getChicken() to value
        if (value == -1) {

        } else if (Integer.parseInt(chickenBreed.getChicken()) > value) {
            // paint the border of item red
            holder.cardView.setCardBackgroundColor(Color.parseColor("#FA7070"));
        } else if (Integer.parseInt(chickenBreed.getChicken()) == value) {
            // paint the border of item green
            holder.cardView.setCardBackgroundColor(Color.parseColor("#7ABA78"));
        }
        holder.tvChickenName.setText(String.valueOf(chickenBreed.getId()));
        holder.tvChickenTemp.setText(String.valueOf(chickenBreed.getHctemp()));
        long unixtime = Long.parseLong(String.valueOf(chickenBreed.getTime()));
        String date = new java.text.SimpleDateFormat("d/M/yyyy H:mm:ss").format(new java.util.Date(unixtime*1000L));
        holder.tvChickenTime.setText(date);
        //Picasso.get().load(chickenBreed.getUrl()).into(holder.ivChickenImage);
        Glide.with(holder.itemView.getContext()).load(chickenBreed.getUrl()).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.ivChickenImage);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final ChickenBreed ck = chickensList.get(position);
//                Toast.makeText(v.getContext(), "You clicked " + chickenBreed.getUuid(), Toast.LENGTH_SHORT).show();
                Intent i = new Intent(v.getContext(), ChickenItemActivity.class);
                i.putExtra("id", Long.toString(ck.getId()));
                i.putExtra("uuid", ck.getUuid());
                i.putExtra("url", ck.getUrl());
                i.putExtra("predict", ck.getPredict());
                i.putExtra("infared", ck.getInfared());
                i.putExtra("labels", ck.getLabels());
                i.putExtra("chicken", ck.getChicken());
                i.putExtra("time", ck.getTime());
                i.putExtra("hctemp", String.valueOf(ck.getHctemp()));
                i.putExtra("other", ck.getOther());
                Log.i("hello", ck.getUuid());
//                Toast.makeText(
//                        v.getContext(),
//                        "You clicked " + ck.getUuid(),
//                        Toast.LENGTH_SHORT
//                );
                v.getContext().startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {

        if (chickensList != null)
            return chickensList.size();
        else
            return 0;
    }

    public void onMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(chickensList, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(chickensList, i, i - 1);
            }
        }
        chickensAdapter.notifyItemMoved(fromPosition, toPosition);
    }

    public void swipe(int position, int direction) {
        chickensList.remove(position);
        chickensAdapter.notifyItemRemoved(position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public CardView cardView;
        private ImageView ivChickenImage;
        private TextView tvChickenName;
        private TextView tvChickenTemp;

        private TextView tvChickenTime;

        OnChickenListener onChickenListener;
        public ViewHolder(View view,OnChickenListener onChickenListener) {
            super(view);
            // Define click listener for the ViewHolder's View
            cardView = view.findViewById(R.id.cardView);
            ivChickenImage = view.findViewById(R.id.iv_chickenImage);
            tvChickenName = view.findViewById(R.id.tv_chickenName);
            tvChickenTemp = view.findViewById(R.id.tv_chickenTemp);
            tvChickenTime = view.findViewById(R.id.tv_chickenTime);
            this.onChickenListener=onChickenListener;
            view.setOnClickListener(this);
            // textView = (TextView) view.findViewById(R.id.textView);
        }

        @Override
        public void onClick(View view) {
            onChickenListener.onChickenClick(getAdapterPosition());

        }


//        public TextView getTextView() {
//            return textView;
//        }
    }
    public interface OnChickenListener{
        void onChickenClick(int position);
    }
}

