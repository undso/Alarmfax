package de.friedrichs.alarmfax;

import de.friedrichs.alarmfax.camel.CamelHandler;
import de.friedrichs.alarmfax.config.AlarmfaxProperties;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 *
 */
public class App implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(App.class);
    public static volatile boolean KEEPRUNNING = true;
    private final AlarmfaxProperties properties;
    private CamelHandler camelHandler;

    public static void main(String[] args) {
        new Thread(new App(args)).start();
    }

    public App(String[] args) {
        this.properties = new AlarmfaxProperties();
        try {
            loadProperties(args);
        } catch (IOException | IllegalArgumentException ioe) {
            LOG.error("Fehler beim Laden der Properties", ioe);
            System.exit(1);
        }

    }

    @Override
    public void run() {
        try {
            this.camelHandler = new CamelHandler(properties);
            LOG.info("Starte Camel...");
            this.camelHandler.start();
            this.registerShutdownHook();
            while (KEEPRUNNING) {
                TimeUnit.SECONDS.sleep(5);
                if (Thread.interrupted()) {
                    LOG.info("Hauptthread interrupted");
                    System.exit(0);
                }
            }
        } catch (Exception ocre) {
            LOG.error("UPS", ocre);
        }
    }

    protected void registerShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                try {
                    LOG.info("ShutdownHook ...");
                    camelHandler.stop();
                } catch (Exception ie) {
                    LOG.error("Camel konnte nicht sauber beendet werden.", ie);
                }
            }
        });
    }

    private void loadProperties(String[] args) throws IOException {
        if (args.length > 0) {
            LOG.info("Lade Properties aus Datei {}", args[0]);
            File props = new File(args[0]);
            if (!props.exists()) {
                throw new IllegalArgumentException("Datei " + props.getAbsolutePath() + " existiert nicht");
            }
            LOG.debug("Lade Properties...");
            
            try (FileInputStream fis = new FileInputStream(props) ) {

                if (props.getName().endsWith("properties")) {
                    properties.load(fis);
                } else if (props.getName().endsWith("xml")) {
                    properties.loadFromXML(fis);
                } else {
                    throw new IllegalArgumentException("Die Properties muessen als XML oder Properties Datei uebergeben werden.");
                }
            }
        }
    }
}
