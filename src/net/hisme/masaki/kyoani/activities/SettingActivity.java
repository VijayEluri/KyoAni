package net.hisme.masaki.kyoani.activities;

import net.hisme.masaki.kyoani.App;
import net.hisme.masaki.kyoani.R;
import android.preference.PreferenceActivity;
import android.os.Bundle;

/**
 * @author masaki
 */
public class SettingActivity extends PreferenceActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.pref);
	}
	
	public void onDestroy() {
		App.li.reset_account();
		super.onDestroy();
	}
}