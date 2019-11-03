package com.example.wildkarrde;

import android.os.StrictMode;

import javax.net.ssl.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.lang.String;

public class registerHelper {
    public String connectionattempt(SSLContext inpcontext, String inpattr[]) throws IOException {
        /* FOR DEVELOPMENT BUILDS ONLY! IF TIME USE AsyncTask INSTEAD! */
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        System.out.println("At beginning of connection attempt\n");

        String result;
        /* Forming the url to the registration php script */
        URL url = new URL("https://138.68.23.145/registration.php");


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
        }
        catch(IOException ex){
            return null;
        }



        System.out.println("Opened connection ok\n");

        /* Gathering the SSLContext object information from the passed parameter */
        SSLContext finalsslobj;
        finalsslobj = inpcontext;

        SSLSocketFactory sf = finalsslobj.getSocketFactory();
        urlConnection.setSSLSocketFactory(sf);

        System.out.println("Set socket factory ok\n");

        /* The following code below is based on answers from both Ricardo, as well as NateS's reply
        from the following link:
        https://stackoverflow.com/questions/44305351/send-data-in-request-body-using-httpurlconnection?rq=1
         */

        /* Setting the urlConnection's output option to true */
        urlConnection.setDoOutput(true);

        /* Setting the HTTP method to POST */
        urlConnection.setRequestMethod("POST");

        /* Connecting to the server */
        urlConnection.connect();

        /* Prepping the output stream */
        OutputStream sender = urlConnection.getOutputStream();

        /* An output streamwriter to write data into the Output Stream */
        OutputStreamWriter bodyformer = new OutputStreamWriter(sender, "UTF-8");

        /* Writing the POST Request body */
        String bodyparam = "email=" + inpattr[0] + "&user_name=" + inpattr[1] + "&password=" + inpattr[2];

        /* Writing using output stream writer */
        bodyformer.write(bodyparam);

        /* Flushing the output stream writer */
        bodyformer.flush();

        /* Closing the output stream itself */
        sender.close();

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

        return result;
    }
}
