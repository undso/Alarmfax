package de.friedrichs.alarmfax.maps.model;

import java.math.BigInteger;
import java.util.List;

/**
 *
 * @author Alexander Friedrichs <friedrichs.alexander@gmail.com>
 */
public class Route {

    private String summary;
    private List<Leg> legs;
    private String copyrights;
    private Polyline overview_polyline;
    private List<String> warnings;
    private List<BigInteger> waypoint_order;
    private Bounds bounds;

    public Route() {
    }
    
    public String printText(){
        StringBuilder builder = new StringBuilder();
        for (Leg leg : legs) {
            for (Step step : leg.getSteps()) {
                builder.append(step.getHtml_instructions()).append(System.lineSeparator());
            }
        }
        return builder.toString();
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public List<Leg> getLegs() {
        return legs;
    }

    public void setLegs(List<Leg> legs) {
        this.legs = legs;
    }

    public String getCopyrights() {
        return copyrights;
    }

    public void setCopyrights(String copyrights) {
        this.copyrights = copyrights;
    }

    public Polyline getOverview_polyline() {
        return overview_polyline;
    }

    public void setOverview_polyline(Polyline overview_polyline) {
        this.overview_polyline = overview_polyline;
    }

    public List<String> getWarnings() {
        return warnings;
    }

    public void setWarnings(List<String> warnings) {
        this.warnings = warnings;
    }

    public List<BigInteger> getWaypoint_order() {
        return waypoint_order;
    }

    public void setWaypoint_order(List<BigInteger> waypoint_order) {
        this.waypoint_order = waypoint_order;
    }

    public Bounds getBounds() {
        return bounds;
    }

    public void setBounds(Bounds bounds) {
        this.bounds = bounds;
    }

    @Override
    public String toString() {
        return "Route{" + "summary=" + summary + ", legs=" + legs + ", copyrights=" + copyrights + ", overview_polyline=" + overview_polyline + ", warnings=" + warnings + ", waypoint_order=" + waypoint_order + ", bounds=" + bounds + '}';
    }
        
}
