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

  ArrayList<Schedule> getSchedules() throws LoginFailureException, NetworkUnavailableException;
}
