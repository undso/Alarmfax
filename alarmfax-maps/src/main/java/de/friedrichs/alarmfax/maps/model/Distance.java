package de.friedrichs.alarmfax.maps.model;

import java.math.BigInteger;

/**
 *
 * @author Alexander Friedrichs <friedrichs.alexander@gmail.com>
 */
public class Distance {

    private BigInteger value;
    private String text;

    public Distance() {
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

    @Override
    public String toString() {
        return "Distance{" + "value=" + value + ", text=" + text + '}';
    }

    public void setText(String text) {
        this.text = text;
    }

}
