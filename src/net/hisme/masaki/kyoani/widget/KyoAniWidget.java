package net.hisme.masaki.kyoani.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetProvider;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import net.hisme.masaki.kyoani.App;
import net.hisme.masaki.kyoani.R;
import net.hisme.masaki.kyoani.activities.MainActivity;
import net.hisme.masaki.kyoani.models.Schedule;
import net.hisme.masaki.kyoani.models.schedule_service.AnimeOne;

abstract public class KyoAniWidget extends AppWidgetProvider {
  protected final int widget_layout = 0;

  @Override
  public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
    super.onUpdate(context, appWidgetManager, appWidgetIds);
    update(context, appWidgetManager);

  }

  abstract protected int getLayoutId();

  protected RemoteViews getView(Context context) {
    return new RemoteViews(context.getPackageName(), getLayoutId());
  }

  protected String buildWidgetString(Context context) {
    try {
      AnimeOne anime_one = new AnimeOne();
      Schedule schedule = anime_one.getNextSchedule();

      String schedule_str = context.getText(R.string.no_schedule).toString();
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
      App.Log.d(e.toString());

    }
    return "";
  }

  public void update(Context context, AppWidgetManager manager) {
    RemoteViews views = getView(context);
    views.setTextViewText(R.id.next_log, buildWidgetString(context));
    ComponentName widget_class = new ComponentName(context, getClass());
    Intent intent = new Intent(context, MainActivity.class);
    PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
    views.setOnClickPendingIntent(R.id.widget_main, pendingIntent);
    manager.updateAppWidget(widget_class, views);
  }
}