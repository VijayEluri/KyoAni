package net.hisme.masaki.kyoani.schedule_service;

import java.util.ArrayList;

import net.hisme.masaki.kyoani.models.Schedule;
import net.hisme.masaki.kyoani.schedule_service.exception.LoginFailureException;
import net.hisme.masaki.kyoani.schedule_service.exception.NetworkUnavailableException;

/**
 * @author masarakki
 */
public interface ScheduleService {
  boolean login() throws NetworkUnavailableException;

  /**
   * fetch schedules by force reloading
   * 
   * @return
   */
  ArrayList<Schedule> reloadSchedules() throws LoginFailureException, NetworkUnavailableException;

  /**
   * get schedules
   * 
   * @return schedules
   */
  ArrayList<Schedule> getSchedules() throws LoginFailureException, NetworkUnavailableException;

  Schedule getNextSchedule() throws LoginFailureException, NetworkUnavailableException;
}
