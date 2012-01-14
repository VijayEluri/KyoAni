package net.hisme.masaki.kyoani.test.models;

import net.hisme.masaki.kyoani.App;
import net.hisme.masaki.kyoani.models.KyoAni;
import android.test.ApplicationTestCase;

public class KyoAniTest extends ApplicationTestCase<App> {
  public KyoAniTest() {
    super(App.class);
  }

  private KyoAni kyoani = null;

  public void setUp() {
    this.createApplication();
    kyoani = new KyoAni(this.getContext());
  }

  public void test_context() {
    assertEquals(getContext(), kyoani.context());
  }
}
