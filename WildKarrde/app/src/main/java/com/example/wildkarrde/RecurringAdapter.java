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

public class RecurringAdapter extends RecyclerView.Adapter<RecurringAdapter.RecurringViewHolder> {

    private ArrayList<RecurringTask> RecurringList;
    private OnItemClickListener Listener;

    public interface OnItemClickListener {
        void onItemClick(int position);
        void onCompleteClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener mlistener){
        Listener = mlistener;
    }
    public static class RecurringViewHolder extends RecyclerView.ViewHolder{
        public android.widget.ImageView ImageView;
        public TextView Title;
        public TextView Time;

        public RecurringViewHolder(View itemView, OnItemClickListener listener){
            super(itemView);
            ImageView = itemView.findViewById(R.id.checkboxImage);
            Title = itemView.findViewById(R.id.dailyTitle);
            Time = itemView.findViewById(R.id.dailyTime);

            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }
                    }
                }
            });

            ImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onCompleteClick(position);
                        }
                    }
                }
            });
        }
    }


    public RecurringAdapter(ArrayList<RecurringTask> recurringList){
        RecurringList = recurringList;
    }
    @NonNull
    @Override
    public RecurringViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recurring_task, parent, false);
        RecurringViewHolder dvh = new RecurringViewHolder(v, Listener);
        return dvh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecurringViewHolder holder, int position) {
        RecurringTask tecurringDask = RecurringList.get(position);

        //based on what the value of the image resource is, it changes the image resource displayed
        if(tecurringDask.getCheckboxResource() == 1){ //if checkboxresource is 1, it is done
            holder.ImageView.setImageResource(R.drawable.ic_done);
        }
        else //else it is not done
            holder.ImageView.setImageResource(R.drawable.ic_not_done);

        holder.Title.setText(tecurringDask.getTitle());
        holder.Time.setText(tecurringDask.getDisplayTime());
    }

    @Override
    public int getItemCount() {
        return RecurringList.size();
    }
}

