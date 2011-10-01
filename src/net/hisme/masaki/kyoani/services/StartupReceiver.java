package net.hisme.masaki.kyoani.services;

import net.hisme.masaki.kyoani.App;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class StartupReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            App.Log.d("BOOT_COMPLETED received");
            context.startService(new Intent(context, DailyUpdater.class));
        }
    }
}
