package com.example.wildkarrde;

import android.content.Context;
import android.os.StrictMode;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;

public class loginhelper {

    public String connectionattempt(Context inpcontext, String inpattr[]) throws IOException, CertificateException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        /* FOR DEVELOPMENT BUILDS ONLY! IF TIME USE AsyncTask INSTEAD! */
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        System.out.println("At beginning of connection attempt\n");


        //Check to see if the username is actually an email
        //Source: PointerNull answer at: https://stackoverflow.com/questions/6119722/how-to-check-edittexts-text-is-email-address-or-not
        boolean isemail = android.util.Patterns.EMAIL_ADDRESS.matcher(inpattr[0]).matches();

        /* Creating a custom ssl contextobject by making an
        allowSelfSignedCerts object to call an getsslcontext
        function whick will retrieve a custom SSLContext object
        based on a self-signed certificate
         */
        allowSelfSignedCerts retrieve = new allowSelfSignedCerts();
        SSLContext customssl;
        customssl = retrieve.getsslcontext(inpcontext);

        if(customssl == null)
        {
            System.out.println("making a custom ssl context didn't work!");
            return("SSL error!");
        }

        String result;
        /* Forming the url to the registration php script */
        URL url = new URL("https://138.68.23.145/login.php");


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
        urlConnection.setRequestMethod("POST");

        /* Connecting to the server */
        urlConnection.connect();

        /* Prepping the output stream */
        OutputStream sender = urlConnection.getOutputStream();

        /* An output streamwriter to write data into the Output Stream */
        OutputStreamWriter bodyformer = new OutputStreamWriter(sender, "UTF-8");

        /* Writing the POST Request body */

        String bodyparam = null;
        /* If an email was entered in the first input box, then make POST request with email attribute */
        if(isemail)
        {
            bodyparam = "email=" + inpattr[0] + "&password=" + inpattr[1];
        }

        else
        {
            bodyparam = "user_name=" + inpattr[0] + "&password=" + inpattr[1];
        }


        /* Writing using output stream writer */
        bodyformer.write(bodyparam);

        /* Flushing the output stream writer */
        bodyformer.flush();

        /* Closing the output stream itself */
        sender.close();

        /* Checking the http response */
        int httpstatus = urlConnection.getResponseCode();

        /* If the http response was not ok, then return an error string back to login activity */
        if(httpstatus != HttpURLConnection.HTTP_OK)
        {
            result = "HTTP response error!";
            return result;
        }

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

        String cookie_data = null;

        boolean login_success;
        String successstr = "success";
        login_success = result.toLowerCase().contains(successstr.toLowerCase());


        /* If success was found in the returned message, then get the header data from Set-Cookie */
        if(login_success){
            /* Gathering the header data from the Set-Cookie header and
            saving it as raw data into a string
             */
            cookie_data = urlConnection.getHeaderField("Set-Cookie");

            /* Saving the raw cookie data in shared preferences for storage and retrieval from other
            connection functions
             */
            cookie_storage cookie_storer = new cookie_storage();

            cookie_storer.store_cookie(cookie_data, inpcontext);

            System.out.println("Cookie data stored is" + cookie_data + "\n");

            /* Once cookie is stored, then close the input stream and return the result*/
            response.close();

            return result;
        }

        /* Otherwise, just return whatever result string we have */
        else
        {
            response.close();
            return result;
        }
    }
}
