package de.friedrichs.alarmfax.handler;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Alexander Friedrichs <friedrichs.alexander@gmail.com>
 */
public class GoogleMapsProcessorTest {

    private final GoogleMapsProcessor processor;
    
    public GoogleMapsProcessorTest() {
        processor = new GoogleMapsProcessor("Zeil 112, 60313 Frankfurt am Main");
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of process method, of class GoogleMapsProcessor.
     */
    @Test
    public void testProcess() {
        assertTrue(true);
    }

    

}
