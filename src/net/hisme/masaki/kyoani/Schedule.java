package net.hisme.masaki.kyoani;

import java.io.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import android.content.Context;
import android.util.Log;

public class Schedule {
	private String channel;
	private String name;
	private GregorianCalendar start;

	public Schedule(String channel, String name, String start) {
		GregorianCalendar now = new GregorianCalendar();

		GregorianCalendar today = new GregorianCalendar(now.get(Calendar.YEAR), now
				.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH), now
				.get(Calendar.HOUR_OF_DAY) - 6, 0, 0);
		String[] times = start.split(":");
		GregorianCalendar _start = new GregorianCalendar(today.get(Calendar.YEAR),
				today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH), Integer
						.parseInt(times[0]), Integer.parseInt(times[1]), 0);
		this.channel = channel;
		this.name = name;
		this.start = _start;
	}

	public Schedule(String channel, String name, GregorianCalendar start) {
		this.channel = channel;
		this.name = name;
		this.start = start;
	}

	public String getChannel() {
		return this.channel;
	}

	public String getName() {
		return this.name;
	}

	public GregorianCalendar getStart() {
		return this.start;
	}

	public String getStartString() {
		int h = start.get(Calendar.HOUR_OF_DAY);
		int m = start.get(Calendar.MINUTE);
		if (h < 6)
			h += 24;

		return String.format("%02d:%02d", h, m);
	}

	public static File getFile(Context context) {
		return context.getFileStreamPath("today_list");
	}

	public static boolean saveSchedules(Context context,
			ArrayList<Schedule> schedules) {
		boolean ret = false;
		try {
			ObjectOutputStream writer = new ObjectOutputStream(new FileOutputStream(
					Schedule.getFile(context)));
			writer.writeObject(schedules);
			ret = true;
		} catch (FileNotFoundException e) {
			log("FileNotFound in save");
		} catch (IOException e) {
			log("IOException in save");
		}
		return ret;
	}

	public static ArrayList<Schedule> loadSchedules(Context context) {
		ArrayList<Schedule> schedules = new ArrayList<Schedule>();
		try {
			ObjectInputStream reader = new ObjectInputStream(new FileInputStream(
					Schedule.getFile(context)));
			schedules = (ArrayList<Schedule>) reader.readObject();
		} catch (FileNotFoundException e) {
			log("FileNotFound in load");
		} catch (ClassNotFoundException e) {
			log("ClassNotFound in load");
		} catch (IOException e) {
			log("IOException in load");
		}
		return schedules;
	}

	private static void log(int n) {
		log(new Integer(n).toString());
	}

	private static void log(String str) {
		Log.d("KyoAni", "[ScheduleListActivity] " + str);
	}

	public String toString() {
		return "[Schedule] channel = " + channel + "; name = " + name
				+ "; start = " + start;
	}
}
