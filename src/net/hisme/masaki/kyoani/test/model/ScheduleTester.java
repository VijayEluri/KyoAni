package net.hisme.masaki.kyoani.test.model;

import junit.framework.TestCase;
import net.hisme.masaki.kyoani.Schedule;

public class ScheduleTester extends TestCase {
	private Schedule schedule;

	public ScheduleTester(String name) {
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

}
