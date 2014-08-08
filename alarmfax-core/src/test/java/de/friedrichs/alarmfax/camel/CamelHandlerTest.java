package de.friedrichs.alarmfax.camel;

import java.io.File;
import java.util.UUID;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Alexander Friedrichs <friedrichs.alexander@gmail.com>
 */
public class CamelHandlerTest {
    
    private static final Logger LOG = LoggerFactory.getLogger(CamelHandlerTest.class);
    
    public CamelHandlerTest() {
    }

    @Test
    public void testUUID() {
        String uuid1 = UUID.randomUUID().toString();
        String uuid2 = UUID.randomUUID().toString();
        LOG.info("UUID1: {} UUID2: {}", uuid1, uuid2);
        Assert.assertNotSame(uuid1, uuid2);
    }
    
}
