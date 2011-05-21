package net.hisme.masaki.kyoani.services;

import java.util.Calendar;

import net.hisme.masaki.kyoani.models.AnimeCalendar;
import net.hisme.masaki.kyoani.models.AnimeOne;
import net.hisme.masaki.kyoani.models.Account.BlankException;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * 朝のアップデートをするサービス
 * 
 * @author masaki
 * 
 */
public class DailyUpdater extends Service {
    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        try {
            new AnimeOne(this).fetchSchedules();
        } catch (BlankException e) {
            e.printStackTrace();
        }
        setupNext();
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    private void setupNext() {
        Intent intent = new Intent(DailyUpdater.this, DailyUpdater.class);

        PendingIntent pending_intent = PendingIntent.getService(
                DailyUpdater.this, 0, intent, 0);

        AnimeCalendar calendar = AnimeCalendar.tomorrow();

        AlarmManager alarm_manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarm_manager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                pending_intent);
    }

    public void log(String message) {
        android.util.Log.d("KyoAni", "[DailyUpdater] " + message);
    }

}
