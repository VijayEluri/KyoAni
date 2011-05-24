package net.hisme.masaki.kyoani.services;

import net.hisme.masaki.kyoani.R;
import net.hisme.masaki.kyoani.models.AnimeCalendar;
import net.hisme.masaki.kyoani.models.AnimeOne;
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

public class WidgetUpdater extends Service {
    public Schedule getNextSchedule() {
        try {
            return new AnimeOne(this).getNextSchedule();
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
        AppWidgetManager widget_manager = AppWidgetManager
                .getInstance(WidgetUpdater.this);
        Schedule schedule = getNextSchedule();
        String schedule_string;

        if (schedule != null) {
            log("next schedule found.");
            schedule_string = buildWidgetString(schedule);
        } else {
            log("next schedule not found.");
            schedule_string = getText(R.string.no_schedule).toString();
        }

        RemoteViews views;
        ComponentName widget_class;

        log("Update 1x1");
        views = new RemoteViews(getPackageName(), R.layout.widget_layout_1x1);
        views.setTextViewText(R.id.next_log, schedule_string);
        widget_class = new ComponentName(this, KyoAniWidget1.class);
        widget_manager.updateAppWidget(widget_class, views);

        log("Update 2x2");
        views = new RemoteViews(getPackageName(), R.layout.widget_layout_2x2);
        views.setTextViewText(R.id.next_log, schedule_string);
        widget_class = new ComponentName(this, KyoAniWidget2.class);
        widget_manager.updateAppWidget(widget_class, views);

        if (schedule != null) {
            setupNext(schedule);
        }
        stopSelf();
    }

    protected void setupNext(Schedule schedule) {
        log("setup alart for next show");
        Intent intent = new Intent(WidgetUpdater.this, WidgetUpdater.class);
        PendingIntent pending_intent = PendingIntent.getService(
                WidgetUpdater.this, 0, intent, 0);

        AnimeCalendar calendar = schedule.getStart();
        calendar.add(AnimeCalendar.MINUTE, 3);
        log(String.format("scheduled to update widget at %s", calendar
                .toString()));
        AlarmManager alarm_manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarm_manager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                pending_intent);
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
        android.util.Log.d("KyoAni", "[WidgetUpdater] " + message);
    }
}
