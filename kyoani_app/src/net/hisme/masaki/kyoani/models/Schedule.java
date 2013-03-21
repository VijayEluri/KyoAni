package net.hisme.masaki.kyoani.models;

import java.io.*;
import java.util.ArrayList;
import net.hisme.masaki.kyoani.App;

/**
 * Animation Program
 * 
 * @author masarakki
 * 
 */
public class Schedule implements Serializable {
  public static String LIST_FILE = "list.obj";
  private String channel;
  private String name;
  private AnimeCalendar start;
  static final long serialVersionUID = 2;

  /**
   * 
   * @param channel
   *          channel name
   * @param name
   *          program name
   * @param start
   *          start time like '25:30'
   */
  public Schedule(String channel, String name, String start) {
    this.channel = channel;
    this.name = name;
    this.start = new AnimeCalendar(start);
  }

  /**
   * 
   * @param channel
   *          channel name
   * @param name
   *          program name
   * @param start
   *          instance of AnimeCalendar
   */
  public Schedule(String channel, String name, AnimeCalendar start) {
    this.channel = channel;
    this.name = name;
    this.start = start;
  }

  /**
   * @return channel name
   */
  public String getChannel() {
    return this.channel;
  }

  /**
   * @return program name
   */
  public String getName() {
    return this.name;
  }

  /**
   * @return start time
   */
  public AnimeCalendar getStart() {
    return (AnimeCalendar) this.start.clone();
  }

  /**
   * @return start time as string
   */
  public String getStartString() {
    return this.start.getTimeString();
  }

  /**
   * store the schedules to storage
   * 
   * @param schedules
   *          Schedules
   * @return succeeded?
   */
  public static boolean saveSchedules(ArrayList<Schedule> schedules) {
    boolean ret = false;
    try {
      ObjectOutputStream writer = new ObjectOutputStream(App.li.openFileOutput(LIST_FILE, 0));
      writer.writeObject(schedules);
      writer.close();
      ret = true;
    } catch (FileNotFoundException e) {
      log("FileNotFound in save");
    } catch (IOException e) {
      log("IOException in save");
    }
    return ret;
  }

  /**
   * load the schedules from storage
   * 
   * @return schedules
   */
  @SuppressWarnings("unchecked")
  public static ArrayList<Schedule> loadSchedules() {
    ArrayList<Schedule> schedules = new ArrayList<Schedule>();
    try {
      ObjectInputStream reader = new ObjectInputStream(App.li.openFileInput(LIST_FILE));
      schedules = (ArrayList<Schedule>) reader.readObject();
      reader.close();
    } catch (FileNotFoundException e) {
      log("FileNotFound in load");
    } catch (ClassNotFoundException e) {
      log("ClassNotFound in load");
    } catch (IOException e) {
      log("IOException in load");
    }
    return schedules;
  }

  private static void log(String str) {
    App.Log.d("[Schedule] " + str);
  }

  public String toString() {
    return "[Schedule] channel = " + this.channel + "; name = " + this.name + "; start = " + this.start.getTimeString();
  }
}
