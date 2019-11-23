package com.example.wildkarrde;

import android.content.Context;
import android.os.StrictMode;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.Arrays;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;

public class logout{
    public boolean deleteServerSide(Context inpcontext) throws IOException, CertificateException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {

        /* FOR DEVELOPMENT BUILDS ONLY! IF TIME USE AsyncTask INSTEAD! */
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        System.out.println("At beginning of connection attempt\n");

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
            return false;
        }

        String result;
        /* Forming the url to the registration php script */
        URL url = new URL("https://138.68.23.145/logout.php");


        /* FOR DEVELOPMENT PURPOSES ONLY!
        THIS ALLOWS ANY HOST NAME (ip hostname is being denied by javax.ssl and general
        ssl guideline) TO PASS THROUGH.
         */
        /*Null Host Name verifier code is from Noam's answer at:
        https://stackoverflow.com/questions/14619781/java-io-ioexception-hostname-was-not-verified
         */
        HttpsURLConnection.setDefaultHostnameVerifier(new NullHostNameVerifier());
        HttpsURLConnection urlConnection = null;

        try {
            urlConnection = (HttpsURLConnection) url.openConnection();
        }
        catch(IOException ex){
            ex.printStackTrace();
        }

        System.out.println("Opened connection ok\n");

        /* Gathering the SSLContext object information from the passed parameter and
        setting the SSLSocketFactory for the urlConnection*/
        SSLSocketFactory sf = customssl.getSocketFactory();
        urlConnection.setSSLSocketFactory(sf);

        System.out.println("Set socket factory ok\n");

        /* Retrieving the cookie from the shared preferences */
        cookie_storage cookie_retriever = new cookie_storage();
        String raw_cookie = cookie_retriever.get_cookie(inpcontext);

        /* Parsing the raw_cookie string to only get the PHPSESSID information before the ;*/
        String[] cookielist = raw_cookie.split(";");
        String cookie_value = cookielist[0];

        //System.out.println("Cookie value is: " + cookie_value + "\n");

        /* Setting the urlConnection's output option to true */
        urlConnection.setDoOutput(true);

        /* Setting the HTTP method to POST */
        urlConnection.setRequestMethod("POST");

       // Setting the cookie header
        urlConnection.setRequestProperty("Cookie", cookie_value);

        /* Connecting to the server */
        urlConnection.connect();

        /* Checking the http response */
        int httpstatus = urlConnection.getResponseCode();

        /* If the http response was not ok, then return an error string back to login activity */
        if(httpstatus != HttpURLConnection.HTTP_OK)
        {
            result = "HTTP response error!";
            return false;
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

        boolean logout_success;
        String successstr = "success";
        logout_success = result.toLowerCase().contains(successstr.toLowerCase());

        /* If success was found in the returned message, then */
        return logout_success;
    }
}
