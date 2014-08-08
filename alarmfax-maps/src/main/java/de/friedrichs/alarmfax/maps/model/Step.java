package de.friedrichs.alarmfax.maps.model;

/**
 *
 * @author Alexander Friedrichs <friedrichs.alexander@gmail.com>
 */
public class Step {
    
    private String travel_mode;
    private GeoLocation start_location;
    private GeoLocation end_location;
    private Polyline polyline;
    private Duration duration;
    private String html_instructions;
    private Distance distance;

    public Step() {
    }

    public String getTravel_mode() {
        return travel_mode;
    }

    public void setTravel_mode(String travel_mode) {
        this.travel_mode = travel_mode;
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

    public Polyline getPolyline() {
        return polyline;
    }

    public void setPolyline(Polyline polyline) {
        this.polyline = polyline;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public String getHtml_instructions() {
        return html_instructions;
    }

    public void setHtml_instructions(String html_instructions) {
        this.html_instructions = html_instructions;
    }

    public Distance getDistance() {
        return distance;
    }

    public void setDistance(Distance distance) {
        this.distance = distance;
    }

    @Override
    public String toString() {
        return "Step{" + "travel_mode=" + travel_mode + ", start_location=" + start_location + ", end_location=" + end_location + ", polyline=" + polyline + ", duration=" + duration + ", html_instructions=" + html_instructions + ", distance=" + distance + '}';
    }
    
    
}
