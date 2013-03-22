package net.hisme.masaki.kyoani.test.models;

import junit.framework.TestCase;
import net.hisme.masaki.kyoani.models.Schedule;
import net.hisme.masaki.kyoani.models.Schedules;

public class SchedulesTest extends TestCase {
  private Schedules schedules;

  public void setUp() {
    schedules = new Schedules();
    schedules.add(new Schedule("channel1", "title1", "05:00"));
    schedules.add(new Schedule("channel2", "title2", "29:58"));
    schedules.add(new Schedule("channel3", "title3", "29:59"));
  }

  public void testNext() {
    Schedule schedule = schedules.next();
    assertEquals("channel2", schedule.getChannel());
  }
}
