package net.hisme.masaki.kyoani.models;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 * 25時 などの表記に特化したカレンダークラス
 * 
 * @author masarakki
 */
public class AnimeCalendar extends GregorianCalendar {
    private static final long serialVersionUID = 1L;

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
        return super.get(HOUR_OF_DAY) < 5;
    }

    public String getTimeString() {
        return String.format("%02d:%02d", get(HOUR_OF_DAY), get(MINUTE));
    }

    /**
     * 今日の日付を返す ただし5時~29時
     * 
     * @return
     */
    public static GregorianCalendar today() {
        AnimeCalendar now = new AnimeCalendar();
        return new GregorianCalendar(now.get(Calendar.YEAR), now
                .get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
    }
}
