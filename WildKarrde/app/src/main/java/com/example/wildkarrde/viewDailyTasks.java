package com.example.wildkarrde;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import java.util.ArrayList;

public class viewDailyTasks extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_daily_tasks);

        //get arraylist of items
        ArrayList<DailyTask> taskList = new ArrayList<>();
        taskList.add(new DailyTask(R.drawable.ic_not_done, "Take out Trash", "4:45-5:00am"));
        taskList.add(new DailyTask(R.drawable.ic_not_done, "Do HW", "5:00am-9:00pm"));

        //need to have recycler view for viewdailytasks


        //declare adapter and stuff
    }

}
