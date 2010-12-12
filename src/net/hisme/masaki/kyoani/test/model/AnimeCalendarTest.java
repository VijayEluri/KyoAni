package net.hisme.masaki.kyoani.test.model;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.*;

import static org.junit.Assert.*;

import junit.framework.TestCase;
import net.hisme.masaki.kyoani.models.AnimeCalendar;

public class AnimeCalendarTest {
    private AnimeCalendar cal;

    @Before
    public void before() {
        cal = new AnimeCalendar(2000, 2, 2, 4, 30);
    }

    @Test
    public void hourWhenMidnight() {
        AnimeCalendar cal1 = new AnimeCalendar("28:30");
        AnimeCalendar cal2 = new AnimeCalendar();
        assertEquals(cal1.get(Calendar.DAY_OF_MONTH), cal2
                .get(Calendar.DAY_OF_MONTH));
        assertEquals(cal1.get(Calendar.YEAR), cal2.get(Calendar.YEAR));
        assertEquals(cal1.get(Calendar.MONTH), cal2.get(Calendar.MONTH));
        assertEquals(28, cal1.get(Calendar.HOUR_OF_DAY));
        assertEquals(30, cal1.get(Calendar.MINUTE));
    }

    @Test
    public void init() {
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

    @Test
    public void isMidnight() {
        assertEquals(true, cal.isMidnight());
        assertEquals(false, new AnimeCalendar(2000, 2, 2, 5, 10).isMidnight());
    }

    @Test
    public void get() {
        assertEquals(1, cal.get(Calendar.DAY_OF_MONTH));
        assertEquals(28, cal.get(Calendar.HOUR_OF_DAY));

        AnimeCalendar cal2 = new AnimeCalendar(2000, 2, 2, 5, 10);
        assertEquals(2, cal2.get(Calendar.DAY_OF_MONTH));
        assertEquals(5, cal2.get(Calendar.HOUR_OF_DAY));

        AnimeCalendar cal3 = new AnimeCalendar(2000, 2, 1, 28, 30);
        assertEquals(1, cal3.get(Calendar.DAY_OF_MONTH));
        assertEquals(28, cal3.get(Calendar.HOUR_OF_DAY));
    }

    @Test
    public void getTimeString() {
        assertEquals("28:30", cal.getTimeString());
    }
}
