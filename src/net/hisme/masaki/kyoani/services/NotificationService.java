package net.hisme.masaki.kyoani.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Vibrator;
import android.widget.Toast;

public class NotificationService extends Service {
    @Override
    public void onStart(Intent intent, int startId) {
        String message = (String) intent.getExtras().get("ToastMessage");
        getVibrator().vibrate(100);
        Toast.makeText(NotificationService.this, message, Toast.LENGTH_LONG);
        stopSelf();
    }

    private Vibrator getVibrator() {
        return (Vibrator) getSystemService(VIBRATOR_SERVICE);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
