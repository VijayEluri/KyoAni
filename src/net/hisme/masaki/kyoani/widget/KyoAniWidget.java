package net.hisme.masaki.kyoani.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetProvider;
import android.appwidget.AppWidgetManager;
import android.widget.RemoteViews;
import android.content.Intent;
import android.content.Context;
import android.content.ComponentName;
import android.util.Log;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import net.hisme.masaki.kyoani.activities.MainActivity;
import net.hisme.masaki.kyoani.models.AnimeOne;
import net.hisme.masaki.kyoani.models.Schedule;

import net.hisme.masaki.kyoani.*;

public class KyoAniWidget extends AppWidgetProvider {
	protected final int widget_layout = 0;

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		super.onUpdate(context, appWidgetManager, appWidgetIds);
		new UpdateThread(context, getView(context), appWidgetManager,
				appWidgetIds).run();
	}

	protected int layout_id() {
		return 0;
	}

	protected RemoteViews getView(Context context) {
		return new RemoteViews(context.getPackageName(), layout_id());
	}

	private class UpdateThread extends Thread {
		private Context context;
		private RemoteViews views;
		private AppWidgetManager manager;
		private int[] widgetIds;

		public void log(String str) {
			Log.d("KyoAni", "[Update] " + str);
		}

		public UpdateThread(Context context, RemoteViews views,
				AppWidgetManager manager, int[] widgetIds) {
			this.context = context;
			this.views = views;
			this.manager = manager;
			this.widgetIds = widgetIds;
		}

		public void run() {
			try {
				AnimeOne anime_one = new AnimeOne(context);
				String schedule_str = new String();
				schedule_str = context.getText(R.string.login_failure)
						.toString();
				log("getSchedules");
				ArrayList<Schedule> schedules = anime_one.getSchedules(context);
				if (schedules != null) {
					schedule_str = context.getText(R.string.no_schedule)
							.toString();
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

				views.setTextViewText(R.id.next_log, schedule_str);
				Intent intent = new Intent(context, MainActivity.class);
				PendingIntent pendingIntent = PendingIntent.getActivity(
						context, 0, intent, 0);
				views.setOnClickPendingIntent(R.id.widget_main, pendingIntent);
				for (int widgetId : widgetIds) {
					manager.updateAppWidget(widgetId, views);
				}
			} catch (Exception e) {

			}
		}
	}
}