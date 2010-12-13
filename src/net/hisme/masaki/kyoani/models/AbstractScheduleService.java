package net.hisme.masaki.kyoani.models;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import net.hisme.masaki.kyoani.models.ScheduleService.NetworkUnavailableException;

import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;

import android.content.Context;
import android.util.Log;

public abstract class AbstractScheduleService implements ScheduleService {
    protected DefaultHttpClient http = null;
    protected Context context = null;

    /**
     * get session file name
     * 
     * @return stored session file name
     */
    abstract protected String getSessionFileName();

    /**
     * get key name of cookie for session
     * 
     * @return key name
     */
    abstract protected String getSessionKeyName();

    /**
     * initialize http client
     */
    protected void initHttpClient() {
        BasicHttpParams params = new BasicHttpParams();
        int timeout = 0;
        HttpConnectionParams.setConnectionTimeout(params, timeout);
        HttpConnectionParams.setSoTimeout(params, timeout);

        this.http = new DefaultHttpClient(params);
        loadSessionID();
    }

    /**
     * return cookie has session id or not
     * 
     * @return has?
     */
    protected boolean hasSessionID() {
        String cookie_key = getSessionKeyName();
        for (Cookie cookie : this.http.getCookieStore().getCookies()) {
            if (cookie.getName().equals(cookie_key)) {
                return true;
            }
        }
        return false;
    }

    /**
     * load SessionID from stored file
     * 
     */
    protected void loadSessionID() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    this.context.openFileInput(getSessionFileName())));
            String session = reader.readLine();
            this.http.getCookieStore().addCookie(
                    new BasicClientCookie(getSessionKeyName(), session));
            reader.close();
        } catch (FileNotFoundException e) {
            debug("Session File not exists");
        } catch (IOException e) {
            debug("IOException in loadSessionID()");
        }
    }

    protected void saveSessionID(String s) {
        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                    context.openFileOutput(getSessionFileName(), 0)));
            writer.write(s);
            writer.close();
        } catch (FileNotFoundException e) {

        } catch (IOException e) {

        }
    }

    private void debug(String str) {
        Log.d("KyoAni", "[AbstractScheduleService] " + str);
    }
}
