package com.example.wildkarrde;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import javax.net.ssl.*;

public class registration extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        getSupportActionBar().setTitle("Registration");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Button button = (Button) findViewById(R.id.submitRegister);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                try {
                    registerUser();
                } catch (CertificateException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (KeyStoreException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (KeyManagementException e) {
                    e.printStackTrace();
                } catch (InvalidAlgorithmParameterException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void registerUser() throws CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException, KeyManagementException, InvalidAlgorithmParameterException {
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

        /* Gathering this activity's context so files can be
        grabbed by non-activity functions, and data can easily be
        passed around
         */
        Context regis = this;

        /* Creating a custom ssl contextobject by making an
        allowSelfSignedCerts object to call an getsslcontext
        function whick will retrieve a custom SSLContext object
        based on a self-signed certificate
         */
        allowSelfSignedCerts retrieve = new allowSelfSignedCerts();
        SSLContext customssl;
        customssl = retrieve.getsslcontext(regis);

        if(customssl == null)
        {
            System.out.println("making a custom ssl context didn't work!");
        }

        /* Displaying the connection result via Toast */

        /*
        Toast response_result = Toast.makeText(this, connresult, Toast.LENGTH_SHORT);
        response_result.show();
        */

        String connresult = null;

        if(!(registration_pass.equals(registration_check_pass))){
            Toast pass_err = Toast.makeText(this,"Passwords do not match.", Toast.LENGTH_SHORT);
            pass_err.show();
        }

        /* Otherwise, make the http connection here */
        else{
            /* Forming a string list containing registration attributes */
            String attr[] = {registration_email, registration_username, registration_pass};
            /* Make the connection attempt here by making a helper object
            and then calling it's connection attempt function. Also passing a
            context into the function to work with */
            registerHelper registerattempt = new registerHelper();
            connresult = registerattempt.connectionattempt(customssl,attr);

            /* If a success message was returned, then display it and
            return to the login screen
            */
            String resulttocheck = "You have successfully registered, you can now login!";
            if(connresult.equals(resulttocheck))
            {
                Toast gen_success = Toast.makeText(this, connresult, Toast.LENGTH_SHORT);
                gen_success.show();
                System.out.println("Success!");
                startActivity(intent);
            }

            /* Otherwise, display the error message the server sent back */
            else{
                Toast gen_err = Toast.makeText(this, connresult, Toast.LENGTH_SHORT);
                gen_err.show();
            }
        }
    }
}
