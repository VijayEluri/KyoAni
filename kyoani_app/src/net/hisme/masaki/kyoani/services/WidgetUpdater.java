package net.hisme.masaki.kyoani.services;

import net.hisme.masaki.kyoani.App;
import net.hisme.masaki.kyoani.R;
import net.hisme.masaki.kyoani.activities.MainActivity;
import net.hisme.masaki.kyoani.models.Schedule;
import net.hisme.masaki.kyoani.widget.KyoAniWidget1;
import net.hisme.masaki.kyoani.widget.KyoAniWidget2;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.IBinder;
import android.widget.RemoteViews;

/**
 * service to update widgets
 * 
 * @author masarakki
 * 
 */
public class WidgetUpdater extends Service {

  /**
   * @param schedule
   *          schedule
   * @return text of widget
   */
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
    Schedule schedule = App.li.nextSchedule();
    String schedule_string;

    if (schedule != null) {
      schedule_string = buildWidgetString(schedule);
    } else {
      schedule_string = getText(R.string.no_schedule).toString();
    }

    RemoteViews views;
    ComponentName widget_class;

    PendingIntent pending_intent = PendingIntent.getActivity(WidgetUpdater.this, 0,
        new Intent(WidgetUpdater.this, MainActivity.class), 0);

    log("Update Widget 1x1");
    views = new RemoteViews(getPackageName(), R.layout.widget_layout_1x1);
    views.setTextViewText(R.id.next_log, schedule_string);
    views.setOnClickPendingIntent(R.id.widget_main, pending_intent);
    widget_class = new ComponentName(this, KyoAniWidget1.class);
    widget_manager.updateAppWidget(widget_class, views);

    log("Update Widget 2x2");
    views = new RemoteViews(getPackageName(), R.layout.widget_layout_2x2);
    views.setTextViewText(R.id.next_log, schedule_string);
    views.setOnClickPendingIntent(R.id.widget_main, pending_intent);
    widget_class = new ComponentName(this, KyoAniWidget2.class);
    widget_manager.updateAppWidget(widget_class, views);

    App.li.resetServices();

    stopSelf();
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
