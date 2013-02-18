package net.hisme.masaki.kyoani.schedule_service;

import java.util.ArrayList;

import net.hisme.masaki.kyoani.models.Schedule;
import net.hisme.masaki.kyoani.schedule_service.exception.LoginFailureException;
import net.hisme.masaki.kyoani.schedule_service.exception.NetworkUnavailableException;

/**
 * @author masaki
 */
public interface ScheduleService {
  boolean login() throws NetworkUnavailableException;

  /**
   * 強制リロードしてスケジュールを取得
   * 
   * @return
   */
  ArrayList<Schedule> reloadSchedules() throws LoginFailureException, NetworkUnavailableException;

  /**
   * スケジュールを取得
   * 
   * @return
   */
  ArrayList<Schedule> getSchedules() throws LoginFailureException, NetworkUnavailableException;

  Schedule getNextSchedule() throws LoginFailureException, NetworkUnavailableException;
}
