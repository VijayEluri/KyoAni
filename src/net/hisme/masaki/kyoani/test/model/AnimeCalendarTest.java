package net.hisme.masaki.kyoani.test.model;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import junit.framework.TestCase;
import net.hisme.masaki.kyoani.AnimeCalendar;

public class AnimeCalendarTest extends TestCase {
	private AnimeCalendar cal;

	public AnimeCalendarTest(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
		super.setUp();
		cal = new AnimeCalendar(2000, 2, 2, 4, 30);
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testHourWhenMidnight() {
		AnimeCalendar cal1 = new AnimeCalendar("28:30");
		AnimeCalendar cal2 = new AnimeCalendar();
		assertEquals(cal1.get(Calendar.DAY_OF_MONTH), cal2
				.get(Calendar.DAY_OF_MONTH));
		assertEquals(cal1.get(Calendar.YEAR), cal2.get(Calendar.YEAR));
		assertEquals(cal1.get(Calendar.MONTH), cal2.get(Calendar.MONTH));
		assertEquals(28, cal1.get(Calendar.HOUR_OF_DAY));
		assertEquals(30, cal1.get(Calendar.MINUTE));
	}

	public void testAnimeCalendar() {
		assertEquals(new AnimeCalendar(2010, 1, 1), new GregorianCalendar(2010,
				1, 1));
		assertEquals(new AnimeCalendar(2010, 1, 1, 0, 10, 30),
				new GregorianCalendar(2010, 1, 1, 0, 10, 30));
		assertEquals(new AnimeCalendar(), new GregorianCalendar());
		assertEquals(new AnimeCalendar(Locale.JAPAN), new GregorianCalendar(
				Locale.JAPAN));
		TimeZone tz = TimeZone.getDefault();
		assertEquals(new AnimeCalendar(tz), new GregorianCalendar(tz));
		assertEquals(new AnimeCalendar(tz, Locale.JAPAN), new AnimeCalendar(tz,
				Locale.JAPAN));
	}

	public void testIsMidnight() {
		assertEquals(true, cal.isMidnight());
		assertEquals(false, new AnimeCalendar(2000, 2, 2, 5, 10).isMidnight());
	}

	public void testGet() {
		assertEquals(1, cal.get(Calendar.DAY_OF_MONTH));
		assertEquals(28, cal.get(Calendar.HOUR_OF_DAY));

		AnimeCalendar cal2 = new AnimeCalendar(2000, 2, 2, 5, 10);
		assertEquals(2, cal2.get(Calendar.DAY_OF_MONTH));
		assertEquals(5, cal2.get(Calendar.HOUR_OF_DAY));

		AnimeCalendar cal3 = new AnimeCalendar(2000, 2, 1, 28, 30);
		assertEquals(1, cal3.get(Calendar.DAY_OF_MONTH));
		assertEquals(28, cal3.get(Calendar.HOUR_OF_DAY));
	}

	public void testGetTimeString() {
		assertEquals("28:30", cal.getTimeString());
	}
}
