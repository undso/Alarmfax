package de.friedrichs.alarmfax.maps.model;

import java.util.List;

/**
 *
 * @author Alexander Friedrichs <friedrichs.alexander@gmail.com>
 */
public class DirectionResponse {

    private String status;
    private List<Route> routes;

    public DirectionResponse() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Route> getRoutes() {
        return routes;
    }

    public void setRoutes(List<Route> routes) {
        this.routes = routes;
    }

    @Override
    public String toString() {
        return "DirectionResponse{" + "status=" + status + ", routes=" + routes + '}';
    }

}
