package net.hisme.masaki.kyoani.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import net.hisme.masaki.kyoani.services.KyoAniService;

public class DailyUpdater extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        log("onReceive");
    }

    public void log(String message) {
        android.util.Log.d("KyoAni", "[DailyUpdater] " + message);
    }
}
