package net.hisme.masaki.kyoani;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Account {
	private String user_id = null;
	private String password = null;

	public static Account load(Context context) throws BlankException {
		SharedPreferences pref = PreferenceManager
				.getDefaultSharedPreferences(context);
		String user_id = pref.getString("account", null);
		String password = pref.getString("password", null);
		if (user_id == null || password == null || user_id == ""
				|| password == "") {
			throw new BlankException();
			// return null;
		}
		return new Account(user_id, password);
	}

	public Account(String user_id, String password) {
		this.user_id = user_id;
		this.password = password;
	}

	public String getUser() {
		return this.user_id;
	}

	public String getPassword() {
		return this.password;
	}

	public static class BlankException extends Exception {
	}
}
