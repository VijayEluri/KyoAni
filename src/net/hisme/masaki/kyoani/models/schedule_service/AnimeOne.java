package net.hisme.masaki.kyoani.models.schedule_service;

import net.hisme.masaki.kyoani.App;
import net.hisme.masaki.kyoani.models.AnimeCalendar;
import net.hisme.masaki.kyoani.models.Schedule;
import net.hisme.masaki.kyoani.models.ScheduleService;
import net.hisme.masaki.kyoani.models.ScheduleService.LoginFailureException;
import net.hisme.masaki.kyoani.models.ScheduleService.NetworkUnavailableException;
import net.hisme.masaki.kyoani.utils.StringUtils;
import net.hisme.masaki.kyoani.models.schedule_service.SessionExpiredException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.cookie.Cookie;
import java.lang.StringBuffer;
import java.net.UnknownHostException;
import java.io.OutputStreamWriter;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.BufferedReader;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.xml.sax.InputSource;

import java.util.regex.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.io.IOException;
import java.io.FileNotFoundException;

public class AnimeOne extends Base {

  public static final String REGISTER_URI = "https://anime.biglobe.ne.jp/regist/regist_user";
  private static final String MYPAGE_URI = "http://anime.biglobe.ne.jp/program/myprogram";
  private static final String LOGIN_FORM = "https://anime.biglobe.ne.jp/login/";
  private static final String LOGIN_URI = "https://anime.biglobe.ne.jp/login/login_ajax";
  private static final String LOGOUT_URI = "https://anime.biglobe.ne.jp/login/logout_ajax";
  private static final String SESSION_FILE_NAME = "_session";
  private static final String SESSION_KEY_NAME = "PHPSESSID";

  private static final int BUFFSIZE = 1024;

  public static final int LOGIN_OK = 0;
  public static final int LOGIN_NG = 1;
  public static final int NETWORK_ERROR = 2;

  public AnimeOne() {
    this.http = get_client();
    loadSessionID();
  }

  @Override
  protected String getSessionFileName() {
    return SESSION_FILE_NAME;
  }

  @Override
  protected String getSessionKeyName() {
    return SESSION_KEY_NAME;
  }

  public Schedule getNextSchedule() throws LoginFailureException, NetworkUnavailableException {
    AnimeCalendar now = new AnimeCalendar();
    for (Schedule schedule : getSchedules()) {
      if (now.compareTo(schedule.getStart()) == -1) {
        return schedule;
      }
    }
    return null;
  }

  public ArrayList<Schedule> reloadSchedules() throws LoginFailureException, NetworkUnavailableException {
    log("Reload Schedule");
    if (hasSessionID() || login()) {
      log("Use Session");
      try {
        ArrayList<Schedule> schedules = mypage();
        if (Schedule.saveSchedules(schedules)) {
          log("Update cached date");
          AnimeCalendar today = new AnimeCalendar();
          try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(App.li.openFileOutput(DATE_FILE, 0)));
            writer.write(String.format("%04d-%02d-%02d", today.get(Calendar.YEAR), today.get(Calendar.MONTH) + 1, today.get(Calendar.DAY_OF_MONTH)));
            writer.flush();
            writer.close();
          } catch (FileNotFoundException e) {
            log("FileNotFound in write updated date");
          } catch (IOException e) {
            log("IOException in write updated date");
          }
        }
        return schedules;
      } catch (SessionExpiredException e) {
        if (login())
          return reloadSchedules();
      }
    }
    return null;
  }

  @Override
  public ArrayList<Schedule> fetchSchedules() {
    log("fetchSchedule");
    try {
      return reloadSchedules();
    } catch (LoginFailureException e) {
      e.printStackTrace();
    } catch (NetworkUnavailableException e) {
      e.printStackTrace();
    }
    return null;
  }

  public ArrayList<Schedule> mypage() throws SessionExpiredException {
    return mypage(3);
  }

  public ArrayList<Schedule> parseMyPage(String html) throws SessionExpiredException {
    ArrayList<Schedule> schedules = new ArrayList<Schedule>();

    Pattern pattern = Pattern.compile(
        "(<div class=\"w220Box program program2 marginLeft10px\">.*</div>).*<div class=\"w220Box program marginLeft10px\">",
        Pattern.DOTALL | Pattern.MULTILINE | Pattern.UNICODE_CASE | Pattern.UNIX_LINES);
    Matcher match = pattern.matcher(html);
    try {
      if (match.find()) {
        NodeList tmp;
        String body = match.group(1);
        body = body.replace("&", "&amp;");
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document doc = builder.parse(new InputSource(new StringReader(body)));
        Matcher date_matcher = Pattern.compile("([0-9]+)月([0-9]+)").matcher(nodeMapString(doc.getElementsByTagName("span").item(0)).get(0));
        date_matcher.find();

        NodeList td_list = doc.getElementsByTagName("td");
        final int TDNUMS = 4;
        for (int i = 0; i < td_list.getLength() / TDNUMS; i++) {
          tmp = td_list.item(i * TDNUMS + 1).getChildNodes();
          for (int j = 0; j < tmp.getLength(); j++) {
            ArrayList<String> values = null;
            if (tmp.item(j).getNodeName().compareTo("img") == 0
                && tmp.item(j).getAttributes().getNamedItem("alt").getNodeValue().compareTo("ネット配信") != 0) {
              String channel = "";
              String name = "";
              String start = "";

              values = nodeMapString(td_list.item(i * TDNUMS + 0));
              if (values.size() == 1) {
                Matcher m = Pattern.compile("([0-9:]+) +(.+)").matcher(values.get(0));
                m.find();
                start = m.group(1);
                channel = m.group(2);
              }
              values = nodeMapString(td_list.item(i * TDNUMS + 2));
              name = values.get(0);
              schedules.add(new Schedule(channel, name, start));
              break;
            }
          }
        }
      } else {
        throw new SessionExpiredException();
      }
    } catch (org.xml.sax.SAXException e) {
      org.xml.sax.SAXParseException ex = (org.xml.sax.SAXParseException) e;
      log("row:" + ex.getLineNumber() + "   col: " + ex.getColumnNumber());
      throw new RuntimeException(e);
    } catch (Exception e) {
      log(e.toString());
      throw new RuntimeException(e);
    }
    return schedules;
  }

  public ArrayList<Schedule> mypage(int retry_count) throws SessionExpiredException {
    log("MyPage Start");
    ArrayList<Schedule> result = new ArrayList<Schedule>();
    boolean retry = true;
    try {
      String html = httpGet(MYPAGE_URI);
      return parseMyPage(html);
    } catch (SessionExpiredException e) {

    } catch (RuntimeException e) {
      if (retry && retry_count > 0) {
        return mypage(retry_count - 1);
      }
    }
    return result;
  }

  public void logout() {
    try {
      HttpPost post = new HttpPost(LOGOUT_URI);
      http.execute(post);
      post.abort();
    } catch (Exception e) {
      log(e.toString());
    }
  }

  private String getSessionID() {
    for (Cookie cookie : this.http.getCookieStore().getCookies()) {
      if (cookie.getName().equals("PHPSESSID")) {
        return cookie.getValue();
      }
    }
    return "";
  }

  @Override
  public boolean login() throws NetworkUnavailableException {
    log("Login Start");
    boolean result = false;
    try {
      http = get_client();
      HttpHost host = new HttpHost("anime.biglobe.ne.jp", 443, "https");
      HttpPost post = new HttpPost("/login/login_ajax");

      ArrayList<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
      params.add(new BasicNameValuePair("mail", App.li.account().username()));
      params.add(new BasicNameValuePair("password", App.li.account().password()));
      post.setEntity(new UrlEncodedFormEntity(params));
      post.setHeader("Accept-Language", "ja");
      post.setHeader("X-requested-With", "XMLHttpRequest");
      post.setHeader("Content-Type", "application/x-www-form-urlencoded");
      post.setHeader("Accept", "text/javascript, application/javascript, */*");
      App.Log.d("post headers");
      for (Header header : post.getAllHeaders()) {
        App.Log.d(header.toString());
      }

      HttpResponse response = http.execute(host, post);
      App.Log.d("response headers");
      for (Header header : response.getAllHeaders()) {
        App.Log.d(header.toString());
      }
      String str;
      BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
      while ((str = reader.readLine()) != null) {
        App.Log.d(str);
      }

      for (Cookie cookie : http.getCookieStore().getCookies()) {
        if (cookie.getName().equals(getSessionKeyName()))
          saveSessionID(cookie.getValue());
        if (cookie.getName().equals("user[id_nick]"))
          result = true;
      }
      App.Log.d("cookies");
      for (Cookie cookie : http.getCookieStore().getCookies()) {
        App.Log.d(cookie.getName() + ": " + cookie.getValue());
      }

      post.abort();

      if (result)
        return result;

    } catch (ClientProtocolException e) {
      e.printStackTrace();
    } catch (UnknownHostException e) {
      throw new NetworkUnavailableException();
    } catch (IOException e) {
      throw new NetworkUnavailableException();
    }
    return false;
  }

  private ArrayList<String> nodeMapString(Node node) {
    ArrayList<String> res = new ArrayList<String>();
    _nodeToString(res, node);
    return res;
  }

  private void _nodeToString(ArrayList<String> list, Node node) {
    Pattern p = Pattern.compile("[ 　\t\n\r]+", Pattern.DOTALL | Pattern.MULTILINE | Pattern.UNICODE_CASE | Pattern.UNIX_LINES);
    switch (node.getNodeType()) {
    case Node.TEXT_NODE:
      Matcher match = p.matcher(node.getNodeValue());
      if (match.replaceAll("").compareTo("") != 0) {
        list.add(match.replaceAll(" "));
      }
      break;
    default:
      NodeList nl = node.getChildNodes();
      for (int i = 0; i < nl.getLength(); i++) {
        _nodeToString(list, nl.item(i));
      }
      break;
    }
  }

  @SuppressWarnings("unused")
  private void log(boolean b) {
    log(b ? "true" : "false");
  }

  @SuppressWarnings("unused")
  private void log(int n) {
    log(new Integer(n).toString());
  }

  private void log(String str) {
    App.Log.d("[AnimeOne] " + str);
  }

  @Override
  protected boolean isAccountPresent() {
    return !App.li.account().is_blank();
  }

}
