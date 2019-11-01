package com.example.wildkarrde;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    //declare all the buttons
    private Button addReminderButton;
    private Button viewDailyTasks;
    private Button viewRecurringTasks;
    private Button viewProfile;
    private Button viewStats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //comments above repeat for all of the rest
        viewDailyTasks = (Button) findViewById(R.id.viewDailyTasks);
        viewDailyTasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                openDailyTasks();
            }
        });

        viewRecurringTasks = (Button) findViewById(R.id.viewRecurringTasks);
        viewRecurringTasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                openRecurringTasks();
            }
        });

        viewProfile = (Button) findViewById(R.id.viewRecurringTasks);
        viewProfile.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                openProfile();
            }
        });

        viewStats = (Button) findViewById(R.id.viewStats);
        viewStats.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                openStats();
            }
        });
    }

    //opens the activities, called from MainActivity class close above

    public void openDailyTasks(){
        Intent intent = new Intent(this, viewDailyTasks.class);
        startActivity(intent);
        Toast.makeText(this, "Hey everyone", Toast.LENGTH_SHORT);
    }

    public void openRecurringTasks(){
        Intent intent = new Intent (this, viewRecurringTasks.class);
        startActivity(intent);
    }

    public void openProfile(){
        Intent intent = new Intent (this, viewProfile.class);
        startActivity(intent);
    }

    public void openStats(){
        Intent intent = new Intent (this, viewStats.class);
        startActivity(intent);
    }

    public void openAddDailyTask(){
        Intent intent = new Intent(this, addDailyTask.class);
        startActivity(intent);
    }
    public void openAddRecurringTask(){
        Intent intent = new Intent(this, addRecurringTask.class);
        startActivity(intent);
    }

    public void openAddEvent() {
        Intent intent = new Intent(this, addEvent.class);
        startActivity(intent);
    }

    //here's the menu. Menu is how you add reminders, currently. select which you'd like to add from the dropdown.
    //I'm not attached to this design of how adding reminders works. Let me know what y'all think.
    @Override // here I have created the menu at the top of the application
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater(); //inflates menu to show all the options
        inflater.inflate(R.menu.menu1, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) { //when an item is selected, this is called
        switch(item.getItemId()){
            case R.id.addDailyTask:
                openAddDailyTask();
                return true;
            case R.id.addRecurringTask:
                openAddRecurringTask();
                return true;
            case R.id.addEvent:
                openAddEvent();
                return true;
            default: return super.onOptionsItemSelected(item);
        }
    }

    public void openPastReminders(){
        Intent intent = new Intent(this, pastReminders.class);
        startActivity(intent);
    }
}
