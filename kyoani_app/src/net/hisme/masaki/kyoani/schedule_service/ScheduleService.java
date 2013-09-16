package net.hisme.masaki.kyoani.schedule_service;

import net.hisme.masaki.kyoani.models.Schedules;
import net.hisme.masaki.kyoani.schedule_service.exception.LoginFailureException;
import net.hisme.masaki.kyoani.schedule_service.exception.NetworkUnavailableException;

/**
 * @author masarakki
 */
public interface ScheduleService {

  /**
   * fetch schedules by force reloading
   * 
   * @return
   */
  Schedules reloadSchedules() throws LoginFailureException, NetworkUnavailableException;

  /**
   * get schedules
   * 
   * @return schedules
   */
  Schedules getSchedules() throws LoginFailureException, NetworkUnavailableException;
}
