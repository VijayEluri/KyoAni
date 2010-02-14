package net.hisme.masaki.kyoani.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetProvider;
import android.appwidget.AppWidgetManager;
import android.widget.RemoteViews;
import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.ComponentName;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.Log;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import net.hisme.masaki.kyoani.activities.MainActivity;

import net.hisme.masaki.kyoani.*;

public class KyoAniWidget extends AppWidgetProvider {
	private static AppWidgetManager manager;
	private static RemoteViews views;
	private static Context context;

	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		super.onUpdate(context, appWidgetManager, appWidgetIds);

		Intent intent = new Intent(context, MainActivity.class);
		for (int i = 0; i < appWidgetIds.length; i++) {
			int widgetId = appWidgetIds[i];
			PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
					intent, 0);
			RemoteViews _views = new RemoteViews(context.getPackageName(),
					R.layout.widget_layout);
			_views.setOnClickPendingIntent(R.id.next_log, pendingIntent);
			_views.setOnClickPendingIntent(R.id.widget_main, pendingIntent);
			appWidgetManager.updateAppWidget(widgetId, _views);
			updateAppWidget(context, appWidgetManager, _views);
		}
	}

	public static void updateAppWidget(Context _context,
			AppWidgetManager _manager, RemoteViews _views) {
		context = _context;
		manager = _manager;
		views = _views;

		Thread thread = new Thread(new Runnable() {
			public void run() {
				try {
					Account account = Account.load(context);
					AnimeOne anime_one = new AnimeOne(account);
					String schedule_str = new String();
					schedule_str = context.getText(R.string.login_failure)
							.toString();
					log("getSchedules");
					ArrayList<Schedule> schedules = anime_one
							.getSchedules(context);
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
					log("Update Widget View");
					manager.updateAppWidget(new ComponentName(context,
							KyoAniWidget.class), views);
				} catch (Exception e) {

				}
			}
		});
		log("Thread Start");
		thread.start();
	}

	private static void log(boolean b) {
		log(b ? "true" : "false");
	}

	private static void log(int n) {
		log(new Integer(n).toString());
	}

	private static void log(String str) {
		Log.d("KyoAni", "[KyoAniWidget] " + str);
	}
}
