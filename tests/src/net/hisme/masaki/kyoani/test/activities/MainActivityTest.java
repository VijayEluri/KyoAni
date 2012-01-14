package net.hisme.masaki.kyoani.test.activities;

import net.hisme.masaki.kyoani.activities.MainActivity;
import android.test.ActivityUnitTestCase;

public class MainActivityTest extends ActivityUnitTestCase<MainActivity> {

  public MainActivityTest() {
    super(MainActivity.class);
  }

  protected void setUp() throws Exception {
    super.setUp();
  }

  protected void tearDown() throws Exception {
    super.tearDown();
  }

  public void test_a() {
    assertEquals(2, 1 + 1);
  }

  public void test_b() {
    assertEquals(2.0, 1.0 + 1.0);
  }

}
