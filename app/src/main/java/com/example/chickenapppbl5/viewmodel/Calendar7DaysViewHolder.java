package com.example.chickenapppbl5.viewmodel;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.chickenapppbl5.R;

public class Calendar7DaysViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public final TextView dayOfMonth;
    private final Calendar7DaysAdapter.OnItemListener onItemListener;
    public Calendar7DaysViewHolder(@NonNull View itemView, Calendar7DaysAdapter.OnItemListener onItemListener)
    {
        super(itemView);
        dayOfMonth = itemView.findViewById(R.id.cellDayText);
        this.onItemListener = onItemListener;
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view)
    {
        onItemListener.onItemClick(getAdapterPosition(), (String) dayOfMonth.getText());
    }

}
