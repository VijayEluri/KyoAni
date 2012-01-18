package net.hisme.masaki.kyoani.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Vibrator;

public class NotificationService extends Service {
  @Override
  public void onStart(Intent intent, int startId) {
    super.onStart(intent, startId);
    long[] pattern = { 0, 200, 300, 200, 300, 200 };
    getVibrator().vibrate(pattern, -1);
    stopSelf();
  }

  private Vibrator getVibrator() {
    return (Vibrator) getSystemService(VIBRATOR_SERVICE);
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
  }

  @Override
  public IBinder onBind(Intent intent) {
    return null;
  }
}
