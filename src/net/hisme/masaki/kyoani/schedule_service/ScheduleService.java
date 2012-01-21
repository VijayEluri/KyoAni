package net.hisme.masaki.kyoani.schedule_service;

import java.util.ArrayList;

import net.hisme.masaki.kyoani.models.Schedule;

/**
 * @author masaki
 */
public interface ScheduleService {
  boolean login() throws NetworkUnavailableException;

  ArrayList<Schedule> getSchedules() throws LoginFailureException, NetworkUnavailableException;

  class LoginFailureException extends Exception {
    private static final long serialVersionUID = 1L;
  }

  class NetworkUnavailableException extends Exception {
    private static final long serialVersionUID = 1L;
  }
}
