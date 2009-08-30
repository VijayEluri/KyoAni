package net.hisme.masaki.kyoani;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
import android.net.Uri;

public class KyoAni extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		Button button_animeone = (Button) findViewById(R.id.button_link_to_animeone);
		Button button_setting = (Button) findViewById(R.id.button_link_to_setting);
		button_animeone.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				Intent intent = new Intent(Intent.ACTION_VIEW, Uri
						.parse("https://anime.biglobe.ne.jp/regist/regist_user"));
				startActivity(intent);
			}
		});
		button_setting.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				Intent intent = new Intent(KyoAni.this, SettingActivity.class);
				startActivity(intent);
			}
		});
	}
}