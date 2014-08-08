package de.friedrichs.alarmfax.maps.model;

import java.util.List;

/**
 *
 * @author Alexander Friedrichs <friedrichs.alexander@gmail.com>
 */
public class Leg {

    private List<Step> steps;
    private Duration duration;
    private Distance distance;
    private GeoLocation start_location;
    private GeoLocation end_location;
    private String start_address;
    private String end_address;

    public Leg() {
    }

    public List<Step> getSteps() {
        return steps;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public Distance getDistance() {
        return distance;
    }

    public void setDistance(Distance distance) {
        this.distance = distance;
    }

    public GeoLocation getStart_location() {
        return start_location;
    }

    public void setStart_location(GeoLocation start_location) {
        this.start_location = start_location;
    }

    public GeoLocation getEnd_location() {
        return end_location;
    }

    public void setEnd_location(GeoLocation end_location) {
        this.end_location = end_location;
    }

    public String getStart_address() {
        return start_address;
    }

    public void setStart_address(String start_address) {
        this.start_address = start_address;
    }

    public String getEnd_address() {
        return end_address;
    }

    public void setEnd_address(String end_address) {
        this.end_address = end_address;
    }

    @Override
    public String toString() {
        return "Leg{" + "steps=" + steps + ", duration=" + duration + ", distance=" + distance + ", start_location=" + start_location + ", end_location=" + end_location + ", start_address=" + start_address + ", end_address=" + end_address + '}';
    }

    
}
