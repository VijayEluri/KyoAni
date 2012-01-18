package net.hisme.masaki.kyoani.test;

import android.test.ApplicationTestCase;
import net.hisme.masaki.kyoani.App;

public class AppTest extends ApplicationTestCase<App> {
  public AppTest() {
    super(App.class);
  }

  public void setUp() {
    this.createApplication();

  }

  public void testApp_li() {
    assertEquals(App.li, this.getApplication());
  }

}
