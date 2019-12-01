package com.example.wildkarrde;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.*;

@TargetApi(Build.VERSION_CODES.CUPCAKE)
public class BackgroundTask extends AsyncTask<String, Void, String> {
    Context ctx;
    BackgroundTask(Context ctx){
        this.ctx = ctx;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    protected String doInBackground(String...params) {
        String rect_url = "127.0.0.1";// Reccuring Task: url specific sites that should point to the PHP scripts that allow for the information input into the server
        //String dailyt_url = "127.0.0.1"; //Daily Task: url specifc sites that...
        String method = params[0];//Once this function is called, it will check the first parameter for the method specified
        if(method.equals("AddR")){//if a recurring task is called, follow this code block
            String name = params[1];
            String date = params[2];
            String location = params[3];
            String description = params[4];
            String time = params[5];
            try {
                URL url = new URL(rect_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                OutputStream OS = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));
                String data = URLEncoder.encode("name", "UTF-8") +" = "+URLEncoder.encode(name, "UTF-8") +"&"+
                        URLEncoder.encode("date", "UTF-8") +" = "+URLEncoder.encode(date, "UTF-8") +"&"+
                        URLEncoder.encode("location", "UTF-8") +" = "+URLEncoder.encode(location, "UTF-8") +"&"+
                        URLEncoder.encode("description", "UTF-8") +" = "+URLEncoder.encode(description, "UTF-8") +"&"+
                        URLEncoder.encode("time", "UTF-8") +" = "+URLEncoder.encode(time, "UTF-8");

                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                OS.close();
                InputStream IS = httpURLConnection.getInputStream();
                IS.close();
                return "Recurring Task Added!";
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String result) {
        Toast.makeText(ctx, result, Toast.LENGTH_LONG).show();
    }

    public void execute(String method, EditText name, EditText date, EditText location, EditText description, EditText time) {
    }
}
