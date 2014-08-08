package de.friedrichs.alarmfax.maps.model;

import java.math.BigInteger;

/**
 *
 * @author Alexander Friedrichs <friedrichs.alexander@gmail.com>
 */
public class Duration {

    private BigInteger value;
    private String text;

    public Duration() {
    }

    public BigInteger getValue() {
        return value;
    }

    public void setValue(BigInteger value) {
        this.value = value;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "Duration{" + "value=" + value + ", text=" + text + '}';
    }

}
