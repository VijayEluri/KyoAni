package net.hisme.masaki.kyoani.models;

import java.io.Serializable;

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

  public String toString() {
    return "[Schedule] channel = " + this.channel + "; name = " + this.name + "; start = " + this.start.getTimeString();
  }
}
