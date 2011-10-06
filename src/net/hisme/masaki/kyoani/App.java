package net.hisme.masaki.kyoani;

import net.hisme.masaki.kyoani.models.Account;
import android.app.Application;

/**
 * @author masaki
 */
public class App extends Application {
	public static App li;

	private Account account;

	public App() {
		super();
		App.li = this;
	}

	public Account account() {
		if (account == null) {
			try {
				account = new Account();
			} catch (Account.BlankException e) {

			}
		}
		return account;
	}

	public static class Log {
		private static final String tag = "KyoAni";

		public static void i(String str) {
			android.util.Log.i(tag, str);
		}

		public static void d(String str) {
			android.util.Log.d(tag, str);
		}

		public static void e(String str) {
			android.util.Log.e(tag, str);
		}

		public static void w(String str) {
			android.util.Log.w(tag, str);
		}
	}
}
