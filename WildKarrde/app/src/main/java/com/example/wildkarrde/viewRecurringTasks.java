package com.example.wildkarrde;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import org.json.JSONArray;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;

public class viewRecurringTasks extends AppCompatActivity {
    ProgressDialog p;
    //private String serv_response;
    private ArrayList<RecurringTask> taskList;
    private String phonedate;

    /* indrid stands for individual rid, and string indstatus stands for individual status */
    String indrid;
    String indstatus;

    private RecyclerView RecurringRecyclerView;
    private com.example.wildkarrde.RecurringAdapter RecurringAdapter;
    private RecyclerView.LayoutManager RecurringLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_recurring_tasks);

        /* Getting the user's date on their phone */
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date currentTime = Calendar.getInstance().getTime();

        phonedate = dateFormat.format(currentTime);

        /* Making a connectionattempt async task, executing it, making the connection
         * to the server, and storing the server's response into serv_response */
        connectionattempt attempter = new connectionattempt();
        attempter.execute(phonedate);
    }

    public void updateCheckBoxStatus(int position){
        if(taskList.get(position).getCheckboxResource() == 1)
        {
            indrid = Integer.toString(taskList.get(position).getrid());
            indstatus = Integer.toString(0);

            updatestatus changestatus = new updatestatus();
            changestatus.execute(indrid, indstatus);

            taskList.get(position).setCheckboxResource(0);

        }
        else
        {
            indrid = Integer.toString(taskList.get(position).getrid());
            indstatus = Integer.toString(1);

            updatestatus changestatus = new updatestatus();
            changestatus.execute(indrid, indstatus);

            taskList.get(position).setCheckboxResource(1);
        }

        RecurringAdapter.notifyItemChanged(position);
    }


    public void createTaskList(String json_data){
        taskList = new ArrayList<>();

        /* THIS IS WHERE JSON PARSING WILL BE DONE FROM the json_data String! */

        /* CHANGE BY JACOB ROQUEMORE HERE: USING JSONArray to parse each returned json object, and not
        just one json object
         */
        JSONArray jsonarray = null;
        try {
            jsonarray = new JSONArray(json_data);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONObject jsonObject = null;
        for (int i = 0; i < jsonarray.length(); i++) {
            try {
                jsonObject = jsonarray.getJSONObject(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                /*
                will need to modify this jsonObject function call
                should get each item of the recurringtask datatype instead

                title, date, description, start_time, end_time, "checkboxResource", month_time_int, week_time_int, day_time_int
                 */
                taskList.add(new RecurringTask(jsonObject.getInt("rid"),
                        jsonObject.getString("type"), jsonObject.getString("Title"),
                        jsonObject.getString("upcom"), jsonObject.getString("Description"),
                        jsonObject.getInt("checkboxResource")));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        //here is where I would get and populate new arraylist to display.
        /*
        taskList.add(new DailyTask(R.drawable.ic_not_done, "Take out Trash", "4:45-5:00am"));
        taskList.add(new DailyTask(R.drawable.ic_not_done, "Do HW", "5:00am-9:00pm"));
    */
        //sorting the taskList to be from earliest task to latest

        /* COMMENTING OUT SORTING FOR NOW */
        //Collections.sort(taskList, new sortByTime());
    }

    public void buildRecurringRecyclerView(){
        RecurringRecyclerView = findViewById(R.id.recurringRecyclerView);
        RecurringRecyclerView.setHasFixedSize(true);
        RecurringLayoutManager = new LinearLayoutManager(this);
        RecurringAdapter = new RecurringAdapter(taskList);

        RecurringRecyclerView.setLayoutManager(RecurringLayoutManager);
        RecurringRecyclerView.setAdapter(RecurringAdapter);

        RecurringAdapter.setOnItemClickListener(new RecurringAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                taskList.get(position);
            }

            @Override
            public void onCompleteClick(int position) {
                updateCheckBoxStatus(position);
            }
        });



    }

    private class connectionattempt extends AsyncTask<String, String, String> {
        private String serv_response = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            p = new ProgressDialog(viewRecurringTasks.this);
            p.setMessage("Retrieving data from server...");
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
                url = new URL("https://138.68.23.145/returnRecurReminders.php");
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

            /* Setting content type to json (commented out for now, since add_reminder.php
             * will just get the json information from a json_data parameter) */
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
            String bodyparam = "inpdate=" + inpdata;

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
            String serv_response;
            System.out.println("We are in postexecute\n");
            System.out.println("Here is the response: " + result + "\n");
            super.onPostExecute(result);
            serv_response = result;
            System.out.println("Here is the responseresult string: " + serv_response + "\n");
            p.dismiss();

            /* Checking to see if there was an error */
            String errorstr = "error";
            boolean errorresult = false;
            if (!serv_response.isEmpty()) {
                errorresult = serv_response.toLowerCase().contains(errorstr.toLowerCase());
            }

            if(errorresult)
            {
                String shownmsg = "There was an error retrieiving the tasks! Here is the error: " + serv_response;
                Toast errormsg = Toast.makeText(getApplicationContext(), shownmsg, Toast.LENGTH_SHORT);
                errormsg.show();
            }

            else {
                //create taskList and populate the arraylist of DailyTask objects
                createTaskList(serv_response);
                //build the recyclerview so we have one!
                buildRecurringRecyclerView();
            }
        }
    }


    private class updatestatus extends AsyncTask<String, String, String> {
        private String serv_response = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            p = new ProgressDialog(viewRecurringTasks.this);
            p.setMessage("Contacting server...");
            p.setIndeterminate(false);
            p.setCancelable(false);
            p.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String rid = strings[0];
            String compstatus = strings[1];

            System.out.println("The inputted data: " + rid + "\n");

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
                url = new URL("https://138.68.23.145/updateReminderStatus.php");
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

            /* Setting content type to json (commented out for now, since add_reminder.php
             * will just get the json information from a json_data parameter) */
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
            String bodyparam = "rid=" + rid + "&compstatus=" + compstatus;

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
            String serv_response;
            System.out.println("We are in postexecute\n");
            System.out.println("Here is the response: " + result + "\n");
            super.onPostExecute(result);
            serv_response = result;
            System.out.println("Here is the responseresult string: " + serv_response + "\n");
            p.dismiss();

            /* Checking to see if it was a success */
            String successstr = "success";
            boolean successresult = false;
            if (!serv_response.isEmpty()) {
                successresult = serv_response.toLowerCase().contains(successstr.toLowerCase());
            }

            if(successresult)
            {
                //String shownmsg = "Successfully updated status on server!";
                Toast succmsg = Toast.makeText(getApplicationContext(), serv_response, Toast.LENGTH_SHORT);
                succmsg.show();
            }

            else
            {
                String shownmsg = "Could not update status on server!";
                Toast succmsg = Toast.makeText(getApplicationContext(), shownmsg, Toast.LENGTH_SHORT);
                succmsg.show();
            }
        }
    }

}

