package net.hisme.masaki.kyoani.models;

import java.util.ArrayList;

public interface ScheduleService {
    boolean login() throws NetworkUnavailableException;

    ArrayList<Schedule> getSchedules() throws LoginFailureException,
            NetworkUnavailableException;

    class LoginFailureException extends Exception {
        private static final long serialVersionUID = 1L;
    }

    class NetworkUnavailableException extends Exception {
        private static final long serialVersionUID = 1L;
    }
}
