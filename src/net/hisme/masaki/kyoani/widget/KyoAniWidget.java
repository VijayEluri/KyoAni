package net.hisme.masaki.kyoani.widget;

import android.appwidget.AppWidgetProvider;
import android.appwidget.AppWidgetManager;
import android.widget.RemoteViews;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.ComponentName;
import android.preference.PreferenceManager;
import android.util.Log;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import net.hisme.masaki.kyoani.Account;
import net.hisme.masaki.kyoani.AnimeOne;
import net.hisme.masaki.kyoani.R;

public class KyoAniWidget extends AppWidgetProvider {
	private static AppWidgetManager manager;
	private static RemoteViews views;
	private static Context context;

	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		super.onUpdate(context, appWidgetManager, appWidgetIds);
		updateAppWidget(context);
	}

	public static void updateAppWidget(Context _context) {
		context = _context;
		manager = AppWidgetManager.getInstance(context);
		SharedPreferences pref = PreferenceManager
				.getDefaultSharedPreferences(context);
		views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
		views.setTextViewText(R.id.next_log, "updating...");

		final String account = pref.getString("account", "");
		final String password = pref.getString("password", "");

		Thread thread = new Thread(new Runnable() {
			public void run() {
				AnimeOne anime_one = new AnimeOne(new Account(account, password));
				String schedule_str = "ログインできませんでした";
				int login_result = anime_one.login();
				if (login_result == AnimeOne.LOGIN_OK) {
					GregorianCalendar now = new GregorianCalendar();
					log(now.getTime().toString());
					GregorianCalendar today = new GregorianCalendar(now
							.get(Calendar.YEAR), now.get(Calendar.MONTH), now
							.get(Calendar.DAY_OF_MONTH), now.get(Calendar.HOUR_OF_DAY) - 6,
							0, 0);
					log(today.getTime().toString());
					ArrayList<String[]> schedules = anime_one.mypage();
					schedule_str = context.getText(R.string.no_schedule).toString();
					for (String[] schedule : schedules) {
						String[] times = schedule[1].split(":");
						GregorianCalendar start = new GregorianCalendar(today
								.get(Calendar.YEAR), today.get(Calendar.MONTH), today
								.get(Calendar.DAY_OF_MONTH), Integer.parseInt(times[0]),
								Integer.parseInt(times[1]), 0);
						log(start.getTime().toString());
						if (now.compareTo(start) == -1) {
							schedule_str = schedule[1] + " " + schedule[2] + "\n"
									+ schedule[0] + "\n";
							break;
						}
					}
					views.setTextViewText(R.id.next_log, schedule_str);
				} else if (login_result == AnimeOne.LOGIN_NG) {
					views.setTextViewText(R.id.next_log, context
							.getText(R.string.login_failure));
				}
				manager.updateAppWidget(new ComponentName(context, KyoAniWidget.class),
						views);
				log(schedule_str);
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
