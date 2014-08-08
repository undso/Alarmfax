package de.friedrichs.alarmfax.maps.exception;

/**
 *
 * @author Alexander Friedrichs <friedrichs.alexander@gmail.com>
 */
public class MapsException extends Exception {

    public MapsException(String message) {
        super(message);
    }

    public MapsException(String message, Throwable throwable) {
        super(message, throwable);
    }

}
