package com.example.chickenapppbl5.viewmodel;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chickenapppbl5.ChickenItemActivity;
import com.example.chickenapppbl5.Chicken_Info;
import com.example.chickenapppbl5.MainActivity;
import com.example.chickenapppbl5.R;
import com.example.chickenapppbl5.model.ChickenBreed;
import com.squareup.picasso.Picasso;

import java.time.Instant;
import java.util.List;

public class ChickenAdapter extends RecyclerView.Adapter<ChickenAdapter.ViewHolder> {
    public static List<ChickenBreed>chickensList;
    private OnChickenListener onChickenListener;
    public ChickenAdapter(List<ChickenBreed> chickensList,OnChickenListener onChickenListener){
        this.chickensList=chickensList;
        this.onChickenListener=onChickenListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.info_chicken, parent, false);

        return new ViewHolder(view,onChickenListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        ChickenBreed chickenBreed = chickensList.get(position);
        holder.edtNumberOfChicken.setText(chickenBreed.getChicken());
        String formattedTime = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            Instant time = chickenBreed.getInstantTime();
            formattedTime = time.toString();
        }
        holder.edtTimePredict.setText(formattedTime);
        //holder.editMaxCel.setText(chickenBreed.get());
        //holder.editMinCel.setText(chickenBreed.getChicken());
        //holder.editTimeCel.setText(chickenBreed.getChicken());
        Picasso.get().load(chickenBreed.getUrl()).into(holder.ivPredict);

        holder.editTimeCel.setText(formattedTime);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final ChickenBreed ck = chickensList.get(position);
                Toast.makeText(v.getContext(), "You clicked " + chickenBreed.getUuid(), Toast.LENGTH_SHORT).show();
                Intent i = new Intent(v.getContext(), Chicken_Info.class);
                i.putExtra("id", Long.toString(ck.getId()));
                i.putExtra("uuid", ck.getUuid());
                i.putExtra("url", ck.getUrl());
                i.putExtra("predict", ck.getPredict());
                i.putExtra("infared", ck.getInfared());
                i.putExtra("time", ck.getTime());
                i.putExtra("labels", ck.getLabels());
                i.putExtra("chicken", ck.getChicken());
                i.putExtra("non-chicken", ck.getNon_chicken());
                Log.i("hello", ck.getUuid());
                Toast.makeText(
                        v.getContext(),
                        "You clicked " + ck.getUuid(),
                        Toast.LENGTH_SHORT
                );
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

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView ivPredict;
        private ImageView ivInfared;
        private EditText edtNumberOfChicken;
        private EditText edtTimePredict;
        private EditText editMaxCel;
        private EditText editMinCel;
        private EditText editTimeCel;
        OnChickenListener onChickenListener;
        public ViewHolder(View view,OnChickenListener onChickenListener) {
            super(view);
            // Define click listener for the ViewHolder's View
            ivPredict = view.findViewById(R.id.img_predict);
            ivPredict = view.findViewById(R.id.img_infar);
            edtNumberOfChicken = view.findViewById(R.id.edt_number_chicken);
            edtTimePredict = view.findViewById(R.id.edt_time);

            editMaxCel = view.findViewById(R.id.edt_Max_Cel);
            editMinCel = view.findViewById(R.id.edt_min_cel);
            editTimeCel = view.findViewById(R.id.edt_time_cel);
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

