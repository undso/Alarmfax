package de.friedrichs.alarmfax.maps.model;

/**
 *
 * @author Alexander Friedrichs <friedrichs.alexander@gmail.com>
 */
public class Polyline {

    private String points;

    public Polyline() {
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    @Override
    public String toString() {
        return "Polyline{" + "points=" + points + '}';
    }

}
