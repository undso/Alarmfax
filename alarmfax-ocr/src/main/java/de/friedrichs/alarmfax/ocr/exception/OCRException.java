package de.friedrichs.alarmfax.ocr.exception;

/**
 *
 * @author Alexander Friedrichs <friedrichs.alexander@gmail.com>
 */
public class OCRException extends Exception {

    public OCRException(String message) {
        super(message);
    }

    public OCRException(String message, Throwable throwable) {
        super(message, throwable);
    }

}
