package net.hisme.masaki.kyoani.models;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Calendar for TV show that support like 25:00
 * 
 * @author masarakki
 */
public class AnimeCalendar extends GregorianCalendar {
  private static final long serialVersionUID = 1L;
  private static final int BEGINNING_OF_DAY_HOUR = 5;
  private static final int BEGINNING_OF_DAY_MINUTES = 0;

  public AnimeCalendar() {
    super();
  }

  public AnimeCalendar(long unixtime) {
    super();
    setTime(new Date(unixtime * 1000));
  }

  public AnimeCalendar(int year, int month, int dayOfMonth) {
    super(year, month, dayOfMonth);
  }

  public AnimeCalendar(int year, int month, int dayOfMonth, int hourOfDay,
      int minute) {
    super(year, month, dayOfMonth, hourOfDay, minute);
  }

  public AnimeCalendar(int year, int month, int dayOfMonth, int hourOfDay,
      int minute, int second) {
    super(year, month, dayOfMonth, hourOfDay, minute, second);
  }

  public AnimeCalendar(Locale aLocale) {
    super(aLocale);
  }

  public AnimeCalendar(TimeZone zone) {
    super(zone);
  }

  public AnimeCalendar(TimeZone zone, Locale aLocale) {
    super(zone, aLocale);
  }

  /**
   * @param time
   *          date time expression like 25:30
   */
  public AnimeCalendar(String time) {
    super();
    String[] times = time.split(":");
    set(DAY_OF_MONTH, get(DAY_OF_MONTH));
    set(HOUR_OF_DAY, Integer.parseInt(times[0]));
    set(MINUTE, Integer.parseInt(times[1]));
  }

  @Override
  public int get(int field) {
    int result = super.get(field);
    if (field == DAY_OF_MONTH && isMidnight()) {
      return result - 1;
    } else if (field == HOUR_OF_DAY && isMidnight()) {
      return result + 24;
    } else {
      return result;
    }
  }

  /**
   * @return is over 24:00 or not?
   */
  public boolean isMidnight() {
    return super.get(HOUR_OF_DAY) < BEGINNING_OF_DAY_HOUR || super.get(MINUTE) < BEGINNING_OF_DAY_MINUTES;
  }

  public int year() {
    return get(YEAR);
  }

  public int month() {
    return get(MONTH) + 1;
  }

  public int day() {
    return get(DAY_OF_MONTH);
  }

  public int hour() {
    return get(HOUR_OF_DAY);
  }

  public int minute() {
    return get(MINUTE);
  }

  public String getDateString() {
    return String.format("%02d-%02d", month(), day());
  }

  public String getTimeString() {
    return String.format("%02d:%02d", hour(), minute());
  }

  /**
   * @return new instance of AnimeCalender about beginning of day
   */
  public AnimeCalendar beginningOfDay() {
    AnimeCalendar cloned = (AnimeCalendar) this.clone();
    cloned.set(HOUR_OF_DAY, BEGINNING_OF_DAY_HOUR);
    cloned.set(MINUTE, BEGINNING_OF_DAY_MINUTES);
    cloned.set(SECOND, 0);
    return cloned;
  }

  /**
   * @return new instance of GregorianCalendar that means today
   */
  public static GregorianCalendar today() {
    AnimeCalendar now = new AnimeCalendar();
    return new GregorianCalendar(now.get(YEAR), now.get(MONTH), now.get(DAY_OF_MONTH), 0, 0, 0);
  }

  /**
   * @return new instance of AnimeCalendar of tomorrow
   */
  public static AnimeCalendar tomorrow() {
    AnimeCalendar now = new AnimeCalendar();
    return new AnimeCalendar(now.get(YEAR), now.get(MONTH), now.get(DAY_OF_MONTH) + 1).beginningOfDay();
  }

  public String toString() {
    return String.format("%s %s", getDateString(), getTimeString());
  }

  public boolean equals(GregorianCalendar other) {
    return super.equals(other);
  }
}
