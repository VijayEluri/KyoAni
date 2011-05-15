package net.hisme.masaki.kyoani.services;

import java.util.Calendar;
import java.util.Locale;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import net.hisme.masaki.kyoani.models.ScheduleService;
import net.hisme.masaki.kyoani.models.AnimeOne;
import net.hisme.masaki.kyoani.models.Account.BlankException;

/**
 * KyoAniService provides Schedules
 * 
 * @author masaki
 * 
 */
public class KyoAniService extends Service {
    protected ScheduleService schedule_service;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onCreate() {
        dailyUpdate();
        try {
            this.schedule_service = new AnimeOne(this);
        } catch (BlankException e) {

        }
    }

    public void dailyUpdate() {
        // TODO: implement daily update
        scheduleNextDailyUpdate();
    }

    public void scheduleNextDailyUpdate() {
        Intent intent = new Intent();
        intent.setAction("net.hisme.masaki.kyoani.update_daily");

        PendingIntent pending_intent = PendingIntent.getBroadcast(
                KyoAniService.this, 0, intent, 0);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.SECOND, 3);

        AlarmManager alarm_manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarm_manager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                pending_intent);
    }

    /**
     * schedule_service をロードしなおす
     */
    public void loadScheduleService() {

    }

    @Override
    /**
     * @TODO 終了時処理注意
     */
    public void onDestroy() {
        super.onDestroy();
        log("KyoAniService.onDestroy");
    }

    public class DailyUpdater extends BroadcastReceiver {
        public void onReceive(Context context, Intent intent) {
            log("received timer");
            // KyoAniService.this.scheduleDailyUpdate();
            // Toast.makeText(context, "hoge", 500).show();
        }
    }

    protected void log(String message) {
        android.util.Log.d("KyoAni", "[KyoAniService] " + message);
    }
}
