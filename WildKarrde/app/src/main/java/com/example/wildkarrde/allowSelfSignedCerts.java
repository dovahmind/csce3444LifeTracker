package com.example.wildkarrde;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import javax.net.ssl.*;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.*;
import java.security.KeyStore;

import android.content.Context;
import android.widget.Toast;

/* The following is based on an example from the, "Unknown certificate authority",
at the following link: https://developer.android.com/training/articles/security-ssl.html#java
and the AssetManager retrieval is based on the following example:
https://www.concretepage.com/android/android-assetmanager-example-to-load-image-from-assets-folder
 */


public class allowSelfSignedCerts{
    public SSLContext getsslcontext(Context inpcontext) throws CertificateException, KeyStoreException, NoSuchAlgorithmException, KeyManagementException, IOException, InvalidAlgorithmParameterException {

        if(inpcontext == null)
        {
            System.out.println("Couldn't get context!\n");
        }

        /* AssetManager used to retreive any files stored in assets */
        AssetManager initmng = inpcontext.getAssets();

        /* Making an input stream and then putting the contents of the
        self-signed certificate from assets into it
         */
        InputStream fileinp = null;
        fileinp = initmng.open("138_68_23_145.crt");


        CertificateFactory cf;
        InputStream caInput;



        try{
            cf = CertificateFactory.getInstance("X.509");
        }

        catch(CertificateException ex1){
            System.out.println("error creating cert instance!\n");
            return null;
        }

        caInput = new BufferedInputStream(fileinp);
        Certificate ca;
        try {
            ca = cf.generateCertificate(caInput);
            System.out.println("ca=" + ((X509Certificate) ca).getSubjectDN());
        }
        finally {
            try {
                caInput.close();
            }
            catch(IOException ex3)
            {
                System.out.println("Couldn't close cainputstream!\n");
                return null;
            }

        }

        String certalias = ((X509Certificate) ca).getSubjectX500Principal().getName();
        // Create a KeyStore containing our trusted CAs

        System.out.println("The cert alias is: " + certalias + ".");
        String keyStoreType = KeyStore.getDefaultType();

        KeyStore keyStore = KeyStore.getInstance(keyStoreType);

        keyStore.load(null, null);

        keyStore.setCertificateEntry("ca", ca);

        // Create a TrustManager that trusts the CAs in our KeyStore
        String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();

        TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);

        tmf.init(keyStore);

        // Create an SSLContext that uses our TrustManager
        SSLContext context = SSLContext.getInstance("TLS");

        context.init(null, tmf.getTrustManagers(), null);

        System.out.println("Made it to end of allowselfsignedcerts\n");

        /* Finally, return the context back to the activity that called it */
        return context;
    }
}


