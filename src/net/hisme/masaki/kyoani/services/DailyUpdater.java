package net.hisme.masaki.kyoani.services;

import net.hisme.masaki.kyoani.models.AnimeOne;
import net.hisme.masaki.kyoani.models.Account.BlankException;

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
        log("started.");
        try {
            new AnimeOne(this).fetchSchedules();
            startService(new Intent(DailyUpdater.this, WidgetUpdater.class));
        } catch (BlankException e) {
            e.printStackTrace();
        }
        stopSelf();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        log("destroyed.");
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    public void log(String message) {
        android.util.Log.d("KyoAni", "[DailyUpdater] " + message);
    }
}
