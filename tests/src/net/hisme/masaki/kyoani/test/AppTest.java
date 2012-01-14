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

  public void test_App_li_should_be_an_instance_of_App() {
    assertEquals(App.li, this.getApplication());
  }

}
