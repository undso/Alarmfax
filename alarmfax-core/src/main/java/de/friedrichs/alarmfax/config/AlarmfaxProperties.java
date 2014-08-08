package de.friedrichs.alarmfax.config;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 *
 * @author Alexander Friedrichs <friedrichs.alexander@gmail.com>
 */
public class AlarmfaxProperties extends Properties {

    public static final String SAVEPATH = "alarmfax.savepath";
    public static final String MAIL_IMAP_HOST = "alarmfax.mail.imap.host";
    public static final String MAIL_IMAP_USERNAME = "alarmfax.mail.imap.username";
    public static final String MAIL_IMAP_PASSWORD = "alarmfax.mail.imap.password";
    public static final String MAIL_IMAP_DELAY = "alarmfax.mail.imap.delay";
    public static final String MAIL_OUTGOING_TO = "alarmfax.mail.outgoing.to";
    public static final String MAIL_OUTGOING_FROM = "alarmfax.mail.outgoing.from";
    public static final String MAP_START_ADDRESS = "alarmfax.map.startaddress";

    private static final Map<String, String> DEFAULTS;

    static {
        DEFAULTS = new HashMap<>();
        DEFAULTS.put(SAVEPATH, "/sample/dir");
        DEFAULTS.put(MAIL_IMAP_HOST, "mail.host.de");
        DEFAULTS.put(MAIL_IMAP_USERNAME, "mail@host.de");
        DEFAULTS.put(MAIL_IMAP_PASSWORD, "1234");
        DEFAULTS.put(MAIL_OUTGOING_FROM, "MAIL-SENDER <mail-sender@host.de>");
        DEFAULTS.put(MAIL_OUTGOING_TO, "someone@anymail.com");
        DEFAULTS.put(MAIL_IMAP_DELAY, "10000");
        DEFAULTS.put(MAP_START_ADDRESS, "Zeil 112, 60313 Frankfurt am Main");
    }

    public AlarmfaxProperties() {
        super();
        defaults = new Properties();
        defaults.putAll(DEFAULTS);
    }

}
