package com.example.wildkarrde;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import javax.net.ssl.SSLContext;

public class login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //CREATE CHECK COOKIE FUNCTION HERE AND THEN CALL IT BEFORE MAKING LAYOUT

        setContentView(R.layout.activity_login);

        getSupportActionBar().setTitle("Login");

        Button button = (Button) findViewById(R.id.loginUser);

        /* Before doing anything else, check to see if there
        is a cookie in storage, and if there is then go to MainActivity */
        String conf = "no cookie!";

        Context currcontext = this;

        cookie_storage cookie_retriever = new cookie_storage();

        String cookieresult = cookie_retriever.get_cookie(currcontext);

        if (!cookieresult.equals(conf))
        {
            Intent beginningintent = new Intent(this, MainActivity.class); //intent to open main activity page
            startActivity(beginningintent);
        }

        else{
            Toast.makeText(this, "Couldn't retrieve cookie!", Toast.LENGTH_SHORT);
        }


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    loginUser();
                } catch (CertificateException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (KeyManagementException e) {
                    e.printStackTrace();
                } catch (KeyStoreException e) {
                    e.printStackTrace();
                } catch (InvalidAlgorithmParameterException e) {
                    e.printStackTrace();
                }
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

    public void loginUser() throws CertificateException, NoSuchAlgorithmException, IOException, KeyManagementException, KeyStoreException, InvalidAlgorithmParameterException {

        boolean login_success = false;

        String result;

        EditText user = (EditText) findViewById(R.id.enterUsernameOrEmail);
        String login_user = user.getText().toString();

        EditText pass = (EditText) findViewById(R.id.enterPassword);
        String login_pass = pass.getText().toString();

        //call validation code here

        Intent intent = new Intent(this, MainActivity.class); //intent to open main activity page

        /* Gathering this activity's context so files can be
        grabbed by non-activity functions, and data can easily be
        passed around
         */
        Context regis = this;

        loginhelper loginattempt = new loginhelper();

        String attr[] = {login_user, login_pass};

        result = loginattempt.connectionattempt(regis, attr);

        String successstr = "success";
        login_success = result.toLowerCase().contains(successstr.toLowerCase());

        if(login_success){
            System.out.println("The result was successful.\n");
            startActivity(intent);
        }
        else {
            //Toast.makeText(this, "Invalid login. Try a different Username or Password.", Toast.LENGTH_SHORT);
            Toast.makeText(this, result, Toast.LENGTH_SHORT);
        }
    }
}
