package de.friedrichs.alarmfax.config;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Alexander Friedrichs <friedrichs.alexander@gmail.com>
 */
public class AlarmfaxPropertiesTest {
    
    private static final String TARGET_PATH = "target/props";
    private AlarmfaxProperties properties;
    
    public AlarmfaxPropertiesTest() {
    }
    
    
    @Before
    public void setUp() throws IOException {
        properties = new AlarmfaxProperties();
        FileUtils.forceMkdir(new File(TARGET_PATH));
        FileUtils.cleanDirectory(new File(TARGET_PATH));
    }
    
    @Test
    public void testSaveProps() throws IOException {
        properties.put("TEST", "FOO");
        try(FileOutputStream fos = new FileOutputStream(new File(TARGET_PATH, "props.properties"))){
            properties.store(fos, "Test Properties");
        }
        try(FileOutputStream fos = new FileOutputStream(new File(TARGET_PATH, "props.xml"))){
            properties.storeToXML(fos, "Test XML");
        }
        
        assertTrue(new File(TARGET_PATH, "props.xml").exists());
        assertTrue(new File(TARGET_PATH, "props.properties").exists());
    }
    
}
