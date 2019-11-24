package com.example.recyclerviewproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ExampleAdapter extends RecyclerView.Adapter<ExampleAdapter.ExampleViewHolder> {

    public ArrayList<ExampleItem> ExampleList;

    public static class ExampleViewHolder extends RecyclerView.ViewHolder {
            public ImageView ImageView;
            public TextView TextView1;
            public TextView TextView2;

        public ExampleViewHolder(View itemView){

            super(itemView);
            ImageView = itemView.findViewById(R.id.imageView);
            TextView1 = itemView.findViewById(R.id.textView);
            TextView2 = itemView.findViewById(R.id.textView2);
        }

    }
    public ExampleAdapter(ArrayList<ExampleItem> exampleList){
        //maybe sort the list here?
        ExampleList = exampleList;
    }

    @NonNull
    @Override
    public ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.example_item, parent, false);
        ExampleViewHolder evh = new ExampleViewHolder(v);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull ExampleViewHolder holder, int position) {
        ExampleItem currentItem = ExampleList.get(position);

        holder.ImageView.setImageResource(currentItem.getImageResource());
        holder.TextView1.setText(currentItem.getText1());
        holder.TextView2.setText(currentItem.getText2());
    }

    @Override
    public int getItemCount() {
        return ExampleList.size();
    }
}
