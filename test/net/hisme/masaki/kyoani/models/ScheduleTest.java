package net.hisme.masaki.kyoani.models;

import net.hisme.masaki.kyoani.models.AnimeCalendar;
import net.hisme.masaki.kyoani.models.Schedule;
import static org.junit.Assert.*;
import org.junit.*;

public class ScheduleTest {
  private Schedule schedule;

  @Before
  public void before() {
    schedule = new Schedule("channel", "title", "25:30");
  }

  /**
   * new with instance of AnimeCalendar, getStart() should equal to that
   * instance
   */
  @Test
  public void schedule() {
    assertEquals(schedule.getStart().hashCode(),
        new Schedule("channel", "title", schedule.getStart()).getStart().hashCode());
  }

  /**
   * getChannel() should equal to channel parameter
   */
  @Test
  public void getChannel() {
    assertEquals("channel", schedule.getChannel());
  }

  /**
   * getName() should equal to name parameter
   */
  @Test
  public void getName() {
    assertEquals("title", schedule.getName());
  }

  /**
   * getStart() should equal to start parameter
   */
  @Test
  public void getStart() {
    assertEquals(new AnimeCalendar("25:30"), schedule.getStart());
  }

  /**
   * getStartString() should call start.getStartString()
   */
  @Test
  public void getStartString() {
    assertEquals("25:30", schedule.getStartString());
  }

  /**
   * toString() should format information
   */
  @Test
  public void testToString() {
    assertEquals("[Schedule] channel = channel; name = title; start = 25:30", schedule.toString());

  }
}
