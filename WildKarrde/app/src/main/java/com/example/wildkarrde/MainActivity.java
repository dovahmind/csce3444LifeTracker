package com.example.wildkarrde;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

public class MainActivity extends AppCompatActivity {
    //declare all the buttons
    private Button addReminderButton;
    private Button viewDailyTasks;
    private Button viewRecurringTasks;
    private Button viewLogout;
    private Button viewStats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //here is where we will display whatever current tasks we have. Need to find a solution!

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


        viewLogout = (Button) findViewById(R.id.viewLogout);
        viewLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    openLogout();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (CertificateException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (InvalidAlgorithmParameterException e) {
                    e.printStackTrace();
                } catch (KeyStoreException e) {
                    e.printStackTrace();
                } catch (KeyManagementException e) {
                    e.printStackTrace();
                }
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

    /* ADDING LOGOUT AS AN OPTION HERE */
    public void openLogout() throws IOException, NoSuchAlgorithmException, CertificateException, InvalidAlgorithmParameterException, KeyStoreException, KeyManagementException {
        /* First gather the applicationContext */
        Context appcontext = getApplicationContext();

        /* making a logout object to perform actions */
        logout logouthelper = new logout();

        /* First, making the server call to delete the session */
        boolean connresult = logouthelper.deleteServerSide(appcontext);

        /* If the server successfully logged out,
        then delete the stored cookie from stored preferences,
        display success message, then go back to login activity
         */
        if(connresult) {
            /* Then delete the stored cookie from shared preferences */
            cookie_storage cookie_deleter = new cookie_storage();
            cookie_deleter.delete_cookie(appcontext);

            /* Then display a toast message indiciating logout */
            Toast loggingout = Toast.makeText(this, "Logging out!", Toast.LENGTH_SHORT);
            loggingout.show();

            /* Then start the login activity from the main menu */
            Intent intent = new Intent(this, login.class);
            startActivity(intent);
        }

        /* Otherwise, just display an error message */
        else{
            Toast logouterror = Toast.makeText(this, "Couldn't logout", Toast.LENGTH_SHORT);
            logouterror.show();
        }
    }

    //here's the menu. Menu is how you add Reminder, currently. select which you'd like to add from the dropdown.
    //I'm not attached to this design of how adding Reminder works. Let me know what y'all think.
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
            case R.id.profileIcon:
                openProfile();
                return true;
            default: return super.onOptionsItemSelected(item);
        }
    }

    public void openPastReminders(){
        Intent intent = new Intent(this, pastReminders.class);
        startActivity(intent);
    }
}
