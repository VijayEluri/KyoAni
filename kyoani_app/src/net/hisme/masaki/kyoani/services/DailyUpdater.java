package net.hisme.masaki.kyoani.services;

import net.hisme.masaki.kyoani.App;
import net.hisme.masaki.kyoani.BlankAccontException;
import net.hisme.masaki.kyoani.schedule_service.exception.LoginFailureException;
import net.hisme.masaki.kyoani.schedule_service.exception.NetworkUnavailableException;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * service to update schedules and widgets at beginning of day
 * 
 * @author masarakki
 */
public class DailyUpdater extends Service {
  @Override
  public void onStart(Intent intent, int startId) {
    super.onStart(intent, startId);
    log("started.");
    if (!App.li.getAccount().isBlank()) {
      new Thread() {
        public void run() {
          try {
            App.li.reload();
          } catch (BlankAccontException e) {
            e.printStackTrace();
          } catch (LoginFailureException e) {
            e.printStackTrace();
          } catch (NetworkUnavailableException e) {
            e.printStackTrace();
          }
        }
      }.start();
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
    App.Log.d("[DailyUpdater] " + message);
  }
}
