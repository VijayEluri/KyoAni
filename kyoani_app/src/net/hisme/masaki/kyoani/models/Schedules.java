package net.hisme.masaki.kyoani.models;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import net.hisme.masaki.kyoani.App;

/**
 * collection of schedule
 * 
 * @author masarakki
 */
public class Schedules extends ArrayList<Schedule> {
  private static final long serialVersionUID = 1L;
  private static final String filename = "list.obj";

  /**
   * store the schedules to storage
   * 
   * @param schedules
   *          Schedules
   * @return succeeded?
   */
  public boolean save() {
    try {
      ObjectOutputStream writer = new ObjectOutputStream(App.li.openFileOutput(filename, 0));
      writer.writeObject(this);
      writer.close();
      return true;
    } catch (FileNotFoundException e) {
      log("FileNotFound in save");
    } catch (IOException e) {
      log("IOException in save");
    }
    return false;
  }

  /**
   * load the schedules from storage
   * 
   * @return schedules
   */
  public static Schedules load() {
    Schedules schedules = new Schedules();
    try {
      ObjectInputStream reader = new ObjectInputStream(App.li.openFileInput(filename));
      schedules = (Schedules) reader.readObject();
      reader.close();
    } catch (FileNotFoundException e) {
      log("Class Cast Exception");
    } catch (ClassNotFoundException e) {
      log("ClassNotFound in load");
    } catch (IOException e) {
      log("IOException in load");
    } catch (ClassCastException e) {
      schedules.save();
    }
    return schedules;
  }

  public Schedule next() {
    AnimeCalendar now = new AnimeCalendar();
    for (Schedule schedule : this) {
      if (now.compareTo(schedule.getStart()) == -1) {
        return schedule;
      }
    }
    return null;
  }

  private static void log(String str) {
    App.Log.d("[Schedules] " + str);
  }
}
