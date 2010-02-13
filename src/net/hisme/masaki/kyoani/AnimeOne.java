package net.hisme.masaki.kyoani;

import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.HttpResponse;
import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.cookie.Cookie;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import java.lang.StringBuffer;
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
import android.util.Log;
import android.content.Context;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.io.IOException;
import java.io.FileNotFoundException;

public class AnimeOne {
	Account account = null;
	DefaultHttpClient http;

	private static final String MYPAGE_URI = "http://anime.biglobe.ne.jp/program/myprogram";
	private static final String LOGIN_FORM = "https://anime.biglobe.ne.jp/login/index";
	private static final String LOGIN_URI = "https://anime.biglobe.ne.jp/login/login_ajax";
	private static final String LOGOUT_URI = "https://anime.biglobe.ne.jp/login/logout_ajax";
	private static final int BUFFSIZE = 1024;

	public static final String DATE_FILE = "updated.txt";

	public static final int LOGIN_OK = 0;
	public static final int LOGIN_NG = 1;
	public static final int NETWORK_ERROR = 2;

	public AnimeOne(Account account) {
		this.account = account;
		BasicHttpParams params = new BasicHttpParams();
		int timeout = 0;
		HttpConnectionParams.setConnectionTimeout(params, timeout);
		HttpConnectionParams.setSoTimeout(params, timeout);

		this.http = new DefaultHttpClient(params);
	}

	public void nextAnime() {

	}

	public ArrayList<Schedule> getSchedules(Context context) {
		log("Get Schedules");
		if (needUpdate(context)) {
			log("Need Update");
			return reloadSchedules(context);
		} else {
			log("Cached");
			return Schedule.loadSchedules(context);
		}
	}

	public ArrayList<Schedule> reloadSchedules(Context context) {
		log("Reload Schedule");
		if (login() == LOGIN_OK) {
			log("Login Success");
			ArrayList<Schedule> schedules = mypage();
			if (Schedule.saveSchedules(context, schedules)) {
				log("Update cached date");
				GregorianCalendar today = today();
				try {
					BufferedWriter writer = new BufferedWriter(
							new OutputStreamWriter(context.openFileOutput(
									DATE_FILE, 0)));
					writer.write(String.format("%04d-%02d-%02d", today
							.get(Calendar.YEAR), today.get(Calendar.MONTH) + 1,
							today.get(Calendar.DAY_OF_MONTH)));
					writer.flush();
					writer.close();
				} catch (FileNotFoundException e) {
					log("FileNotFound in write updated date");
				} catch (IOException e) {
					log("IOException in write updated date");
				}
			}
			return schedules;
		} else {
			return null;
		}
	}

	public ArrayList<Schedule> mypage() {
		return mypage(3);
	}

	public GregorianCalendar today() {
		GregorianCalendar now = new GregorianCalendar();
		now.add(GregorianCalendar.HOUR_OF_DAY, -6);
		return new GregorianCalendar(now.get(Calendar.YEAR), now
				.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
	}

	public boolean needUpdate(Context context) {
		GregorianCalendar updated = updatedDate(context);
		if (updated == null)
			return true;

		return today().compareTo(updated) == 1 ? true : false;
	}

	public GregorianCalendar updatedDate(Context context) {
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

	public ArrayList<Schedule> mypage(int retry_count) {
		log("MyPage Start");
		ArrayList<Schedule> result = new ArrayList<Schedule>();
		boolean retry = true;
		try {
			HttpGet get = new HttpGet(MYPAGE_URI);
			HttpResponse response = http.execute(get);
			HttpEntity entity = response.getEntity();

			BufferedReader reader = new BufferedReader(new InputStreamReader(
					entity.getContent()));
			StringBuffer responseText = new StringBuffer();

			char[] buf = new char[BUFFSIZE];
			int read_size = 0;
			while ((read_size = reader.read(buf, 0, BUFFSIZE)) != -1) {
				responseText = responseText.append(buf, 0, read_size);
			}
			get.abort();

			Pattern pattern = Pattern
					.compile(
							"(<div class=\"w220Box program program2 marginLeft10px\">.*</div>).*<div class=\"w220Box program marginLeft10px\">",
							Pattern.DOTALL | Pattern.MULTILINE
									| Pattern.UNICODE_CASE | Pattern.UNIX_LINES);
			Matcher match = pattern.matcher(new String(responseText));
			if (match.find()) {
				retry = false;
				NodeList tmp;
				String body = match.group(1);
				body = body.replace("&", "&amp;");
				DocumentBuilder builder = DocumentBuilderFactory.newInstance()
						.newDocumentBuilder();
				Document doc = builder.parse(new InputSource(new StringReader(
						body)));
				Matcher date_matcher = Pattern.compile("([0-9]+)月([0-9]+)")
						.matcher(
								nodeMapString(
										doc.getElementsByTagName("span")
												.item(0)).get(0));
				date_matcher.find();

				int month = Integer.parseInt(date_matcher.group(1));
				int day = Integer.parseInt(date_matcher.group(2));

				NodeList td_list = doc.getElementsByTagName("td");
				final int TDNUMS = 4;
				for (int i = 0; i < td_list.getLength() / TDNUMS; i++) {
					tmp = td_list.item(i * TDNUMS + 1).getChildNodes();
					for (int j = 0; j < tmp.getLength(); j++) {
						ArrayList<String> values = null;
						if (tmp.item(j).getNodeName().compareTo("img") == 0
								&& tmp.item(j).getAttributes().getNamedItem(
										"alt").getNodeValue()
										.compareTo("ネット配信") != 0) {
							String channel = "";
							String name = "";
							String start = "";

							values = nodeMapString(td_list.item(i * TDNUMS + 0));
							if (values.size() == 1) {
								Matcher m = Pattern.compile("([0-9:]+) +(.+)")
										.matcher(values.get(0));
								m.find();
								start = m.group(1);
								channel = m.group(2);
							}
							values = nodeMapString(td_list.item(i * TDNUMS + 2));
							name = values.get(0);
							Schedule schedule = new Schedule(channel, name,
									start);
							log(schedule.toString());
							result.add(schedule);

							break;
						}
					}
				}
			}
			log("Parse Finish: " + result.size() + " items found.");
		} catch (org.apache.http.client.ClientProtocolException e) {
			log(e.toString());
		} catch (java.io.IOException e) {
			log(e.toString());
		} catch (ParserConfigurationException e) {
			log(e.toString());
		} catch (org.xml.sax.SAXException e) {
			org.xml.sax.SAXParseException ex = (org.xml.sax.SAXParseException) e;
			log("row:" + ex.getLineNumber() + "   col: " + ex.getColumnNumber());
		}
		log("MyPage Finish");
		if (retry && retry_count > 0) {
			return mypage(retry_count - 1);
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

	public int login() {
		log("Login Start");
		int result;
		try {
			HttpGet get = new HttpGet(LOGIN_FORM);
			HttpResponse res = http.execute(get);
			get.abort();

			String user_id = account.getUser();
			String password = account.getPassword();

			result = LOGIN_NG;
			HttpPost post = new HttpPost(LOGIN_URI);
			post.addHeader("Referer", LOGIN_FORM);
			post.addHeader("X-Requested-With", "XMLHttpRequest");
			post.addHeader("User-Agent", "Mozilla/5.0(AnimeOneBrowser)");
			post.addHeader("Content-Type",
					"application/x-www-form-urlencoded; charset=UTF-8");
			post.setEntity(new StringEntity("mail=" + user_id + "&password="
					+ password));

			http.execute(post);
			for (Cookie cookie : http.getCookieStore().getCookies()) {
				if (cookie.getName().equals("user[id_nick]")) {
					result = LOGIN_OK;
					break;
				}
			}
			post.abort();
		} catch (Exception e) {
			log(e.toString());
			result = NETWORK_ERROR;
		}
		log("Login Finish");
		return result;
	}

	private ArrayList<String> nodeMapString(Node node) {
		ArrayList<String> res = new ArrayList<String>();
		_nodeToString(res, node);
		return res;
	}

	private void _nodeToString(ArrayList<String> list, Node node) {
		Pattern p = Pattern
				.compile("[ 　\t\n\r]+", Pattern.DOTALL | Pattern.MULTILINE
						| Pattern.UNICODE_CASE | Pattern.UNIX_LINES);
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

	private void log(boolean b) {
		log(b ? "true" : "false");
	}

	private void log(int n) {
		log(new Integer(n).toString());
	}

	private void log(String str) {
		Log.d("KyoAni", "[AnimeOne] " + str);
	}
}
