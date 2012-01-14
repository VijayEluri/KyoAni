package net.hisme.masaki.kyoani.models;

import java.io.*;
import java.util.ArrayList;
import net.hisme.masaki.kyoani.App;

/**
 * 番組単体を表すクラス
 * 
 * @author masaki
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
   *          チャンネル名
   * @param name
   *          番組名
   * @param start
   *          25:30のような時間を表す文字列
   */
  public Schedule(String channel, String name, String start) {
    this.channel = channel;
    this.name = name;
    this.start = new AnimeCalendar(start);
  }

  /**
   * 
   * @param channel
   *          チャンネル名
   * @param name
   *          番組名
   * @param start
   *          AnimeCalendarのインスタンス
   */
  public Schedule(String channel, String name, AnimeCalendar start) {
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

  public AnimeCalendar getStart() {
    return (AnimeCalendar) this.start.clone();
  }

  public String getStartString() {
    return this.start.getTimeString();
  }

  /**
   * ストレージにスケジュールを保存する
   * 
   * @param schedules
   *          ScheduleのArrayList
   * @return 保存に成功したかどうか
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
   * ストレージからスケジュールをロードする
   * 
   * @return ScheduleのArrayList
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
