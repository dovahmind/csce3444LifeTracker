package com.example.wildkarrde;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class DailyAdapter extends RecyclerView.Adapter<DailyAdapter.DailyViewHolder> {

    private ArrayList<DailyTask> DailyList;

    public static class DailyViewHolder extends RecyclerView.ViewHolder{
        public android.widget.ImageView ImageView;
        public TextView Title;
        public TextView Time;

        public DailyViewHolder(View itemView){
            super(itemView);
            ImageView = itemView.findViewById(R.id.checkboxImage);
            Title = itemView.findViewById(R.id.dailyTitle);
            Time = itemView.findViewById(R.id.dailyTime);
        }
    }


    public DailyAdapter(ArrayList<DailyTask> dailyList){
        DailyList = dailyList;
    }
    @NonNull
    @Override
    public DailyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.daily_task, parent, false);
        DailyViewHolder dvh = new DailyViewHolder(v);
        return dvh;
    }

    @Override
    public void onBindViewHolder(@NonNull DailyViewHolder holder, int position) {
        DailyTask tailyDask = DailyList.get(position);

        holder.ImageView.setImageResource(tailyDask.getCheckboxResource());
        holder.Title.setText(tailyDask.getTitle());
        holder.Time.setText(tailyDask.getDate());
    }

    @Override
    public int getItemCount() {
        return DailyList.size();
    }
}
