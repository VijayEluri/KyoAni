package net.hisme.masaki.kyoani;

import net.hisme.masaki.kyoani.models.*;
import net.hisme.masaki.kyoani.schedule_service.AnimeOne;
import net.hisme.masaki.kyoani.schedule_service.ScheduleService;
import net.hisme.masaki.kyoani.schedule_service.exception.LoginFailureException;
import net.hisme.masaki.kyoani.schedule_service.exception.NetworkUnavailableException;
import net.hisme.masaki.kyoani.services.DailyUpdater;
import net.hisme.masaki.kyoani.services.NotificationService;
import net.hisme.masaki.kyoani.services.WidgetUpdater;
import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Intent;
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

  public Schedules getSchedules() {
    if (schedules == null) {
      schedules = Schedules.load();
    }
    return schedules;
  }

  public ScheduleService getScheduleService() {
    return new AnimeOne();
  }

  public void reload() throws LoginFailureException, NetworkUnavailableException {
    schedules = getScheduleService().reloadSchedules();
    schedules.save();
    updateWidgets();
  }

  public Schedule nextSchedule() {
    return getSchedules().next();
  }

  public void updateWidgets() {
    Intent intent = new Intent(this, WidgetUpdater.class);
    startService(intent);
  }

  public void resetServices() {
    Schedule schedule = nextSchedule();
    if (schedule != null) {
      setupNextWidgetUpdater(schedule);
      setupNextNotification(schedule);
    } else {
      setupNextDailyUpdater();
    }
  }

  /**
   * setup update timer
   * 
   * @param schedule
   */
  private void setupNextWidgetUpdater(Schedule schedule) {
    Log.d("setup WidgetUpdater");
    Intent intent = new Intent(this, WidgetUpdater.class);
    PendingIntent pending_intent = PendingIntent.getService(this, 0, intent, 0);

    AnimeCalendar calendar = schedule.getStart();
    calendar.add(AnimeCalendar.MINUTE, 3);
    Log.d(String.format("scheduled to update widget at %s", calendar.toString()));
    AlarmManager alarm_manager = (AlarmManager) getSystemService(ALARM_SERVICE);
    alarm_manager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pending_intent);
  }

  /**
   * setup notification timer
   * 
   * @param schedule
   */
  private void setupNextNotification(Schedule schedule) {
    Log.d("setup Notification");
    Intent intent = new Intent(this, NotificationService.class);
    PendingIntent pending_intent = PendingIntent.getService(this, 0, intent, 0);

    AnimeCalendar calendar = schedule.getStart();
    calendar.add(AnimeCalendar.MINUTE, -2);
    Log.d(String.format("scheduled to notification at %s", calendar.toString()));
    AlarmManager alarm_manager = (AlarmManager) getSystemService(ALARM_SERVICE);
    alarm_manager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pending_intent);
  }

  /**
   * setup daily update timer
   */
  private void setupNextDailyUpdater() {
    Log.d("setup DailyUpdater");
    Intent intent = new Intent(this, DailyUpdater.class);
    PendingIntent pending_intent = PendingIntent.getService(this, 0, intent, 0);

    AnimeCalendar calendar = AnimeCalendar.tomorrow();
    Log.d(String.format("scheduled to daily update at %s", calendar.toString()));
    AlarmManager alarm_manager = (AlarmManager) getSystemService(ALARM_SERVICE);
    alarm_manager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pending_intent);
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
