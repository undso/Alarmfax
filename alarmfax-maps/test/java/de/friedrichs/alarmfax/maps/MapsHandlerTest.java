package de.friedrichs.alarmfax.maps;

import de.friedrichs.alarmfax.maps.exception.MapsException;
import de.friedrichs.alarmfax.maps.model.DirectionResponse;
import org.junit.Test;
import static org.junit.Assert.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Alexander Friedrichs <friedrichs.alexander@gmail.com>
 */
public class MapsHandlerTest {

    private static final Logger LOG = LoggerFactory.getLogger(MapsHandlerTest.class);
    private final MapsHandler handler;

    public MapsHandlerTest() {
        handler = new MapsHandler("Zeil 112, 60313 Frankfurt am Main");
    }

    @Test
    public void testGetDirectionFromGoogle() {
        String ziel = "Fe1dbergstraße 13, Rodheim v. d. Höhe, Rosbach v. d. Höhe";
        DirectionResponse directionFromGoogle = null;
        try {
            directionFromGoogle = handler.getDirectionFromGoogle(handler.getStart(), ziel);
        }catch(MapsException me){
            LOG.error(me.getLocalizedMessage());
            fail(me.getLocalizedMessage());
        }
        
        assertNotNull(directionFromGoogle);
        LOG.info(directionFromGoogle.toString());
        
        ziel = ", Rodheim v. d. Höhe, Rosbach v. d. Höhe";
        directionFromGoogle = null;
        try {
            directionFromGoogle = handler.getDirectionFromGoogle(handler.getStart(), ziel);
        }catch(MapsException me){
            LOG.error(me.getLocalizedMessage());
            fail();
        }
        
        assertNotNull(directionFromGoogle);
        LOG.info(directionFromGoogle.toString());
        
        ziel = "L3415 --> Petterweiler Straße, Rodheim v. d. Höhe, Rosbach v. d. Höhe";
        directionFromGoogle = null;
        try {
            directionFromGoogle = handler.getDirectionFromGoogle(handler.getStart(), ziel);
        }catch(MapsException me){
            LOG.error(me.getLocalizedMessage());
            fail();
        }
        
        assertNotNull(directionFromGoogle);
        LOG.info(directionFromGoogle.toString());
    }

}
