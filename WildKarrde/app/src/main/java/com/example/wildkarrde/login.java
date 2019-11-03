package com.example.wildkarrde;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().setTitle("Login");

        Button button = (Button) findViewById(R.id.loginUser);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

        /* The following activity redirection code is based on code
            from Gilad Haimov's answer at the following link:
            https://stackoverflow.com/questions/24610527/how-do-i-get-a-button-to-open-another-activity
         */
        TextView register_text = (TextView) findViewById(R.id.call_register);
        register_text.setOnClickListener(new View.OnClickListener(){
           @Override
           public void onClick(View v)
            {
                startActivity(new Intent(login.this, registration.class));
            }
        });
    }

    public void loginUser(){
        boolean login_success = false;

        EditText user = (EditText) findViewById(R.id.enterUsernameOrEmail);
        String login_user = user.getText().toString();

        EditText pass = (EditText) findViewById(R.id.enterPassword);
        String login_pass = pass.getText().toString();

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
