package net.hisme.masaki.kyoani.models;

import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 * 25時 などの表記に特化したカレンダークラス
 * 
 * @author masaki
 */
public class AnimeCalendar extends GregorianCalendar {
  private static final long serialVersionUID = 1L;
  private static int BEGINNING_OF_DAY = 5;

  public AnimeCalendar() {
    super();
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
   *          25:30 のような形式の文字列
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
   * 24時以降か?
   * 
   * @return
   */
  public boolean isMidnight() {
    return super.get(HOUR_OF_DAY) < BEGINNING_OF_DAY;
  }

  public String getDateString() {
    return String.format("%02d-%02d", get(MONTH) + 1, get(DAY_OF_MONTH));
  }

  public String getTimeString() {
    return String.format("%02d:%02d", get(HOUR_OF_DAY), get(MINUTE));
  }

  /**
   * 日付の変わる時刻のインスタンスを取得する
   * 
   * @return
   */
  public AnimeCalendar beginningOfDay() {
    AnimeCalendar cloned = (AnimeCalendar) this.clone();
    cloned.set(HOUR_OF_DAY, BEGINNING_OF_DAY);
    cloned.set(MINUTE, 0);
    cloned.set(SECOND, 0);
    return cloned;
  }

  /**
   * 今日の日付を返す ただし5時~29時
   * 
   * @return
   */
  public static GregorianCalendar today() {
    AnimeCalendar now = new AnimeCalendar();
    return new GregorianCalendar(now.get(YEAR), now.get(MONTH), now.get(DAY_OF_MONTH), 0, 0, 0);
  }

  /**
   * 明日の日付のオブジェクトを返す
   * 
   * @return
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
