package de.friedrichs.alarmfax.camel;

import de.friedrichs.alarmfax.config.AlarmfaxProperties;
import de.friedrichs.alarmfax.handler.GoogleMapsProcessor;
import de.friedrichs.alarmfax.handler.OCRProcessor;
import de.friedrichs.alarmfax.ocr.exception.OCRException;
import java.io.File;
import java.io.FileOutputStream;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import javax.activation.DataHandler;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Alexander Friedrichs <friedrichs.alexander@gmail.com>
 */
public class CamelHandler {

    private static final Logger LOG = LoggerFactory.getLogger(CamelHandler.class);
    private static final String START = "Grabengasse 11, Rodheim vor der Höhe, Rosbach vor der Höhe";
    private static final String OCR_ERROR = "OCR_ERROR";
    private static final String DEV_MAIL = "friedrichs.alexander@gmail.com";
    private static final String DIRECT_PROCESS_GOOGLE = "direct:processGoogle";
    private static final String DIRECT_NO_ADRESS_ERROR = "direct:noAdressError";
    private static final String DIRECT_NO_TIFF_ERROR = "direct:noTiffError";
    private static final String DIRECT_SENDMAIL = "direct:sendMail";

    private final CamelContext camelContext;
    private final AlarmfaxProperties properties;

    public CamelHandler(AlarmfaxProperties properties) throws OCRException {
        this.properties = properties;
        this.camelContext = new DefaultCamelContext();
        this.camelContext.setStreamCaching(Boolean.TRUE);
    }

    public void start() throws Exception {
        createRoute();
        camelContext.start();
        LOG.info("Camel gestartet");
    }

    public void stop() throws Exception {
        camelContext.startRoute("shutdown");
        TimeUnit.SECONDS.sleep(5);
        camelContext.stop();
        LOG.info("Camel gestoppt");
    }

    private void createRoute() throws Exception {

        RouteBuilder routeBuilder;
        routeBuilder = new RouteBuilder() {

            @Override
            public void configure() throws Exception {
                
                StringBuilder imapURI = new StringBuilder("imap://");
                imapURI.append(properties.getProperty(AlarmfaxProperties.MAIL_IMAP_HOST))
                        .append("?username=").append(properties.getProperty(AlarmfaxProperties.MAIL_IMAP_USERNAME))
                        .append("&password=").append(properties.getProperty(AlarmfaxProperties.MAIL_IMAP_PASSWORD))
                        .append("&consumer.delay=").append(properties.getProperty(AlarmfaxProperties.MAIL_IMAP_DELAY))
                        .append("&maxMessagesPerPoll=1&delete=false&fetchSize=1");

                StringBuilder smtpURI = new StringBuilder("smtp://");
                smtpURI.append(properties.getProperty(AlarmfaxProperties.MAIL_IMAP_HOST))
                        .append("?username=").append(properties.getProperty(AlarmfaxProperties.MAIL_IMAP_USERNAME))
                        .append("&password=").append(properties.getProperty(AlarmfaxProperties.MAIL_IMAP_PASSWORD));

                
                onException(Exception.class)
                        .log(LoggingLevel.ERROR, "Exception wurde gefangen. ${exception.message}\n${exception.stacktrace}")
                                                .setBody().simple("Hallo,\n\n"
                                                        + "Es kam leider zu einer Exception:\n${exception.message}\n"
                                                        + "Stacktrace:\n"
                                                        + "${exception.stacktrace}\n"
                                                        + "\nDiese Mail wurde automatisch generiert...", String.class)
                                                .setHeader("contentType").constant("text/plain")
                                                .setHeader("To").constant("friedrichs.alexander@gmail.com")
                                                .setHeader("Subject").constant("[Alarmfax] Exception")
                                                .setHeader("From").constant(properties.getProperty(AlarmfaxProperties.MAIL_OUTGOING_FROM))
                                                .to(smtpURI.toString())
                        .handled(true)
                        .stop();

                
                from(imapURI.toString())
                        .removeHeaders("*")
                        .bean(new MyMailProcessor(), "process")
                        .choice()
                        .when(body().isInstanceOf(File.class))
                        .to(DIRECT_PROCESS_TIFF)
                        .otherwise()
                        .to(DIRECT_NO_TIFF_ERROR);

                from(DIRECT_PROCESS_TIFF)
                        .bean(new OCRProcessor())
                        .choice()
                        .when(header(OCR_ERROR).isNull())
                        .to(DIRECT_PROCESS_GOOGLE)
                        .otherwise()
                        .to(DIRECT_NO_ADRESS_ERROR);

                from(DIRECT_PROCESS_GOOGLE)
                        .bean(new GoogleMapsProcessor(START))
                        .setHeader("Subject").constant("Route zum Einsatzort")
                        .setBody().simple("Hallo,\n\n"
                                + "die Anfahrtsbeschreibung ist im Anhang.\n"
                                + "\nDiese Mail wurde automatisch generiert...", String.class)
                        .to(DIRECT_SENDMAIL);

                from(DIRECT_NO_ADRESS_ERROR)
                        .setHeader("Subject").constant("Fehler in der Verarbeitung des Alarmfax")
                        .setBody().simple("Hallo,\n\nleider konnte aus dem Fax keine Anschrift ermittelt werden.\n\n\nDiese Mail wurde automatisch generiert...", String.class)
                        .to(DIRECT_SENDMAIL);

                from(DIRECT_NO_TIFF_ERROR)
                        .setHeader("Subject").constant("Fehler in der Verarbeitung des Alarmfax")
                        .setBody().simple("Hallo,\n\nleider wurde kein Anhang vom Typ TIFF gefunden.\n\n\nDiese Mail wurde automatisch generiert...", String.class)
                        .to(DIRECT_SENDMAIL);

                from(DIRECT_SENDMAIL)                        
                        .setHeader("contentType").constant("text/plain")
                        .setHeader("To").constant(properties.getProperty(AlarmfaxProperties.MAIL_OUTGOING_TO))
                        .setHeader("Bcc").constant(DEV_MAIL)
                        .setHeader("From").constant(properties.getProperty(AlarmfaxProperties.MAIL_OUTGOING_FROM))
                        .to(smtpURI.toString().concat("&debugMode=false"))
                        .log(LoggingLevel.INFO, "Nachricht gesendet");

                from("timer:startup?repeatCount=1").routeId("startup")
                        .setHeader("contentType").constant("text/plain")
                        .setHeader("To").constant(DEV_MAIL)
                        .setHeader("Subject").constant("[Alarmfax] App Started!")
                        .setHeader("From").constant(properties.getProperty(AlarmfaxProperties.MAIL_OUTGOING_FROM))
                        .to(smtpURI.toString());

                from("timer:shutdown?repeatCount=1&delay=0").routeId("shutdown").autoStartup(false)
                        .setHeader("contentType").constant("text/plain")
                        .setHeader("To").constant(DEV_MAIL)
                        .setHeader("Subject").constant("[Alarmfax] App Stopped!")
                        .setHeader("From").constant(properties.getProperty(AlarmfaxProperties.MAIL_OUTGOING_FROM))
                        .to(smtpURI.toString());

            }
            private static final String DIRECT_PROCESS_TIFF = "direct:processTiff";

        };

        camelContext.addRoutes(routeBuilder);
    }

    public class MyMailProcessor {

        private static final String CONTENT_TYPE_TIF = "IMAGE/TIF";
        private static final String CONTENT_TYPE_TIFF = "IMAGE/TIFF";

        public void process(Exchange exchange) throws Exception {
            LOG.info("process in Instance {} aufgerufen", this);
            // the API is a bit clunky so we need to loop
            Map<String, DataHandler> attachments = exchange.getIn().getAttachments();
            Set<String> keys = new HashSet<>();
            for (Map.Entry<String, DataHandler> entry : attachments.entrySet()) {
                keys.add(entry.getKey());
                DataHandler dh = entry.getValue();
                // get the file name
                if (!dh.getContentType().toUpperCase().startsWith(CONTENT_TYPE_TIF)
                        && !dh.getContentType().toUpperCase().startsWith(CONTENT_TYPE_TIFF)) {
                    LOG.info("Content ist nicht TIFF sondern {}. Wird uebersprungen", dh.getContentType());
                    continue;
                }
                String filename = dh.getName();
                String folder = properties.getProperty(AlarmfaxProperties.SAVEPATH)
                        + File.separator
                        + UUID.randomUUID().toString();
                exchange.getIn().setHeader("storagePath", folder);
                FileUtils.forceMkdir(new File(folder));
                File tiffFile = new File(folder, filename);
                // get the content and convert it to byte[]
                byte[] data = exchange.getContext().getTypeConverter()
                        .convertTo(byte[].class, dh.getInputStream());
                try (FileOutputStream out = new FileOutputStream(tiffFile)) {
                    out.write(data);
                    out.flush();
                }

                LOG.info("{} wurde gespeichert.", tiffFile);

                exchange.getIn().setBody(tiffFile, File.class);
                break;
            }
            //Entfernen aller Attachments
            for (String key : keys) {
                exchange.getIn().removeAttachment(key);
            }
        }
    }

}
