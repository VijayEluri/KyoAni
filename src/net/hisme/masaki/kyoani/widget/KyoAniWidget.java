package net.hisme.masaki.kyoani.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetProvider;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import java.util.ArrayList;
import java.util.GregorianCalendar;

import net.hisme.masaki.kyoani.R;
import net.hisme.masaki.kyoani.activities.MainActivity;
import net.hisme.masaki.kyoani.models.AnimeOne;
import net.hisme.masaki.kyoani.models.Schedule;

abstract public class KyoAniWidget extends AppWidgetProvider {
    protected final int widget_layout = 0;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
            int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        log("onUpdate");
        update(context, appWidgetManager);

    }

    abstract protected int getLayoutId();

    protected RemoteViews getView(Context context) {
        return new RemoteViews(context.getPackageName(), getLayoutId());
    }

    public void log(String message) {
        android.util.Log.d("KyoAni", "KyoAniWidget0 " + message);
    }

    protected String getSchduleString(Context context) {
        String schedule_str = new String();
        try {
            AnimeOne anime_one = new AnimeOne(context);
            schedule_str = context.getText(R.string.login_failure).toString();
            log("getSchedules");
            ArrayList<Schedule> schedules = anime_one.getSchedules();
            if (schedules != null) {
                schedule_str = context.getText(R.string.no_schedule).toString();
                for (Schedule schedule : schedules) {
                    GregorianCalendar now = new GregorianCalendar();

                    if (now.compareTo(schedule.getStart()) == -1) {
                        StringBuffer str_buf = new StringBuffer();
                        str_buf.append(schedule.getChannel());
                        str_buf.append("\n");
                        str_buf.append(schedule.getStartString());
                        str_buf.append("\n");
                        str_buf.append(schedule.getName());
                        str_buf.append("\n");
                        schedule_str = new String(str_buf);
                        break;
                    }
                }
            }

        } catch (Exception e) {

        }
        return schedule_str;
    }

    protected String buildWidgetString(Context context) {
        try {
            AnimeOne anime_one = new AnimeOne(context);
            Schedule schedule = anime_one.getNextSchedule();

            String schedule_str = context.getText(R.string.no_schedule)
                    .toString();
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

    public void update(Context context, AppWidgetManager manager) {
        RemoteViews views = getView(context);
        views.setTextViewText(R.id.next_log, buildWidgetString(context));
        ComponentName widget_class = new ComponentName(context, getClass());
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                intent, 0);
        views.setOnClickPendingIntent(R.id.widget_main, pendingIntent);
        manager.updateAppWidget(widget_class, views);
    }
}