package net.hisme.masaki.kyoani;

import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.HttpResponse;
import org.apache.http.entity.StringEntity;
import org.apache.http.cookie.Cookie;
import java.io.InputStreamReader;
import android.util.Log;

public class AnimeOne {
	Account account = null;
	DefaultHttpClient http;
	private static final String MYPAGE_URI = "http://anime.biglobe.ne.jp/program/myprogram";
	private static final String LOGIN_FORM = "https://anime.biglobe.ne.jp/login";
	private static final String LOGIN_URI = "https://anime.biglobe.ne.jp/login/login_ajax";

	public AnimeOne(Account account) {
		this.account = account;
		this.http = new DefaultHttpClient();
	}

	public void mypage() {
		HttpGet get = new HttpGet(MYPAGE_URI);
	}

	private boolean isLogined() {
		boolean result = false;
		return result;
	}

	public boolean login() {
		boolean result = false;
		try {
			HttpGet get = new HttpGet(LOGIN_FORM);
			http.execute(get);

			String user_id = account.getUser();
			String password = account.getPassword();
			HttpPost post = new HttpPost(LOGIN_URI);
			post.addHeader("Content-Type", "application/x-www-form-urlencoded");
			post.addHeader("Referer", LOGIN_FORM);
			post.setEntity(new StringEntity("mail=" + user_id + "&password="
					+ password));
			http.execute(post);

			for (Cookie cookie : http.getCookieStore().getCookies()) {
				if (cookie.getName().equals("user[id_nick]")) {
					result = true;
					break;
				}
			}
		} catch (org.apache.http.client.ClientProtocolException e) {
			log(e.toString());
		} catch (java.io.IOException e) {
			log(e.toString());
		}
		log("result = " + result);
		return result;
	}

	private void log(String str) {
		Log.d("AnimeOne", str);
	}
}
