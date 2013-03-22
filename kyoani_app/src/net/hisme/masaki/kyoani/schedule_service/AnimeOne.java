package net.hisme.masaki.kyoani.schedule_service;

import net.hisme.masaki.kyoani.App;
import net.hisme.masaki.kyoani.models.Account;
import net.hisme.masaki.kyoani.models.AnimeCalendar;
import net.hisme.masaki.kyoani.models.Schedule;
import net.hisme.masaki.kyoani.models.Schedules;
import net.hisme.masaki.kyoani.schedule_service.exception.LoginFailureException;
import net.hisme.masaki.kyoani.schedule_service.exception.NetworkUnavailableException;
import net.hisme.masaki.kyoani.schedule_service.exception.SessionExpiredException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.HttpHost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.cookie.Cookie;
import java.net.UnknownHostException;
import java.io.OutputStreamWriter;
import java.io.BufferedWriter;
import java.io.StringReader;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.xml.sax.InputSource;

import java.util.regex.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.io.IOException;
import java.io.FileNotFoundException;

/**
 * adapter for AnimeOne 
 * @author masarakki
 */
public class AnimeOne extends Base {
  public static final String HOST = "anime.biglobe.ne.jp";
  public static final String REGISTER_URI = "https://anime.biglobe.ne.jp/regist/regist_user";
  private static final String MYPAGE_URI = "http://anime.biglobe.ne.jp/program/myprogram";
  private static final String LOGIN_URI = "/login/login_ajax";
  private static final String LOGOUT_URI = "/login/logout_ajax";
  private static final String SESSION_FILE_NAME = "_session";
  private static final String SESSION_KEY_NAME = "PHPSESSID";

  public static final int LOGIN_OK = 0;
  public static final int LOGIN_NG = 1;
  public static final int NETWORK_ERROR = 2;
  public HttpHost http_host, https_host = null;

  public AnimeOne() {
    this.http = getClient();
    this.http_host = new HttpHost(HOST, 80, "http");
    this.https_host = new HttpHost(HOST, 443, "https");
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

  public Schedules reloadSchedules() throws LoginFailureException, NetworkUnavailableException {
    log("Reload Schedule");
    if (hasSessionID() || login()) {
      log("Use Session");
      try {
        Schedules schedules = mypage();
        if (schedules.save()) {
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
  public Schedules fetchSchedules() {
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

  /**
   * parse MyPage HTML
   * 
   * @param html
   *          HTML response body
   * @return ArrayList of Schedule
   */
  public Schedules parseMyPage(String html) {
    Schedules schedules = new Schedules();

    Matcher match = Pattern.compile(
        "(<div class=\"w220Box program program2 marginLeft10px\">.*</div>).*<div class=\"w220Box program marginLeft10px\">",
        Pattern.DOTALL | Pattern.MULTILINE | Pattern.UNICODE_CASE | Pattern.UNIX_LINES).matcher(html);

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
        return schedules;
      }
    } catch (org.xml.sax.SAXException e) {
      org.xml.sax.SAXParseException ex = (org.xml.sax.SAXParseException) e;
      log("row:" + ex.getLineNumber() + "   col: " + ex.getColumnNumber());
    } catch (Exception e) {
      log(e.toString());
    }
    throw new RuntimeException("Parse Error");
  }

  /**
   * access to MyPage
   * 
   * @return schedules
   * @throws SessionExpiredException
   */
  public Schedules mypage() throws SessionExpiredException {
    return mypage(3);
  }

  /**
   * access to MyPage with retry
   * 
   * @param retry_count
   * @return ArrayList of Schedule
   * @throws SessionExpiredException
   */
  public Schedules mypage(int retry_count) throws SessionExpiredException {
    try {
      String html = httpGet(MYPAGE_URI);
      return parseMyPage(html);
    } catch (RuntimeException e) {
      if (retry_count > 0) {
        try {
          if (login()) {
            return mypage(retry_count - 1);
          }
        } catch (NetworkUnavailableException ex) {}
        log("MyPage: Giveup; relogin failure");
        throw new SessionExpiredException();
      } else {
        log("MyPage: Giveup;");
        throw new SessionExpiredException();
      }
    }
  }

  public void logout() {
    try {
      HttpPost post = new HttpPost(LOGOUT_URI);
      http.execute(https_host, post);
      post.abort();
    } catch (Exception e) {
      log(e.toString());
    }
  }

  @SuppressWarnings("unused")
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
    return login(App.li.getAccount());
  }

  /**
   * execution of login
   * 
   * @param account
   * @return success or failure
   * @throws NetworkUnavailableException
   */
  public boolean login(Account account) throws NetworkUnavailableException {
    log("Login Start");
    boolean result = false;
    try {
      http = getClient();
      HttpPost post = new HttpPost(LOGIN_URI);

      ArrayList<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
      params.add(new BasicNameValuePair("mail", account.getUserName()));
      params.add(new BasicNameValuePair("password", account.getPassword()));
      post.setEntity(new UrlEncodedFormEntity(params));
      http.execute(https_host, post);

      for (Cookie cookie : http.getCookieStore().getCookies()) {
        if (cookie.getName().equals(getSessionKeyName()))
          saveSessionID(cookie.getValue());
        if (cookie.getName().equals("user[id_nick]"))
          result = true;
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
    return !App.li.getAccount().isBlank();
  }

}
