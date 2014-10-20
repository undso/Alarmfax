package de.friedrichs.alarmfax.monitor;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpServer;
import de.friedrichs.alarmfax.monitor.model.Status;
import java.io.IOException;
import java.io.InputStreamReader;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Alexander Friedrichs <friedrichs.alexander@gmail.com>
 */
public class EmbeddedHTTPServerTest {

    private static final Logger LOG = LoggerFactory.getLogger(EmbeddedHTTPServerTest.class);
    
    public EmbeddedHTTPServerTest() {        
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        EmbeddedHTTPServer.getInstance().startServer();
    }

    @After
    public void tearDown() {
        EmbeddedHTTPServer.getInstance().stopServer(0);
    }

    /**
     * Test of getHttpServer method, of class EmbeddedHTTPServer.
     *
     * @throws java.lang.InterruptedException
     * @throws java.io.IOException
     */
    @Test
    public void testGetHttpServer() throws InterruptedException, IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpget = new HttpGet("http://localhost:666/mon");
        LOG.info("HttpGet Erzeugt: {}", httpget);
        CloseableHttpResponse response = httpclient.execute(httpget);
        LOG.info("Response: {}", response);
        try {
            assertEquals(200, response.getStatusLine().getStatusCode());
            HttpEntity entity = response.getEntity();
            Status status = new Gson().fromJson(new InputStreamReader(entity.getContent()), Status.class);
            LOG.info("Status: {}", status);
            assertNotNull(status);
            assertNotNull(status.getPid());
        } catch (IOException io) {
            fail(io.getLocalizedMessage());
        } finally {
            if (response != null) {
                response.close();
            }
        }

    }

}
