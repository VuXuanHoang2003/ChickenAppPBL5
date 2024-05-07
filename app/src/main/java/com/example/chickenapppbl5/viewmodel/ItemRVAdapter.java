package com.example.chickenapppbl5.viewmodel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chickenapppbl5.R;
import com.example.chickenapppbl5.model.ChickenBreed;
import com.example.chickenapppbl5.model.Day;

import java.util.List;

public class ItemRVAdapter extends RecyclerView.Adapter<ItemRVAdapter.ItemViewHolder>{
    private Context mContext;
    private List<Day> mListDay;

    public ItemRVAdapter(Context mContext) {
        this.mContext = mContext;
    }
    public void SetData(List<Day> list){
        this.mListDay = list;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_day,parent,false);

        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Day day = mListDay.get(position);
        if (day == null){
            return;
        }
        holder.imgDay.setImageResource(day.getId());
        holder.tvName.setText(day.getName());
    }

    @Override
    public int getItemCount() {
        if(mListDay != null){
            return mListDay.size();
        }
        return 0;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder{
        private ImageView imgDay;
        private TextView tvName;
        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            imgDay = itemView.findViewById(R.id.img_1);
            tvName = itemView.findViewById(R.id.tv_name);
        }

    }
}
