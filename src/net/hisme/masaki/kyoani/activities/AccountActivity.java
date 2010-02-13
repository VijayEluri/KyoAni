package net.hisme.masaki.kyoani.activities;

import net.hisme.masaki.kyoani.R;
import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ArrayAdapter;
import android.content.Intent;
import android.net.Uri;

public class AccountActivity extends ListActivity {
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, new String[] {
						getString(R.string.label_account_setting),
						getString(R.string.label_account_test) });
		setListAdapter(adapter);
		/*
		 * TextView account_setting = (TextView)
		 * findViewById(R.id.account_setting);
		 * account_setting.setOnClickListener(new View.OnClickListener() {
		 * public void onClick(View view) { Intent intent = new
		 * Intent(AccountActivity.this, SettingActivity.class);
		 * startActivity(intent); } });
		 * 
		 * Button account_test = (Button) findViewById(R.id.account_test);
		 * account_test.setOnClickListener(new View.OnClickListener() {
		 * 
		 * @Override public void onClick(View v) {
		 * 
		 * // TODO Auto-generated method stub } });
		 */
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		switch (position) {
		case 0:
			startActivity(new Intent(AccountActivity.this,
					SettingActivity.class));
			return;
		case 1:

			return;
		}
		return;
	}
}
