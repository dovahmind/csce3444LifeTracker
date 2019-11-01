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

        addReminderButton = (Button) findViewById(R.id.addReminderButton); //declares buttons
        addReminderButton.setOnClickListener(new View.OnClickListener() { // sets listener for clicks
            @Override
            public void onClick(View v) { //if clicks, call function bellow
                openAddReminder();
            }
        });
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
    public void openAddReminder(){
        Intent intent = new Intent(this, addReminder.class);
        startActivity(intent);
    }

    public void openDailyTasks(){
        Intent intent = new Intent(this, viewDailyTasks.class);
        startActivity(intent);
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

    //here's the menu. I was messing around with it for the most part right now, trying to understand it.
    //feel free to ignore it.
    @Override // here I have created the menu at the top of the application
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater(); //inflates menu to show all the options
        inflater.inflate(R.menu.menu1, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) { //when an item is selected, this is called
        switch(item.getItemId()){
            case R.id.item1://if item one is clicked... opens add reminders
                openAddReminder();
                return true;
            case R.id.item2:
                openPastReminders();
                return true;
            case R.id.item3:
                Toast.makeText(this, "item 3 selected", Toast.LENGTH_SHORT).show();
                return true;
            default: return super.onOptionsItemSelected(item);
        }
    }

    public void openPastReminders(){
        Intent intent = new Intent(this, pastReminders.class);
        startActivity(intent);
    }
}
