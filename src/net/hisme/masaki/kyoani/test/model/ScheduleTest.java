package net.hisme.masaki.kyoani.test.model;

import junit.framework.TestCase;
import net.hisme.masaki.kyoani.Schedule;

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

	public void testGetChannel() {
		assertEquals("channel", schedule.getChannel());
	}

	public void testGetName() {
		assertEquals("title", schedule.getName());
	}

	public void testGetStartString() {
		assertEquals("25:30", schedule.getStartString());
	}

	public void testToString() {
		assertEquals(
				"[Schedule] channel = channel; name = title; start = 25:30",
				schedule.toString());
	}
}
