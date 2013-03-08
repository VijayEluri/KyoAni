package net.hisme.masaki.kyoani.test.activities;

import net.hisme.masaki.kyoani.activities.MainActivity;
import android.test.ActivityInstrumentationTestCase2;

public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {

  public MainActivityTest() {
    super("net.hisme.masaki.kyoani", MainActivity.class);
  }

  protected void setUp() throws Exception {
    super.setUp();
    this.setActivityInitialTouchMode(false);
  }

  public void testDisplayErrorMessage() {

  }
}
