package com.example.wildkarrde;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

public class viewDailyTasks extends AppCompatActivity {

    private ArrayList<DailyTask> taskList;

    private RecyclerView DailyRecyclerView;
    private RecyclerView.Adapter DailyAdapter;
    private RecyclerView.LayoutManager DailyLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_daily_tasks);

        //created taskList and populate the arraylist
        createTaskList();
        //build the recyclerview so we have one!
        buildDailyRecyclerView();

    }

    public void createTaskList(){
        taskList = new ArrayList<>();

        //here is where I would get and populate new arraylist to display.
        taskList.add(new DailyTask(R.drawable.ic_not_done, "Take out Trash", "4:45-5:00am"));
        taskList.add(new DailyTask(R.drawable.ic_not_done, "Do HW", "5:00am-9:00pm"));
    }
    public void buildDailyRecyclerView(){
        DailyRecyclerView = findViewById(R.id.dailyRecyclerView);
        DailyRecyclerView.setHasFixedSize(true);
        DailyLayoutManager = new LinearLayoutManager(this);
        DailyAdapter = new DailyAdapter(taskList);

        DailyRecyclerView.setLayoutManager(DailyLayoutManager);
        DailyRecyclerView.setAdapter(DailyAdapter);
    }

}
