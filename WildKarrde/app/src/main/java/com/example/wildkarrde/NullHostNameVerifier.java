/* THE FOLLOWING CODE IS BASED ON CODE FROM Noam's ANSWER AT:
https://stackoverflow.com/questions/14619781/java-io-ioexception-hostname-was-not-verified
 */

package com.example.wildkarrde;

import android.util.Log;

import javax.net.ssl.HostnameVerifier ;
import javax.net.ssl.SSLSession;

public class NullHostNameVerifier implements HostnameVerifier{
    @Override
    public boolean verify(String hostname, SSLSession session) {
        Log.i("HostnameDisp", "Approving certificate for " + hostname);
        return true;
    }
}
