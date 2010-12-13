package net.hisme.masaki.kyoani.models;

import java.util.ArrayList;

public interface ScheduleService {
    public ArrayList<Schedule> getSchedules() throws LoginFailureException,
            NetworkUnavailableException;

    public class LoginFailureException extends Exception {
        private static final long serialVersionUID = 1L;
    }

    public class NetworkUnavailableException extends Exception {
        private static final long serialVersionUID = 1L;
    }
}
