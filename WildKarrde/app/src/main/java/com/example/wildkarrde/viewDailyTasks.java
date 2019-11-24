package com.example.wildkarrde;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

public class viewDailyTasks extends AppCompatActivity {

    private RecyclerView DailyRecyclerView;
    private RecyclerView.Adapter DailyAdapter;
    private RecyclerView.LayoutManager DailyLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_daily_tasks);

        //get arraylist of items
        ArrayList<DailyTask> taskList = new ArrayList<>();
        taskList.add(new DailyTask(R.drawable.ic_not_done, "Take out Trash", "4:45-5:00am"));
        taskList.add(new DailyTask(R.drawable.ic_not_done, "Do HW", "5:00am-9:00pm"));

        DailyRecyclerView = findViewById(R.id.dailyRecyclerView);
        DailyRecyclerView.setHasFixedSize(true);
        DailyLayoutManager = new LinearLayoutManager(this);
        DailyAdapter = new DailyAdapter(taskList);

        DailyRecyclerView.setLayoutManager(DailyLayoutManager);
        DailyRecyclerView.setAdapter(DailyAdapter);
        //need to have recycler view for viewdailytasks


        //declare adapter and stuff
    }

}
