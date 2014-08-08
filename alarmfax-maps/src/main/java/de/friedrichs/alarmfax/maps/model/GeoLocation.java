package de.friedrichs.alarmfax.maps.model;

import java.math.BigDecimal;

/**
 *
 * @author Alexander Friedrichs <friedrichs.alexander@gmail.com>
 */
public class GeoLocation {

    private BigDecimal lat;
    private BigDecimal lng;

    public GeoLocation() {
    }

    public BigDecimal getLat() {
        return lat;
    }

    public void setLat(BigDecimal lat) {
        this.lat = lat;
    }

    public BigDecimal getLng() {
        return lng;
    }

    public void setLng(BigDecimal lng) {
        this.lng = lng;
    }

    @Override
    public String toString() {
        return "GeoLocation{" + "lat=" + lat + ", lng=" + lng + '}';
    }

}
