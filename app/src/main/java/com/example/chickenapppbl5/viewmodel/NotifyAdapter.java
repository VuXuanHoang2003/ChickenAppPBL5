package com.example.chickenapppbl5.viewmodel;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.chickenapppbl5.R;
import com.example.chickenapppbl5.model.NotifyItem;

import java.util.List;

public class NotifyAdapter extends RecyclerView.Adapter<NotifyAdapter.ViewHolder> {
    public static List<NotifyItem>notifysList;
    private OnNotifyListener onNotifyListener;
    public NotifyAdapter(List<NotifyItem> notifysList,OnNotifyListener onNotifyListener){
        this.notifysList=notifysList;
        this.onNotifyListener=onNotifyListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notify_items, parent, false);

        return new ViewHolder(view,onNotifyListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        NotifyItem notify = notifysList.get(position);
        holder.tvTitle.setText(notify.getTitle());
        holder.tvUuid.setText(notify.getUuid());
        long unixtime = Long.parseLong(notify.getTime());
        String date = new java.text.SimpleDateFormat("d/M/yyyy H:mm:ss").format(new java.util.Date(unixtime*1000L));
        holder.tvTime.setText(date);
        Glide.with(holder.itemView.getContext()).load(notify.getImg()).into(holder.ivImg);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {

        if (notifysList != null)
            return notifysList.size();
        else
            return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView tvTitle;
        public TextView tvTime;
        public TextView tvUuid;

        public TextView tvTimeNow;
        public ImageView ivImg;
        public LinearLayout llNotify;

        OnNotifyListener onNotifyListener;
        public ViewHolder(View view,OnNotifyListener onNotifyListener) {
            super(view);
            // Define click listener for the ViewHolder's View
            tvTitle = view.findViewById(R.id.tv_title);
            tvTitle.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            tvTitle.setSelected(true);
            tvUuid = view.findViewById(R.id.tv_uuid);
            tvTime = view.findViewById(R.id.tv_time);
            ivImg = view.findViewById(R.id.iv_img);
            llNotify = view.findViewById(R.id.layout_notify);
            this.onNotifyListener=onNotifyListener;
            view.setOnClickListener(this);
            // textView = (TextView) view.findViewById(R.id.textView);
        }

        @Override
        public void onClick(View view) {
            onNotifyListener.onNotifyClick(getAdapterPosition());

        }


//        public TextView getTextView() {
//            return textView;
//        }
    }
    public interface OnNotifyListener{
        void onNotifyClick(int position);
    }
}

