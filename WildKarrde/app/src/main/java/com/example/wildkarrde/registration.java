package com.example.wildkarrde;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class registration extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        getSupportActionBar().setTitle("Registration");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Button button = (Button) findViewById(R.id.registerUser);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                registerUser();
            }
        });
    }

    public void registerUser(){
        boolean registration_success = false;

        EditText newUsername = (EditText) findViewById(R.id.createUsername);
        String registration_username = newUsername.getText().toString();

        EditText newEmail = (EditText) findViewById(R.id.createEmail);
        String registration_email = newEmail.getText().toString();

        EditText newPass = (EditText) findViewById(R.id.createPassword);
        String registration_pass = newPass.getText().toString();

        EditText newCheckPass = (EditText) findViewById(R.id.checkPassword);
        String registration_check_pass = newCheckPass.getText().toString();

        Intent intent = new Intent(this, login.class);

        //check registration right here

        if(registration_pass.equals(registration_check_pass)){
            Toast.makeText(this,"Passwords do not match.", Toast.LENGTH_SHORT);
        }
        else if(registration_success){
            startActivity(intent);
        }
        else{
            Toast.makeText(this, "Registration did not work out. Try again.", Toast.LENGTH_SHORT);
        }
    }
}
