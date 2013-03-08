package net.hisme.masaki.kyoani.services;

import net.hisme.masaki.kyoani.App;
import net.hisme.masaki.kyoani.R;
import net.hisme.masaki.kyoani.activities.MainActivity;
import net.hisme.masaki.kyoani.models.AnimeCalendar;
import net.hisme.masaki.kyoani.models.Schedule;
import net.hisme.masaki.kyoani.widget.KyoAniWidget1;
import net.hisme.masaki.kyoani.widget.KyoAniWidget2;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.IBinder;
import android.widget.RemoteViews;

/**
 * ウィジェットを更新するサービス
 * 
 * @author masaki
 * 
 */
public class WidgetUpdater extends Service {
  /**
   * 次のスケジュールを取得する
   * 
   * @return
   */
  public Schedule getNextSchedule() {
    try {
      return App.li.getScheduleService().getNextSchedule();
    } catch (Exception e) {
      log(e.toString());
      return null;
    }
  }

  public String buildWidgetString(Schedule schedule) {
    StringBuffer str_buf = new StringBuffer();
    str_buf.append(schedule.getChannel());
    str_buf.append("\n");
    str_buf.append(schedule.getStartString());
    str_buf.append("\n");
    str_buf.append(schedule.getName());
    str_buf.append("\n");
    return new String(str_buf);
  }

  @Override
  public void onCreate() {
    log("started.");
    AppWidgetManager widget_manager = AppWidgetManager.getInstance(WidgetUpdater.this);
    Schedule schedule = getNextSchedule();
    String schedule_string;

    if (schedule != null) {
      log("next schedule found.");
      schedule_string = buildWidgetString(schedule);
      setupNext(schedule);
    } else {
      log("next schedule not found.");
      schedule_string = getText(R.string.no_schedule).toString();
      setupNext();

    }

    RemoteViews views;
    ComponentName widget_class;

    PendingIntent pending_intent = PendingIntent.getActivity(WidgetUpdater.this, 0,
        new Intent(WidgetUpdater.this, MainActivity.class), 0);

    log("Update 1x1");
    views = new RemoteViews(getPackageName(), R.layout.widget_layout_1x1);
    views.setTextViewText(R.id.next_log, schedule_string);
    views.setOnClickPendingIntent(R.id.widget_main, pending_intent);
    widget_class = new ComponentName(this, KyoAniWidget1.class);
    widget_manager.updateAppWidget(widget_class, views);

    log("Update 2x2");
    views = new RemoteViews(getPackageName(), R.layout.widget_layout_2x2);
    views.setTextViewText(R.id.next_log, schedule_string);
    views.setOnClickPendingIntent(R.id.widget_main, pending_intent);
    widget_class = new ComponentName(this, KyoAniWidget2.class);
    widget_manager.updateAppWidget(widget_class, views);

    stopSelf();
  }

  /**
   * 次のスケジュールが開始した時の更新を設定する
   * 
   * @param schedule
   */
  protected void setupNext(Schedule schedule) {
    setupUpdater(schedule);
    setupNotification(schedule);
  }

  /**
   * 次の更新の設定をする
   * 
   * @param schedule
   */
  protected void setupUpdater(Schedule schedule) {
    log("setup alart for next show");
    Intent intent = new Intent(WidgetUpdater.this, WidgetUpdater.class);
    intent.putExtra("ToastMessage", String.format("%sで%sがはじまります", schedule.getChannel(), schedule.getName()));
    PendingIntent pending_intent = PendingIntent.getService(WidgetUpdater.this, 0, intent, 0);

    AnimeCalendar calendar = schedule.getStart();
    calendar.add(AnimeCalendar.MINUTE, 3);
    log(String.format("scheduled to update widget at %s", calendar.toString()));
    AlarmManager alarm_manager = (AlarmManager) getSystemService(ALARM_SERVICE);
    alarm_manager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pending_intent);
  }

  /**
   * 次のアニメが始まる前にバイブレーションさせる設定をする
   * 
   * @param schedule
   */
  protected void setupNotification(Schedule schedule) {
    log("setup notification for next show");
    Intent intent = new Intent(WidgetUpdater.this, NotificationService.class);
    PendingIntent pending_intent = PendingIntent.getService(WidgetUpdater.this, 0, intent, 0);

    AnimeCalendar calendar = schedule.getStart();
    calendar.add(AnimeCalendar.MINUTE, -2);
    log(String.format("scheduled to notification at %s", calendar.toString()));
    AlarmManager alarm_manager = (AlarmManager) getSystemService(ALARM_SERVICE);
    alarm_manager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pending_intent);
  }

  /**
   * 次の日の更新を設定する
   */
  protected void setupNext() {
    log("setup alart for next day");
    Intent intent = new Intent(WidgetUpdater.this, DailyUpdater.class);
    PendingIntent pending_intent = PendingIntent.getService(WidgetUpdater.this, 0, intent, 0);

    AnimeCalendar calendar = AnimeCalendar.tomorrow();
    log(String.format("scheduled to daily update at %s", calendar.toString()));
    AlarmManager alarm_manager = (AlarmManager) getSystemService(ALARM_SERVICE);
    alarm_manager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pending_intent);
  }

  @Override
  public IBinder onBind(Intent intent) {
    return null;
  }

  public void onDestroy() {
    log("destroyed.");
    super.onDestroy();
  }

  public void log(String message) {
    App.Log.d("[WidgetUpdater] " + message);
  }
}
