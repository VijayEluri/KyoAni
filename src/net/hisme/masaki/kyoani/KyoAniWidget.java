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
			ArrayList<String[]> schedules = anime_one.mypage();
			String schedule_str = "";
			for (String[] schedule : schedules) {
				schedule_str += schedule[1] + " " + schedule[2] + "\n" + schedule[0]
						+ "\n";
			}
			views.setTextViewText(R.id.next_log, schedule_str);
		} else {
			views.setTextViewText(R.id.next_log, context
					.getText(R.string.login_failure));
		}
		manager.updateAppWidget(new ComponentName(context, KyoAniWidget.class),
				views);
	}

	private static void log(String str) {
		Log.d("KyoAni", "[KyoAniWidget] " + str);
	}
}
