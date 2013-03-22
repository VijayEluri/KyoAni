package net.hisme.masaki.kyoani;

import net.hisme.masaki.kyoani.models.*;
import net.hisme.masaki.kyoani.schedule_service.AnimeOne;
import net.hisme.masaki.kyoani.schedule_service.ScheduleService;
import net.hisme.masaki.kyoani.schedule_service.exception.LoginFailureException;
import net.hisme.masaki.kyoani.schedule_service.exception.NetworkUnavailableException;
import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * @author masarakki
 */
public class App extends Application {
  public static App li;
  public static final String schedules_file = "list.obj";
  private Schedules schedules;
  private Account account;

  public App() {
    super();
    App.li = this;
    schedules = Schedules.load();
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

  public ScheduleService getScheduleService() {
    return new AnimeOne();
  }

  public void reload() throws LoginFailureException, NetworkUnavailableException {
    getScheduleService().reloadSchedules();
  }

  public Schedule nextSchedule() {
    // schedules.next();
    Schedules schedules = Schedules.load();
    if (schedules != null) {
      return schedules.next();
    }
    return null;
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
