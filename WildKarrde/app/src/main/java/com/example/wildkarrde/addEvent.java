package com.example.wildkarrde;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class addEvent extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        Button button = (Button) findViewById(R.id.initEvent);
        button.setOnClickListener(new View.OnClickListener(){
           @Override
           public void onClick(View v){
                initEvent();
           }
        });
    }

    public void initEvent(){
        EditText name = (EditText) findViewById(R.id.enterEventName);
        String eventName = name.getText().toString();

        EditText date = (EditText) findViewById(R.id.enterStringEventDate);
        String eventDate = date.getText().toString();

        //initialize event and put it in backend!!!
    }
}
