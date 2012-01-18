package net.hisme.masaki.kyoani.test.activities;

import net.hisme.masaki.kyoani.activities.MainActivity;
import android.test.ActivityUnitTestCase;

public class MainActivityTest extends ActivityUnitTestCase<MainActivity> {

  public MainActivityTest() {
    super(MainActivity.class);
  }

  public void testAdd() {
    assertEquals(1, 0 + 1);
  }
}
