package net.hisme.masaki.kyoani.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetProvider;
import android.appwidget.AppWidgetManager;
import android.widget.RemoteViews;
import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.ComponentName;
import android.preference.PreferenceManager;
import android.util.Log;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import net.hisme.masaki.kyoani.*;

public class KyoAniWidget extends AppWidgetProvider {
	private static AppWidgetManager manager;
	private static RemoteViews views;
	private static Context context;

	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		super.onUpdate(context, appWidgetManager, appWidgetIds);
		updateAppWidget(context);
		log("CallUpdate from onUpdate");
	}

	public void onReceive(Context context, Intent intent) {
		super.onReceive(context, intent);
		Log.d("KyoAni", intent.toString());
		// if (intent.getAction().equals(AppWidgetManager.ACTION_APPWIDGET_UPDATE))
		// {
		// updateAppWidget(context);
		// log("CallUpdate from onReceive");
		// } else {
		// super.onReceive(context, intent);
		// }
	}

	public static void updateAppWidget(Context _context) {
		context = _context;
		manager = AppWidgetManager.getInstance(context);

		SharedPreferences pref = PreferenceManager
				.getDefaultSharedPreferences(context);
		views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);

		final String account = pref.getString("account", "");
		final String password = pref.getString("password", "");

		Thread thread = new Thread(new Runnable() {
			public void run() {
				AnimeOne anime_one = new AnimeOne(new Account(account, password));
				String schedule_str = null;
				schedule_str = context.getText(R.string.login_failure).toString();
				log("getSchedules");
				ArrayList<Schedule> schedules = anime_one.getSchedules(context);
				if (schedules != null) {
					schedule_str = context.getText(R.string.no_schedule).toString();
					for (Schedule schedule : schedules) {
						GregorianCalendar now = new GregorianCalendar();

						if (now.compareTo(schedule.getStart()) == -1) {
							schedule_str = schedule.getChannel() + "\n"
									+ schedule.getStartString() + "\n" + schedule.getName()
									+ "\n";
							break;
						}
					}
				}
				views.setTextViewText(R.id.next_log, schedule_str);
				log("Update Widget View");
				manager.updateAppWidget(new ComponentName(context, KyoAniWidget.class),
						views);
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
