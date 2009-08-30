package net.hisme.masaki.kyoani;

import android.app.PendingIntent;
import android.appwidget.AppWidgetProvider;
import android.appwidget.AppWidgetManager;
import android.widget.RemoteViews;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.ComponentName;
import android.preference.PreferenceManager;
import android.util.Log;
import java.util.ArrayList;
import java.util.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class KyoAniWidget extends AppWidgetProvider {
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		super.onUpdate(context, appWidgetManager, appWidgetIds);
		updateAppWidget(context);
	}

	public static void updateAppWidget(Context context) {
		AppWidgetManager manager = AppWidgetManager.getInstance(context);
		SharedPreferences pref = PreferenceManager
				.getDefaultSharedPreferences(context);
		RemoteViews views = new RemoteViews(context.getPackageName(),
				R.layout.widget_layout);
		views.setTextViewText(R.id.next_log, "updating...");

		String account = pref.getString("account", "");
		String password = pref.getString("password", "");
		AnimeOne anime_one = new AnimeOne(new Account(account, password));

		if (anime_one.login()) {
			GregorianCalendar now = new GregorianCalendar();
			GregorianCalendar today = new GregorianCalendar(now.get(Calendar.YEAR),
					now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH), now
							.get(Calendar.HOUR) - 6, 0, 0);
			ArrayList<String[]> schedules = anime_one.mypage();
			String schedule_str = "今日の予定は終了\nおやすみなさい...";
			for (String[] schedule : schedules) {
				String[] times = schedule[1].split(":");
				GregorianCalendar start = new GregorianCalendar(today
						.get(Calendar.YEAR), today.get(Calendar.MONTH), today
						.get(Calendar.DAY_OF_MONTH), Integer.parseInt(times[0]), Integer
						.parseInt(times[1]), 0);
				
				if (now.compareTo(start) == -1) {
					schedule_str = schedule[1] + " " + schedule[2] + "\n" + schedule[0]
							+ "\n";
					break;
				}
			}
			views.setTextViewText(R.id.next_log, schedule_str);
		} else {
			views.setTextViewText(R.id.next_log, context
					.getText(R.string.login_failure));
		}
		manager.updateAppWidget(new ComponentName(context, KyoAniWidget.class),
				views);
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
