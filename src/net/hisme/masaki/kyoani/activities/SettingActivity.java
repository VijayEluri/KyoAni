package net.hisme.masaki.kyoani.activities;

import net.hisme.masaki.kyoani.R;
import net.hisme.masaki.kyoani.R.xml;
import android.preference.PreferenceActivity;
import android.os.Bundle;

public class SettingActivity extends PreferenceActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.pref);
	}
}