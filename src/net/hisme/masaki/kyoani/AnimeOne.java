package net.hisme.masaki.kyoani;

import android.content.Context;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import javax.net.ssl.HttpsURLConnection;
import java.net.HttpURLConnection;
import java.net.URL;
import android.util.Log;
import java.util.Map;
import java.util.List;
import java.util.Hashtable;

public class AnimeOne {
	private Context context = null;
	private Account account = null;
	private String user_hash = null;
	private Hashtable<String, String> cookie = null;
	private static final String MYPAGE_URI = "http://anime.biglobe.ne.jp/program/myprogram";
	private static final String LOGIN_FORM = "https://anime.biglobe.ne.jp/login";
	private static final String LOGIN_URI = "https://anime.biglobe.ne.jp/login/login_ajax";
	private static final String COOKIE_FILE = "cookie.txt";

	public AnimeOne(Context context, Account account) {
		this.context = context;
		this.account = account;
		this.cookie = new Hashtable<String, String>();
		loadCookie();
	}

	public void mypage() {

	}

	private void loadCookie() {
		try {
			String line = null;
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					this.context.openFileInput(COOKIE_FILE)));
			while ((line = reader.readLine()) != null) {
				String[] hash = line.split("=");
				this.cookie.put(hash[0], hash[1]);
			}
			reader.close();
		} catch (java.io.FileNotFoundException e) {
		} catch (java.io.IOException e) {
		}
	}

	private void saveCookie() {
		try {
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
					this.context.openFileOutput(COOKIE_FILE, Context.MODE_PRIVATE)));
			for (Map.Entry<String, String> entry : this.cookie.entrySet()) {
				writer.write(entry.getKey() + "=" + entry.getValue() + "\n");
			}
			writer.flush();
			writer.close();
		} catch (java.io.FileNotFoundException e) {
		} catch (java.io.IOException e) {
		}
	}

	private String request(HttpURLConnection http, String method, String message) {
		String responseText = "";
		try {
			http.setRequestMethod(method);
			http.setDoInput(true);
			http.setDoOutput(true);
			String cookie_str = "";
			for (Map.Entry<String, String> cookie_entry : this.cookie.entrySet()) {
				cookie_str += cookie_entry.getKey() + "=" + cookie_entry.getValue()
						+ "; ";
			}
			Log.d("AnimeOne", "Cookie: " + cookie_str);
			http.addRequestProperty("Cookie", cookie_str);
			if (method.equals("POST")) {
				PrintWriter writer = new PrintWriter(http.getOutputStream());
				Log.d("AnimeOne", "message: " + message);
				writer.print(message);
				writer.flush();
				writer.close();
			} else {
				http.connect();
			}
			BufferedReader reader = new BufferedReader(new InputStreamReader(http
					.getInputStream()));
			String line = null;
			while ((line = reader.readLine()) != null) {
				responseText += line + "\n";
			}
			Log.d("AnimeOne", "RESPONSE: " + responseText);
			reader.close();

			List<String> set_cookies = http.getHeaderFields().get("set-cookie");

			if (set_cookies != null) {
				Log.d("AnimeOne", "Set-Cookie: " + set_cookies.toString());
				for (String set_cookie : set_cookies) {
					String[] hash = set_cookie.split("[=;]");
					this.cookie.put(hash[0], hash[1]);
				}
				saveCookie();
			}
		} catch (java.net.ProtocolException e) {
		} catch (java.io.IOException e) {
		}
		return responseText;
	}

	public boolean login() {
		String user_id = account.getUser();
		String password = account.getPassword();
		String message = "mail=" + user_id + "&password=" + password;
		boolean result = false;
		HttpsURLConnection http = null;
		try {
			/*
			 * http = (HttpsURLConnection) new URL(LOGIN_FORM) .openConnection();
			 * request(http, "GET", null); http.disconnect();
			 */
			http = (HttpsURLConnection) new URL(LOGIN_URI).openConnection();
			http.addRequestProperty("Referer", LOGIN_FORM);
			request(http, "POST", message);
			http.disconnect();

		} catch (java.io.IOException e) {

		} catch (Exception e) {
			Log.d("AnimeOne.login", e.toString());
		}
		return result;
	}
}
