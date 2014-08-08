package de.friedrichs.alarmfax.maps.model;

/**
 *
 * @author Alexander Friedrichs <friedrichs.alexander@gmail.com>
 */
public class Bounds {

    private GeoLocation southwest;
    private GeoLocation northeast;

    public Bounds() {
    }

    public GeoLocation getSouthwest() {
        return southwest;
    }

    public void setSouthwest(GeoLocation southwest) {
        this.southwest = southwest;
    }

    public GeoLocation getNortheast() {
        return northeast;
    }

    public void setNortheast(GeoLocation northeast) {
        this.northeast = northeast;
    }    
    
}
