package com.example.wildkarrde;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class addDailyTask extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_daily_task);

        Button button = (Button)findViewById(R.id.initDailyTask);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                initDailyTask();
            }
        });
    }

    public void initDailyTask(){
        //need to outline again what parameters we need to pass for all of the adding functions.
        EditText name = (EditText)findViewById(R.id.enterDailyName);
        String dailyTaskName = name.getText().toString();

        EditText date = (EditText) findViewById(R.id.enterDailyDate);
        String dailyTaskDate = date.getText().toString();

        //initialize a new daily task to be added to backend!

    }
}
