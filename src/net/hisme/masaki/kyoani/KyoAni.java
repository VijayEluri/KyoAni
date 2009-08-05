package net.hisme.masaki.kyoani;

import android.preference.PreferenceActivity;
import android.os.Bundle;
import android.R.xml;

public class KyoAni extends PreferenceActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.pref);
	}
}