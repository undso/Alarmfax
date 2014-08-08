package de.friedrichs.alarmfax.handler;

import de.friedrichs.alarmfax.maps.MapsHandler;
import java.io.File;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Alexander Friedrichs <friedrichs.alexander@gmail.com>
 */
public class GoogleMapsProcessor implements Processor {

    private static final Logger LOG = LoggerFactory.getLogger(GoogleMapsProcessor.class);

    private static final String ORT = "ORT";
    private static final String ORTSTEIL = "ORTSTEIL";
    private static final String STRASSE = "STRASSE";

    private final MapsHandler mapsHandler;
    
    public GoogleMapsProcessor(String routeStart) {
        this.mapsHandler = new MapsHandler(routeStart);
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        StringBuilder builder = new StringBuilder();
        if (!exchange.getIn().getHeader(STRASSE, String.class).isEmpty()) {
            builder.append(exchange.getIn().getHeader(STRASSE, String.class));
        }
        if (!exchange.getIn().getHeader(ORTSTEIL, String.class).isEmpty()) {
            builder.append(", ").append(exchange.getIn().getHeader(ORTSTEIL, String.class));
        }
        if (!exchange.getIn().getHeader(ORT, String.class).isEmpty()) {
            builder.append(", ").append(exchange.getIn().getHeader(ORT, String.class));
        }
        LOG.info("Create Map to {}", builder.toString());
        
        //Move File to new location
        File googleMap = mapsHandler.createRoute(builder.toString());
        File folder = new File(exchange.getIn().getHeader("storagePath", String.class));
        File route = new File(folder, "Route.png");
        FileUtils.copyFile(googleMap, route);
        FileUtils.deleteQuietly(googleMap);
        
        exchange.getIn().addAttachment(route.getName(), new DataHandler(new FileDataSource(route)));
        LOG.info("{} wurde an den Exchange angehaengt.", route.getAbsolutePath());
    }

}
