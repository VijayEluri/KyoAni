package net.hisme.masaki.kyoani.services;

import net.hisme.masaki.kyoani.R;
import net.hisme.masaki.kyoani.models.AnimeOne;
import net.hisme.masaki.kyoani.models.Schedule;
import net.hisme.masaki.kyoani.widget.KyoAniWidget1;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.IBinder;
import android.widget.RemoteViews;

public class WidgetUpdater extends Service {
    public String buildWidgetString() {
        try {
            AnimeOne anime_one = new AnimeOne(this);
            Schedule schedule = anime_one.getNextSchedule();

            String schedule_str = getText(R.string.no_schedule).toString();
            if (schedule != null) {
                StringBuffer str_buf = new StringBuffer();
                str_buf.append(schedule.getChannel());
                str_buf.append("\n");
                str_buf.append(schedule.getStartString());
                str_buf.append("\n");
                str_buf.append(schedule.getName());
                str_buf.append("\n");
                schedule_str = new String(str_buf);
            }
            return schedule_str;
        } catch (Exception e) {
            log(e.toString());

        }
        return "";
    }

    @Override
    public void onCreate() {
        AppWidgetManager widget_manager = AppWidgetManager
                .getInstance(WidgetUpdater.this);
        RemoteViews views = new RemoteViews(getPackageName(),
                R.layout.widget_layout_1x1);
        views.setTextViewText(R.id.next_log, buildWidgetString());
        ComponentName thisWidget = new ComponentName(this, KyoAniWidget1.class);
        widget_manager.updateAppWidget(thisWidget, views);
        stopSelf();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onDestroy() {
        log("onDestroy");
        super.onDestroy();
    }

    public void log(String message) {
        android.util.Log.d("KyoAni", "[WidgetUpdater] " + message);
    }
}
