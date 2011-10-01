package net.hisme.masaki.kyoani;

import android.app.Application;

public class App extends Application {
	public static App li;
	
	public App() {
		super();
		App.li = this;
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
