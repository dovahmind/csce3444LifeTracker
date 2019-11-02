package com.example.wildkarrde;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button button = (Button) findViewById(R.id.loginUser);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });
    }

    public void loginUser(){
        boolean login_success = false;

        //call validation code here

        Intent intent = new Intent(this, MainActivity.class); //intent to open main activity page

        if(login_success){
            startActivity(intent);
        }
        else {
            Toast.makeText(this, "Invalid login. Try a different Username or Password.", Toast.LENGTH_SHORT);
        }
    }
}
