package net.hisme.masaki.kyoani.models;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    public static final String DATE_FILE = "updated.txt";

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
     * @return account is present?
     */
    abstract protected boolean isAccountPresent();

    /**
     * サービスに接続してスケジュールリストを取得する
     * 
     * @return スケジュールのリスト
     */
    abstract public ArrayList<Schedule> fetchSchedules();

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

    protected void setContext(Context context) {
        this.context = context;
    }

    protected Context getContext() {
        return this.context;
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
            log("Session File not exists");
        } catch (IOException e) {
            log("IOException in loadSessionID()");
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

    private void log(String str) {
        Log.d("KyoAni", "[AbstractScheduleService] " + str);
    }

    public boolean needUpdate() {
        GregorianCalendar updated = updatedDate();
        if (updated == null)
            return true;

        return AnimeCalendar.today().compareTo(updated) == 1 ? true : false;
    }

    public GregorianCalendar updatedDate() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    context.openFileInput(DATE_FILE)));
            String line = reader.readLine();
            reader.close();
            if (line != null) {
                Matcher m = Pattern.compile("([0-9]{4})-([0-9]{2})-([0-9]{2})")
                        .matcher(line);
                if (m.find()) {
                    int year = Integer.parseInt(m.group(1));
                    int month = Integer.parseInt(m.group(2)) - 1;
                    int day = Integer.parseInt(m.group(3));
                    return new GregorianCalendar(year, month, day, 0, 0, 0);
                }
            }
            return null;
        } catch (FileNotFoundException e) {
            return null;
        } catch (IOException e) {
            return null;
        }
    }


}
