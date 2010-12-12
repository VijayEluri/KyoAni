package net.hisme.masaki.kyoani.test.model;

import junit.framework.TestCase;
import net.hisme.masaki.kyoani.models.AnimeCalendar;
import net.hisme.masaki.kyoani.models.Schedule;

public class ScheduleTest extends TestCase {
	private Schedule schedule;

	public ScheduleTest(String name) {
		super(name);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		schedule = new Schedule("channel", "title", "25:30");
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/**
	 * new with instance of AnimeCalendar, getStart() should equal to that
	 * instance
	 */
	public void testSchedule() {
		assertEquals(schedule.getStart().hashCode(), new Schedule("channel",
				"title", schedule.getStart()).getStart().hashCode());
	}

	/**
	 * getChannel() should equal to channel parameter
	 */
	public void testGetChannel() {
		assertEquals("channel", schedule.getChannel());
	}

	/**
	 * getName() should equal to name parameter
	 */
	public void testGetName() {
		assertEquals("title", schedule.getName());
	}

	/**
	 * getStart() should equal to start parameter
	 */
	public void testGetStart() {
		assertEquals(new AnimeCalendar("25:30"), schedule.getStart());
	}

	/**
	 * getStartString() should call start.getStartString()
	 */
	public void testGetStartString() {
		assertEquals("25:30", schedule.getStartString());
	}

	/**
	 * toString() should format information
	 */
	public void testToString() {
		assertEquals(
				"[Schedule] channel = channel; name = title; start = 25:30",
				schedule.toString());
	}
}
