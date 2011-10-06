package net.hisme.masaki.kyoani.models;

import net.hisme.masaki.kyoani.App;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * アカウント情報
 * 
 * @author masaki
 * 
 */
public class Account {
	private String user_id = null;
	private String password = null;

	/**
	 * create Account instance with Context and load
	 * 
	 * @throws BlankException
	 *             アカウント情報が設定されていない場合
	 */
	public Account() throws BlankException {
		this.load();
	}

	/**
	 * load account information from SharedPreference
	 * 
	 * @param context
	 * @return success?
	 * @throws BlankException
	 */
	public boolean load() throws BlankException {
		SharedPreferences pref = PreferenceManager
				.getDefaultSharedPreferences(App.li);
		user_id = pref.getString("account", null);
		password = pref.getString("password", null);
		if (user_id == null || password == null || user_id == ""
				|| password == "") {
			throw new BlankException();

		}
		return true;
	}

	/**
	 * get user_id
	 * 
	 * @return user_id
	 */
	public String getUser() {
		return this.user_id;
	}

	/**
	 * get password
	 * 
	 * @return password
	 */
	public String getPassword() {
		return this.password;
	}

	/**
	 * アカウント情報が設定されていないときに発生する例外
	 * 
	 * @author masaki
	 * 
	 */
	public static class BlankException extends Exception {
		private static final long serialVersionUID = 1L;
	}
}
