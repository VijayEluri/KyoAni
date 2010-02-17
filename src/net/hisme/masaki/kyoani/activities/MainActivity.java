package net.hisme.masaki.kyoani.activities;

import net.hisme.masaki.kyoani.R;
import net.hisme.masaki.kyoani.AnimeOne;
import net.hisme.masaki.kyoani.Account;
import net.hisme.masaki.kyoani.Schedule;
import java.util.ArrayList;
import java.util.HashMap;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuInflater;
import android.widget.SimpleAdapter;
import android.widget.ListView;
import android.widget.Button;
import android.widget.Toast;
import android.content.Intent;

public class MainActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		updateSchedule();
		Button reload_button = (Button) findViewById(R.id.reload_button);
		reload_button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				reloadSchedule();
			}
		});
	}

	public void updateSchedule() {
		updateSchedule(false);
	}

	public void reloadSchedule() {
		updateSchedule(true);
	}

	public void updateSchedule(boolean force_reload) {
		try {
			AnimeOne anime_one = new AnimeOne(this);

			ArrayList<Schedule> list = new ArrayList<Schedule>();
			if (force_reload) {
				list = anime_one.reloadSchedules(this);
			} else {
				list = anime_one.getSchedules(this);
			}

			ArrayList<HashMap<String, String>> schedules = new ArrayList<HashMap<String, String>>();
			for (Schedule schedule : list) {
				HashMap<String, String> hash = new HashMap<String, String>();
				hash.put("LINE_1", schedule.getName());
				hash.put("LINE_2", schedule.getChannel() + " "
						+ schedule.getStartString());
				schedules.add(hash);
			}

			SimpleAdapter adapter = new SimpleAdapter(this, schedules,
					android.R.layout.simple_list_item_2, new String[] {
							"LINE_1", "LINE_2" }, new int[] {
							android.R.id.text1, android.R.id.text2 });
			ListView schedule_list = (ListView) findViewById(R.id.schedule_list);

			schedule_list.setAdapter(adapter);
		} catch (Account.BlankException e) {
			displayErrorMessage(R.string.error_account_is_blank);
		}
	}

	public void displayErrorMessage(int res_id) {
		String str = getText(res_id).toString();
		Toast.makeText(MainActivity.this, str, 5).show();
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
