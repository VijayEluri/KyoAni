package net.hisme.masaki.kyoani.activities;

import net.hisme.masaki.kyoani.R;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.content.Intent;
import android.net.Uri;

public class AccountActivity extends Activity {
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.account);
		TextView account = (TextView) findViewById(R.id.label_account);
		account.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				Intent intent = new Intent(AccountActivity.this,
						SettingActivity.class);
				startActivity(intent);
			}
		});
	}
}
