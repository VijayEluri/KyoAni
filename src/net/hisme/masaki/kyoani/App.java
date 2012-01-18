package net.hisme.masaki.kyoani;

import net.hisme.masaki.kyoani.models.Account;
import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

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

  private Account loadAccount() {
    SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(App.li);
    String user_id = pref.getString("account", "");
    String password = pref.getString("password", "");
    return new Account(user_id, password);
  }

  public void resetAccount() {
    this.account = null;
  }

  public Account getAccount() {
    if (account == null) {
      account = loadAccount();
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

  public static class BlankAccontError extends RuntimeException {
    private static final long serialVersionUID = 1L;

  }
}
