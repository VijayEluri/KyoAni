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
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.StringReader;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.xml.sax.InputSource;
import java.util.regex.*;
import java.util.ArrayList;
import android.util.Log;

public class AnimeOne {
	Account account = null;
	DefaultHttpClient http;
	private static final String MYPAGE_URI = "http://anime.biglobe.ne.jp/program/myprogram";
	private static final String LOGIN_FORM = "https://anime.biglobe.ne.jp/login";
	private static final String LOGIN_URI = "https://anime.biglobe.ne.jp/login/login_ajax";
	private static final String LOGOUT_URI = "https://anime.biglobe.ne.jp/login/logout_ajax";
	private static final int BUFFSIZE = 1024;

	public AnimeOne(Account account) {
		this.account = account;
		BasicHttpParams params = new BasicHttpParams();
		int timeout = 10000;
		HttpConnectionParams.setConnectionTimeout(params, timeout);
		HttpConnectionParams.setSoTimeout(params, timeout);
		
		this.http = new DefaultHttpClient(params);
	}

	public ArrayList<String[]> mypage() {
		ArrayList<String[]> result = new ArrayList<String[]>();
		try {
			HttpGet get = new HttpGet(MYPAGE_URI);
			HttpResponse response = http.execute(get);
			HttpEntity entity = response.getEntity();

			BufferedReader reader = new BufferedReader(new InputStreamReader(entity
					.getContent()));
			StringBuffer responseText = new StringBuffer();

			char[] buf = new char[BUFFSIZE];
			int read_size = 0;
			while ((read_size = reader.read(buf, 0, BUFFSIZE)) != -1) {
				responseText = responseText.append(buf, 0, read_size);
			}
			get.abort();
			log("GET MyPage");

			Pattern pattern = Pattern
					.compile(
							"(<div class=\"w220Box program program2 marginLeft10px\">.*</div>).*<div class=\"w220Box program marginLeft10px\">",
							Pattern.DOTALL | Pattern.MULTILINE | Pattern.UNICODE_CASE
									| Pattern.UNIX_LINES);
			Matcher match = pattern.matcher(new String(responseText));
			log("Parse MyPage");
			if (match.find()) {
				retry = false;
				NodeList tmp;
				String body = match.group(1);
				DocumentBuilder builder = DocumentBuilderFactory.newInstance()
						.newDocumentBuilder();
				Document doc = builder.parse(new InputSource(new StringReader(body)));
				NodeList td_list = doc.getElementsByTagName("td");
				final int TDNUMS = 4;
				for (int i = 0; i < td_list.getLength() / TDNUMS; i++) {
					tmp = td_list.item(i * TDNUMS + 1).getChildNodes();
					for (int j = 0; j < tmp.getLength(); j++) {
						ArrayList<String> values = null;
						if (tmp.item(j).getNodeName().compareTo("img") == 0
								&& tmp.item(j).getAttributes().getNamedItem("alt")
										.getNodeValue().compareTo("ネット配信") != 0) {
							String[] entry = new String[3];
							// String schedule = nodeToString(td_list.item(i * TDNUMS + 0));
							values = nodeMapString(td_list.item(i * TDNUMS + 0));
							if (values.size() == 1) {
								Matcher m = Pattern.compile("([0-9:]+) +(.+)").matcher(
										values.get(0));
								m.find();
								entry[1] = m.group(1);
								entry[2] = m.group(2);
							}
							values = nodeMapString(td_list.item(i * TDNUMS + 2));
							entry[0] = values.get(0);
							result.add(entry);
							break;
						}
					}
				}
			}
		} catch (org.apache.http.client.ClientProtocolException e) {
			log(e.toString());
		} catch (java.io.IOException e) {
			log(e.toString());
		} catch (ParserConfigurationException e) {
			log(e.toString());
		} catch (org.xml.sax.SAXException e) {
			log(e.toString());
		}
		if (retry && retry_count > 0) {
			log("Retry MyPage");
			return mypage(retry_count - 1);
		}
		log("MyPage Finish");
		return result;
	}

	public void logout() {
		log("Logout Start");
		try {
			HttpPost post = new HttpPost(LOGOUT_URI);
			http.execute(post);
			post.abort();
		} catch (Exception e) {
			log(e.toString());
		}
		log("Logout Finish");
	}

	public boolean login() {
		log("Login Start");
		boolean result = false;
		// logout();
		try {
			HttpGet get = new HttpGet(LOGIN_FORM);
			HttpResponse res = http.execute(get);
			get.abort();
			log("GET LoginForm");

			String user_id = account.getUser();
			String password = account.getPassword();

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
					result = true;
					break;
				}
			}
			post.abort();
			log("POST Login Form");
		} catch (org.apache.http.client.ClientProtocolException e) {
			log(e.toString());
		} catch (java.io.IOException e) {
			log(e.toString());
		} catch (Exception e) {
			log(e.toString());
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
		Pattern p = Pattern.compile("[ 　\t\n\r]+", Pattern.DOTALL
				| Pattern.MULTILINE | Pattern.UNICODE_CASE | Pattern.UNIX_LINES);
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
		Log.d("AnimeOne", str);
	}
}
