package net.hisme.masaki.kyoani.activities;

import net.hisme.masaki.kyoani.App;
import net.hisme.masaki.kyoani.R;
import net.hisme.masaki.kyoani.models.Schedule;
import net.hisme.masaki.kyoani.models.Schedules;
import net.hisme.masaki.kyoani.schedule_service.exception.LoginFailureException;
import net.hisme.masaki.kyoani.schedule_service.exception.NetworkUnavailableException;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuInflater;
import android.widget.SimpleAdapter;
import android.widget.ListView;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ArrayAdapter;
import android.content.Context;
import android.content.Intent;

/**
 * @author masarakki
 */
public class MainActivity extends Activity {
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
    ListView schedule_list = (ListView) findViewById(R.id.schedule_list);
    schedule_list.addFooterView(createReloadButton());
    displaySchedules();
  }

  public View createReloadButton() {
    LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    View reload_button = inflater.inflate(R.layout.reload_button, null);

    ((Button) reload_button.findViewById(R.id.reload_button)).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        reload();
      }
    });

    return reload_button;
  }

  public void reload() {
    ListView schedule_list = (ListView) MainActivity.this.findViewById(R.id.schedule_list);
    ArrayAdapter<String> array_adapter = new ArrayAdapter<String>(
        MainActivity.this, android.R.layout.simple_list_item_1);
    array_adapter.add("更新中...");
    schedule_list.setAdapter(array_adapter);

    final Handler handler = new Handler();
    new Thread() {
      public void run() {
        try {
          App.li.reload();
          handler.post(new Runnable() {
            public void run() {
              MainActivity.this.displaySchedules();
            }
          });
        } catch (LoginFailureException e) {
          MainActivity.this.displayErrorMessage(R.string.error_account_cant_authorize);
        } catch (NetworkUnavailableException e) {
          MainActivity.this.displayErrorMessage(R.string.error_network_disable);
        }
      }
    }.start();
  }

  private void displaySchedules() {
    Schedules schedules = App.li.getSchedules();
    ArrayList<HashMap<String, String>> schedules_hash = new ArrayList<HashMap<String, String>>();
    try {
      for (Schedule schedule : schedules) {
        HashMap<String, String> hash = new HashMap<String, String>();
        hash.put("LINE_1", schedule.getName());
        hash.put("LINE_2", schedule.getChannel() + " " + schedule.getStartString());
        schedules_hash.add(hash);
      }
    } catch (NullPointerException e) {}
    SimpleAdapter adapter = new SimpleAdapter(MainActivity.this, schedules_hash,
        android.R.layout.simple_list_item_2,
        new String[] { "LINE_1", "LINE_2" },
        new int[] { android.R.id.text1, android.R.id.text2 });
    ListView schedule_list = (ListView) findViewById(R.id.schedule_list);
    schedule_list.setAdapter(adapter);
  }

  public void displayErrorMessage(int res_id) {
    String str = getText(res_id).toString();
    Toast.makeText(MainActivity.this, str, Toast.LENGTH_LONG).show();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.main, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
    case R.id.menu_account:
      startActivity(new Intent(MainActivity.this, AccountActivity.class));
      return true;
    case R.id.menu_reload:
      reload();
      return true;
    case R.id.menu_help:
      startActivity(new Intent(MainActivity.this, HelpActivity.class));
      return true;
    case R.id.menu_about:
      startActivity(new Intent(MainActivity.this, AboutActivity.class));
      return true;
    }
    return false;
  }
}
