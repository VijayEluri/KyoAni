package net.hisme.masaki.kyoani.test.services;

import net.hisme.masaki.kyoani.services.DailyUpdater;
import android.test.ServiceTestCase;

public class DailyUpdaterTest extends ServiceTestCase<DailyUpdater> {

  public DailyUpdaterTest() {
    super(DailyUpdater.class);
  }

  public void testOnCreate() {
    this.getService();
  }
}
