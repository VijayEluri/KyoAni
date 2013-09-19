package net.hisme.masaki.kyoani.activities;

import net.hisme.masaki.kyoani.schedule_service.LifePlan;
import android.preference.PreferenceActivity;
import android.os.Bundle;

/**
 * @author masarakki
 */
public class SettingActivity extends PreferenceActivity {
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    LifePlan life_plan = new LifePlan();
    life_plan.getAuthorizationUrl();
  }
}