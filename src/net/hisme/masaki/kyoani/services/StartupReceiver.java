package net.hisme.masaki.kyoani.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class StartupReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        log("onReceive");
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            log("receive BOOT_COMPLETED");
            context.startService(new Intent(context, DailyUpdater.class));
        }
    }

    public void log(String message) {
        android.util.Log.d("AnimeOne", "[StartupReceive] " + message);
    }
}
