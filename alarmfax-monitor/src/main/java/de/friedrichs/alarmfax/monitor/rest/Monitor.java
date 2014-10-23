package de.friedrichs.alarmfax.monitor.rest;

import de.friedrichs.alarmfax.monitor.model.Status;
import java.lang.management.ManagementFactory;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author Alexander Friedrichs <friedrichs.alexander@gmail.com>
 */
@Path("mon")
public class Monitor {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Status get() {
        Status s = new Status();
        String name = ManagementFactory.getRuntimeMXBean().getName();
        s.setPid(name);
        return s;
    }

}
