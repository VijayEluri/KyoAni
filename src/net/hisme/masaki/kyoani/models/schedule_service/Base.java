package net.hisme.masaki.kyoani.models.schedule_service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.hisme.masaki.kyoani.App;
import net.hisme.masaki.kyoani.models.AnimeCalendar;
import net.hisme.masaki.kyoani.models.Schedule;
import net.hisme.masaki.kyoani.models.ScheduleService;
import net.hisme.masaki.kyoani.utils.StringUtils;

import org.apache.http.ProtocolVersion;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpProtocolParams;

/**
 * @author masaki
 */
public abstract class Base implements ScheduleService {
  protected DefaultHttpClient http = null;
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
  protected DefaultHttpClient getClient() {
    BasicHttpParams params = new BasicHttpParams();
    int timeout = 0;
    HttpConnectionParams.setConnectionTimeout(params, timeout);
    HttpConnectionParams.setSoTimeout(params, timeout);
    HttpProtocolParams.setVersion(params, new ProtocolVersion("HTTP", 1, 2));
    HttpProtocolParams.setUserAgent(params, "net.hisme.masaki.kyoani.browser");

    return new DefaultHttpClient(params);
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
      BufferedReader reader = new BufferedReader(new InputStreamReader(App.li.openFileInput(getSessionFileName())));
      String session = reader.readLine();
      this.http.getCookieStore().addCookie(new BasicClientCookie(getSessionKeyName(), session));
      reader.close();
    } catch (FileNotFoundException e) {
      App.Log.d("Session File not exists");
    } catch (IOException e) {
      App.Log.d("IOException in loadSessionID()");
    }
  }

  protected void saveSessionID(String s) {
    try {
      BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(App.li.openFileOutput(getSessionFileName(), 0)));
      writer.write(s);
      writer.close();
    } catch (FileNotFoundException e) {

    } catch (IOException e) {

    }
  }

  /**
	 * 
	 */
  public ArrayList<Schedule> getSchedules() throws LoginFailureException,
      NetworkUnavailableException {
    if (this.needUpdate()) {
      return this.fetchSchedules();
    } else {
      return Schedule.loadSchedules();
    }
  }

  /**
   * send GET request and return response body
   * 
   * @param url
   *          URL
   * @return response body
   */
  public String httpGet(String url) {
    HttpGet get = new HttpGet(url);
    String body;
    try {
      body = StringUtils.getString(http.execute(get).getEntity().getContent());
      get.abort();
      return body;
    } catch (Exception e) {
      get.abort();
      throw new RuntimeException(e);
    }
  }

  public boolean needUpdate() {
    GregorianCalendar updated = updatedDate();
    return updated == null || AnimeCalendar.today().compareTo(updated) == 1;
  }

  public boolean clearUpdatedDate() {
    try {
      App.li.openFileOutput(DATE_FILE, 0).close();
      return true;
    } catch (Exception e) {}
    return false;
  }

  public GregorianCalendar updatedDate() {
    try {
      BufferedReader reader = new BufferedReader(new InputStreamReader(App.li.openFileInput(DATE_FILE)));
      String line = reader.readLine();
      reader.close();
      if (line != null) {
        Matcher m = Pattern.compile("([0-9]{4})-([0-9]{2})-([0-9]{2})").matcher(line);
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
