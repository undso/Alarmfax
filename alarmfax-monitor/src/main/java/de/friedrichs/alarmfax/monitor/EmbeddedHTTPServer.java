package de.friedrichs.alarmfax.monitor;

import com.sun.jersey.api.container.httpserver.HttpServerFactory;
import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.api.core.ResourceConfig;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URI;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.core.UriBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Alexander Friedrichs <friedrichs.alexander@gmail.com>
 */
public class EmbeddedHTTPServer {

    private static final Logger LOG = LoggerFactory.getLogger(EmbeddedHTTPServer.class);
    private static final int PORT = 666;
    private static Map<Integer, EmbeddedHTTPServer> INSTANCES = new HashMap<>();
    private int _port = PORT;
    private HttpServer httpServer;

    private EmbeddedHTTPServer(int port) {
        _port = port;        
    }
    
    public static synchronized EmbeddedHTTPServer getInstance(){
        return getInstance(PORT);
    }
    
    public static synchronized EmbeddedHTTPServer getInstance(int port){
        if(INSTANCES.get(port) == null){
            INSTANCES.put(port, new EmbeddedHTTPServer(port));
        }
        return INSTANCES.get(port);
    }

    public void startServer(){
        this.getHttpServer().start();
    }
    
    public void stopServer(int i){
        if(httpServer != null){
            httpServer.stop(i);
        }
    }
    
    protected HttpServer getHttpServer() {
        if (httpServer == null) {            
            ResourceConfig resourceConfig = new PackagesResourceConfig("de.friedrichs.alarmfax.monitor.rest");
            try{
                httpServer = HttpServerFactory.create(getURI(), resourceConfig);
            }catch(IOException ioe){
                LOG.error("Server konnte nicht erzeugt werden.", ioe);
            }
        }
        return httpServer;
    }
    
    protected URI getURI() {
        return UriBuilder.fromUri("http://" + getHostName()+ "/").port(_port).build();
    }

    protected String getHostName() {
        String hostName = "localhost";
        try {
            hostName = InetAddress.getLocalHost().getCanonicalHostName();
        } catch (UnknownHostException e) {
            LOG.warn("Konnte HostName nicht ermitteln.", e);
        }
        return hostName;
    }

}
