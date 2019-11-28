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

import org.json.JSONException;
import org.json.JSONObject;

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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;

public class addRecurringTask extends AppCompatActivity {
    ProgressDialog p;
    String responseresult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recurring_task);

        //recurring task initialize button
        Button button = (Button) findViewById(R.id.initRecurringTask);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initRecurring();
            }
        });
    }

    //hopefully this is the function where we create and add a new recurring task to the backend
    public void initRecurring() {

        /* A boolean stating whether the connection
        attempt should be made or not based on the
        inputted values
         */
        boolean makeattempt = true;

        EditText name = (EditText) findViewById(R.id.enterRecurringName);
        String recurringName = name.getText().toString();

        EditText date = (EditText) findViewById(R.id.enterRecurringDate);
        String recurringDate = date.getText().toString();

        EditText location = (EditText) findViewById(R.id.enterStringRecurringLocation);
        String recurringLocation = location.getText().toString();

        EditText description = (EditText) findViewById(R.id.enterRecurringDescription);
        String recurringDescription = description.getText().toString();

        EditText starttime = (EditText) findViewById(R.id.enterRecurringStartTime);
        String recurringStartTime = starttime.getText().toString();

        System.out.println("The start time is: " + recurringStartTime + "\n");

        EditText endtime = (EditText) findViewById(R.id.enterRecurringEndTime);
        String recurringEndTime = endtime.getText().toString();

        System.out.println("The end time is: " + recurringEndTime + "\n");


        /* Gathering interval values from user input here */
        EditText monthint = (EditText) findViewById(R.id.enterRecurringMonthInt);
        String rmonthint = monthint.getText().toString();

        EditText weekint = (EditText) findViewById(R.id.enterRecurringWeekInt);
        String rweekint = weekint.getText().toString();

        EditText dayint = (EditText) findViewById(R.id.enterRecurringDayInt);
        String rdayint = dayint.getText().toString();

        /* Checking for null values */


        /* If the name is no null, then tell the user at least a name is required */
        if (recurringName.isEmpty()) {
            Toast name_err = Toast.makeText(this, "Recurring task name required!", Toast.LENGTH_SHORT);
            name_err.show();
            makeattempt = false;
        }

        /* If the date is left blank, then just grab the user's phone date instead */
        if (recurringDate.isEmpty()) {
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
            Date currentTime = Calendar.getInstance().getTime();

            recurringDate = dateFormat.format(currentTime);

            /* FOR TESTING ! */
            System.out.println(recurringDate);
        }

        /* We are not working with the location for now */


        /* If there is no description, then just put no desc */
        if (recurringDescription.isEmpty()) {
            recurringDescription = "no desc";
        }


        /* If there is no start time, then just 00:00:00 for the start time */
        if (recurringStartTime.isEmpty()) {
            recurringStartTime = "00:00:00";
        }

        /* If there is no end time, then just put 00:00:00 for the end time */
        if (recurringEndTime.isEmpty()) {
            recurringEndTime = "00:00:00";
        }

        /* If any of the intervals where not entered, then set intervals to 0 */
        if (rmonthint.isEmpty()) {
            rmonthint = "0";
        }

        if (rweekint.isEmpty()) {
            rweekint = "0";
        }

        if (rdayint.isEmpty()) {
            rdayint = "0";
        }

        //initialize event and put it in backend!!!
        /* THIS IS A MANUALLY ENCODED JSON STRING (not enough time to do it through a standardized library)*/
        /* (Comment this out for now, and attempt to make a json object instead, convert it to a string, and send that */

        /*
        String json_test = "{\n" +
                "  \"type\": \"event\",\n" +
                "  \"title\": \"" + eventName + "\",\n" +
                "  \"date\": \"" + eventDate + "\",\n" +
                "  \"description\": \"" + eventDescription + "\",\n" +
                "  \"start_time\": \"" + eventStartTime + "\", \n" +
                "  \"end_time\": \"" + eventEndTime + "\", \n" +
                "  \"completed\": \"0\"\n" +
                "}";

         */

        /* Forming a JSON object for a recurring task specifically, refer to
        * add_reminder.php for names required in put
         */
        JSONObject reocctask = new JSONObject();

        try {
            reocctask.put("type", "reocc");
            reocctask.put("title", recurringName);
            reocctask.put("date", recurringDate);
            reocctask.put("description", recurringDescription);
            reocctask.put("start_time", recurringStartTime);
            reocctask.put("end_time", recurringEndTime);
            reocctask.put("completed", 0);
            reocctask.put("month_time_int", rmonthint);
            reocctask.put("week_time_int", rweekint);
            reocctask.put("day_time_int", rdayint);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        /* Converting the jsonobject to a string */
        String json_obj = reocctask.toString();

        /* Call the EventConnector AsyncTask here so long as we can make a connection attempt to the server */
        if (makeattempt) {
            System.out.println("Making connection attempt.\n");
            connectionattempt attempter = new connectionattempt();
            attempter.execute(json_obj);
        }


        //initialize a new daily task to be added to backend!
        //need to pass reccuring task info to backend.

        /* To Martin from Jacob: Uncomment this if you get BackgroundTask.java working with our server, and remove my
            connectionattempt code block below.
            My reason for commenting this out for now is that we are using json for adding reminders to the server
            as well as when we are returning reminders (this way multiple post parameters do not have to be accounted for, but
            everything is gathered from one json document). This design philosophy conflicts with BackgroundTask.java
            which is using html form (through URLEncoder) post parameters. If you need an example of how to manually form a
            json string, check addEvent.java, or look at (be sure to copy the full link)
            https://developer.android.com/reference/org/json/JSONObject.html#put(java.lang.String,%20double) and
            https://www.vogella.com/tutorials/AndroidJSON/article.html#write-json for examples on how to form a json object.
            Also, to see how json is interpreted on the server end, look at add_reminder.php on github.
         */

        /*
        String method = "AddR";//Method call to add a recurring task. Used in Background Task
        BackgroundTask backgroundTask = new BackgroundTask(this);
        backgroundTask.execute(method, name, date, location, description, time);
        finish();
        */
    }


    private class connectionattempt extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            p = new ProgressDialog(addRecurringTask.this);
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

            /* Setting content type to json */
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
            if (!responseresult.isEmpty()) {
                addsuccess = responseresult.toLowerCase().contains(successstr.toLowerCase());
            }

            /* If the responseresult contains success, then go back to the main activity */
            if (addsuccess) {
                Toast succmsg = Toast.makeText(getApplicationContext(), "Event was successfully added!", Toast.LENGTH_SHORT);
                succmsg.show();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }

            /* Otherwise, display the error the server gave back */
            else {
                Toast errormsg = Toast.makeText(getApplicationContext(), responseresult, Toast.LENGTH_SHORT);
                errormsg.show();
            }
        }
    }
}
