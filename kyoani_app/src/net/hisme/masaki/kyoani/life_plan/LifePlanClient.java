package net.hisme.masaki.kyoani.life_plan;

import java.util.ArrayList;
import java.util.List;

import net.arnx.jsonic.JSON;
import net.hisme.masaki.kyoani.models.AnimeCalendar;
import net.hisme.masaki.kyoani.models.Schedule;
import net.hisme.masaki.kyoani.models.Schedules;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import android.net.Uri;

public class LifePlanClient {
  private String client_id;
  private String client_secret;
  private String callback_url;

  public LifePlanClient(String app_key, String secret, String callback) {
    this.client_id = app_key;
    this.client_secret = secret;
    this.callback_url = callback;
  }

  public Uri.Builder getBasicUrlBuilder() {
    return new Uri.Builder().
        scheme("https").
        authority("life-plan.np-complete-doj.in").
        appendQueryParameter("client_id", this.client_id);
  }

  public String getAuthorizationUrl() {
    return getBasicUrlBuilder().
        path("/oauth/authorize").
        appendQueryParameter("redirect_uri", this.callback_url).
        appendQueryParameter("response_type", "code").build().toString();
  }

  public AccessToken getAccessToken(String code) {
    String url = getBasicUrlBuilder().path("/oauth/token").build().toString();
    List<NameValuePair> formparams = new ArrayList<NameValuePair>();
    formparams.add(new BasicNameValuePair("client_id", this.client_id));
    formparams.add(new BasicNameValuePair("client_secret", this.client_secret));
    formparams.add(new BasicNameValuePair("redirect_uri", this.callback_url));
    formparams.add(new BasicNameValuePair("grant_type", "authorization_code"));
    formparams.add(new BasicNameValuePair("code", code));
    HttpPost request = new HttpPost(url);

    try {
      UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, "UTF-8");
      request.setEntity(entity);
      DefaultHttpClient client = new DefaultHttpClient();
      HttpResponse response = client.execute(request);

      int status_code = response.getStatusLine().getStatusCode();
      if (status_code != 200) {
        throw new Exception(String.format("Error response: %d", status_code));
      }

      return JSON.decode(response.getEntity().getContent(), AccessToken.class);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  public AccessToken refresh(AccessToken token) {
    String url = getBasicUrlBuilder().path("/oauth/token").build().toString();
    List<NameValuePair> formparams = new ArrayList<NameValuePair>();
    formparams.add(new BasicNameValuePair("client_id", this.client_id));
    formparams.add(new BasicNameValuePair("client_secret", this.client_secret));
    formparams.add(new BasicNameValuePair("redirect_uri", this.callback_url));
    formparams.add(new BasicNameValuePair("grant_type", "refresh_token"));
    formparams.add(new BasicNameValuePair("refresh_token", token.getRefreshToken()));
    HttpPost request = new HttpPost(url);

    try {
      UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, "UTF-8");
      request.setEntity(entity);
      DefaultHttpClient client = new DefaultHttpClient();
      HttpResponse response = client.execute(request);

      int status_code = response.getStatusLine().getStatusCode();
      if (status_code != 200) {
        throw new Exception(String.format("Error response: %d", status_code));
      }

      return JSON.decode(response.getEntity().getContent(), AccessToken.class);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  public Schedules getSchedules(AccessToken token) {
    String url = getBasicUrlBuilder().path("/api/v1/programs").build().toString();
    HttpGet request = new HttpGet(url);
    request.addHeader("Authorization", "Bearer " + token.getAccessToken());
    Schedules schedules = new Schedules();
    try {
      HttpResponse response = new DefaultHttpClient().execute(request);
      int status_code = response.getStatusLine().getStatusCode();
      if (status_code != 200) {
        throw new Exception(String.format("Error response: %d", status_code));
      }
      ScheduleJson[] schedules_json = JSON.decode(response.getEntity().getContent(), ScheduleJson[].class);
      for (ScheduleJson schedule : schedules_json) {
        schedules.add(schedule.toSchedule());
      }
      return schedules;
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }

  }

  class ScheduleJson {
    private String channel;
    private String title;
    private int no;
    private AnimeCalendar start_at;

    public void setChannel(String channel) {
      this.channel = channel;
    }

    public void setTitle(String title) {
      this.title = title;
    }

    public void setNo(int no) {
      this.no = no;
    }

    public void setStartAt(int start_at) {
      this.start_at = new AnimeCalendar((long) start_at);
    }

    public String getChannel() {
      return this.channel;
    }

    public String getTitle() {
      return this.title;
    }

    public int getNo() {
      return this.no;
    }

    public AnimeCalendar getStartAt() {
      return start_at;
    }

    public Schedule toSchedule() {
      return new Schedule(channel, title, start_at);
    }

    public String toString() {
      return String.format("<Schedule channel=%s title=%s start_at=%s>", channel, title, start_at);
    }
  }

}
