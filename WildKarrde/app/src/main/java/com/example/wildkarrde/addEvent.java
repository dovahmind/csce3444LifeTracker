package com.example.wildkarrde;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.text.SimpleDateFormat;

public class addEvent extends AppCompatActivity {

    ProgressDialog p;
    String responseresult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        Button button = (Button) findViewById(R.id.initEvent);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initEvent();
            }
        });
    }

    public void initEvent() {

        /* A boolean stating whether the connection
        attempt should be made or not based on the
        inputted values
         */
        boolean makeattempt = true;

        EditText name = (EditText) findViewById(R.id.enterEventName);
        String eventName = name.getText().toString();

        EditText date = (EditText) findViewById(R.id.enterStringEventDate);
        String eventDate = date.getText().toString();

        EditText location = (EditText) findViewById(R.id.enterStringEventLocation);
        String eventLocation = location.getText().toString();

        EditText description = (EditText) findViewById(R.id.enterEventDescription);
        String eventDescription = description.getText().toString();

        EditText starttime = (EditText) findViewById(R.id.enterStartTime);
        String eventStartTime = starttime.getText().toString();

        //System.out.println("The start time is: " + eventStartTime + "\n");

        EditText endtime = (EditText) findViewById(R.id.enterEndTime);
        String eventEndTime = endtime.getText().toString();

        //System.out.println("The end time is: " + eventEndTime + "\n");

        /* Checking for null values */


        /* If the name is no null, then tell the user at least a name is required */
        if(eventName.isEmpty())
        {
            Toast name_err = Toast.makeText(this,"Event name required!", Toast.LENGTH_SHORT);
            name_err.show();
            makeattempt = false;
        }

        /* If the date is left blank, then just grab the user's phone date instead */
        if(eventDate.isEmpty())
        {
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
            Date currentTime = Calendar.getInstance().getTime();

            eventDate = dateFormat.format(currentTime);

            /* FOR TESTING ! */
            //System.out.println(eventDate);
        }

        /* We are not working with the location for now */


        /* If there is no description, then just put no desc */
        if(eventDescription.isEmpty())
        {
            eventDescription = "no desc";
        }


        /* If there is no start time, then just 00:00:00 for the start time */
        if(eventStartTime.isEmpty())
        {
            eventStartTime = "00:00:00";
        }

        /* If there is no end time, then just put 00:00:00 for the end time */
        if(eventEndTime.isEmpty())
        {
            eventEndTime = "00:00:00";
        }

        //initialize event and put it in backend!!!
        /* THIS IS A MANUALLY ENCODED JSON STRING (not enough time to do it through a standardized library)*/
        String json_test = "{\n" +
                "  \"type\": \"event\",\n" +
                "  \"title\": \"" + eventName + "\",\n" +
                "  \"date\": \"" + eventDate + "\",\n" +
                "  \"description\": \"" + eventDescription + "\",\n" +
                "  \"start_time\": \"" + eventStartTime + "\", \n" +
                "  \"end_time\": \"" + eventEndTime + "\", \n" +
                "  \"completed\": \"0\"\n" +
                "}";

        /* Call the EventConnector AsyncTask here so long as we can make a connection attempt to the server */
        if(makeattempt){
            //System.out.println("Making connection attempt.\n");
            connectionattempt attempter = new connectionattempt();
            attempter.execute(json_test);
        }


    }


    private class connectionattempt extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            p = new ProgressDialog(addEvent.this);
            p.setMessage("Sending data to server...");
            p.setIndeterminate(false);
            p.setCancelable(false);
            p.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String inpdata = strings[0];

            System.out.println("The inputted data: " + inpdata + "\n");

            System.out.println("At beginning of connection attempt\n");

            /* Creating a custom ssl contextobject by making an
            allowSelfSignedCerts object to call an getsslcontext
            function whick will retrieve a custom SSLContext object
            based on a self-signed certificate
             */
            allowSelfSignedCerts retrieve = new allowSelfSignedCerts();
            SSLContext customssl = null;
            try {
                customssl = retrieve.getsslcontext(getApplicationContext());
            } catch (CertificateException e) {
                e.printStackTrace();
            } catch (KeyStoreException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (KeyManagementException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InvalidAlgorithmParameterException e) {
                e.printStackTrace();
            }

            if (customssl == null) {
                System.out.println("making a custom ssl context didn't work!");
                return ("ssl error!");
            }

            String result;
            /* Forming the url to the registration php script */
            URL url = null;
            try {
                url = new URL("https://138.68.23.145/add_reminder.php");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            /* FOR DEVELOPMENT PURPOSES ONLY!
            THIS ALLOWS ANY HOST NAME (ip hostname is being denied by javax.ssl and general
            ssl guideline) TO PASS THROUGH.
             */
            /*Null Host Name verifier code is from Noam's answer at:
            https://stackoverflow.com/questions/14619781/java-io-ioexception-hostname-was-not-verified
             */
            HttpsURLConnection.setDefaultHostnameVerifier(new NullHostNameVerifier());
            HttpsURLConnection urlConnection;

            try {
                urlConnection = (HttpsURLConnection) url.openConnection();
            } catch (IOException ex) {
                return null;
            }

            System.out.println("Opened connection ok\n");

            /* Gathering the SSLContext object information from the passed parameter and
            setting the SSLSocketFactory for the urlConnection */
            SSLSocketFactory sf = customssl.getSocketFactory();
            urlConnection.setSSLSocketFactory(sf);

            System.out.println("Set socket factory ok\n");

            /* The following code below is based on answers from both Ricardo, as well as NateS's reply
            from the following link:
            https://stackoverflow.com/questions/44305351/send-data-in-request-body-using-httpurlconnection?rq=1
             */

            /* Setting the urlConnection's output option to true */
            urlConnection.setDoOutput(true);

            /* Setting the HTTP method to POST */
            try {
                urlConnection.setRequestMethod("POST");
            } catch (ProtocolException e) {
                e.printStackTrace();
            }


            /* Retrieving the cookie from the shared preferences */
            cookie_storage cookie_retriever = new cookie_storage();
            String raw_cookie = cookie_retriever.get_cookie(getApplicationContext());

            /* Parsing the raw_cookie string to only get the PHPSESSID information before the ;*/
            String[] cookielist = raw_cookie.split(";");
            String cookie_value = cookielist[0];

            //System.out.println("Cookie value is: " + cookie_value + "\n");

            // Setting the cookie header
            urlConnection.setRequestProperty("Cookie", cookie_value);

            /* Setting content type to json. (commented out for now, since add_reminder.php
            * will just get the json information from a json_data parameter)*/
            //urlConnection.setRequestProperty("Content-Type", "application/json");

            /* Connecting to the server */
            try {
                urlConnection.connect();
            } catch (IOException e) {
                e.printStackTrace();
            }


            System.out.println("Connection was made\n");

            /* Prepping the output stream */
            OutputStream sender = null;
            try {
                sender = urlConnection.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }

            /* An output streamwriter to write data into the Output Stream */
            OutputStreamWriter bodyformer = null;
            try {
                bodyformer = new OutputStreamWriter(sender, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            /* Writing the POST Request body */
            String bodyparam = "json_data=" + inpdata;

            /* Writing using output stream writer */
            try {
                bodyformer.write(bodyparam);
            } catch (IOException e) {
                e.printStackTrace();
            }

            /* Flushing the output stream writer */
            try {
                bodyformer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }

            /* Closing the output stream itself */
            try {
                sender.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            System.out.println("Sent data\n");


            /*Gathering response from POST request */
            InputStream response = null;

            try {
                response = urlConnection.getInputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }

        /* The following InputStream to string conversion trick is from Pavel Repin's
        answer at the following link:
        https://stackoverflow.com/questions/309424/how-do-i-read-convert-an-inputstream-into-a-string-in-java
         */
            java.util.Scanner s = new java.util.Scanner(response).useDelimiter("\\A");
            result = s.hasNext() ? s.next() : "";

            /* Closing the input stream */
            try {
                response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            System.out.println("Here is the response: " + result + "\n");

            return result;
        }

        /* For post execute here, just return the resulting string */
        @Override
        protected void onPostExecute(String result) {

            System.out.println("We are in postexecute\n");
            System.out.println("Here is the response: " + result + "\n");
            super.onPostExecute(result);
            responseresult = result;
            System.out.println("Here is the responseresult string: " + responseresult + "\n");
            p.dismiss();

            /* Checking to see if the event addition was a success */
            String successstr = "success";

            boolean addsuccess = false;
            if(!responseresult.isEmpty()) {
                addsuccess = responseresult.toLowerCase().contains(successstr.toLowerCase());
            }

            /* If the responseresult contains success, then go back to the main activity */
            if(addsuccess)
            {
                Toast succmsg = Toast.makeText(getApplicationContext(), "Event was successfully added!", Toast.LENGTH_SHORT);
                succmsg.show();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }

            /* Otherwise, display the error the server gave back */
            else
            {
                Toast errormsg = Toast.makeText(getApplicationContext(), responseresult, Toast.LENGTH_SHORT);
                errormsg.show();
            }
        }
    }
}