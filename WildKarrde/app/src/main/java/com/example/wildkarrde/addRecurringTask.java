package com.example.wildkarrde;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class addRecurringTask extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recurring_task);

        //recurring task initialize button
        Button button = (Button) findViewById(R.id.initRecurringTask);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                initRecurring();
            }
        });
    }

    //hopefully this is the function where we create and add a new recurring task to the backend
    public void initRecurring(){
        EditText name = (EditText) findViewById(R.id.enterRecurringName);
        String recurringName = name.getText().toString();

        EditText date = (EditText) findViewById(R.id.enterRecurringDate);
        String recurringDate = date.getText().toString();

        EditText location = (EditText) findViewById(R.id.enterStringRecurringLocation);
        String recurringLocation = location.getText().toString();

        EditText description = (EditText) findViewById(R.id.enterRecurringDescription);
        String recurringDescription = description.getText().toString();

        EditText time = (EditText) findViewById(R.id.enterRecurringTime);
        String recurringTime = time.getText().toString();

        //initialize a new daily task to be added to backend!
        //need to pass reccuring task info to backend.

        String method = "AddR";//Method call to add a recurring task. Used in Background Task
        BackgroundTask backgroundTask = new BackgroundTask(this);
        backgroundTask.execute(method, name, date, location, description, time);
        finish();
    }


}
