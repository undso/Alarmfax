package de.friedrichs.alarmfax.maps;

import com.google.gson.Gson;
import de.friedrichs.alarmfax.maps.exception.MapsException;
import de.friedrichs.alarmfax.maps.model.DirectionResponse;
import de.friedrichs.alarmfax.maps.model.Leg;
import de.friedrichs.alarmfax.maps.model.Route;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Alexander Friedrichs <friedrichs.alexander@gmail.com>
 */
public class MapsHandler {

    private static final Logger LOG = LoggerFactory.getLogger(MapsHandler.class);
    private final Gson gson;
    private final String start;

    public MapsHandler(String start) {
        this.gson = new Gson();
        this.start = start;
    }

    public File createRoute(String target) throws MapsException {
        DirectionResponse directionFromGoogle = getDirectionFromGoogle(getStart(), target);
        if (directionFromGoogle.getRoutes().isEmpty()) {
            throw new MapsException("Keine Routen von Google fuer die Strecke von '" + getStart() + "' nach '" + target + "' erhalten!");
        }
        LOG.info("Google lieferte {} Route(n).", directionFromGoogle.getRoutes().size());
        return getGoogleMap(directionFromGoogle.getRoutes().get(0));
    }

    protected File getGoogleMap(Route route) throws MapsException {
        URI uri;
        try {
            uri = new URIBuilder()
                    .setScheme("http")
                    .setHost("maps.googleapis.com")
                    .setPath("/maps/api/staticmap")
                    .setParameter("size", "640x640")
                    .setParameter("scale", "2")
                    .setParameter("sensor", "false")
                    .setParameter("markers", createMarker(route))
                    .setParameter("path", createPath(route))
                    .build();
        } catch (URISyntaxException ex) {
            LOG.error("Fehler beim Erzeugen der URI.", ex);
            throw new MapsException("Fehler beim Erzeugen der URI.", ex);
        }

        LOG.info("Get Map from URI: {}", uri.toString());

        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet get = new HttpGet(uri);
        File pngFile;

        try (CloseableHttpResponse response = httpclient.execute(get)) {

            pngFile = File.createTempFile("MAP_", ".png");

            try (FileOutputStream fos = new FileOutputStream(pngFile)) {
                int read;
                byte[] bytes = new byte[1024];
                InputStream content = response.getEntity().getContent();
                while ((read = content.read(bytes)) != -1) {
                    fos.write(bytes, 0, read);
                }
                LOG.info("Resultat wurde unter {} abgelegt.", pngFile.getAbsolutePath());
            } catch (IOException ex) {
                LOG.error("Datei konnte nicht geschrieben werden!", ex);
                throw new MapsException("Datei konnte nicht geschrieben werden.", ex);
            }
        } catch (IOException | IllegalStateException ise) {
            LOG.error("Fehler beim Abrufen der Anfahrt.", ise);
            throw new MapsException("Fehler beim Abrufen der Anfahrt", ise);
        }
        return pngFile;
    }

    protected DirectionResponse getDirectionFromGoogle(String from, String to) throws MapsException {
        URI uri;
        try {
            uri = new URIBuilder()
                    .setScheme("http")
                    .setHost("maps.googleapis.com")
                    .setPath("/maps/api/directions/json")
                    .setParameter("language", "de")
                    .setParameter("region", "de")
                    .setParameter("sensor", "false")
                    .setParameter("mode", "driving")
                    .setParameter("origin", from)
                    .setParameter("destination", to)
                    .build();
        } catch (URISyntaxException ex) {
            LOG.error("Fehler beim Erzeugen der URI.", ex);
            throw new MapsException("Fehler beim Erzeugen der URI.", ex);
        }

        LOG.info("Routenanfrage: {}", uri.toString());
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet get = new HttpGet(uri);
        DirectionResponse directionResponse = null;
        try (CloseableHttpResponse response = httpclient.execute(get)) {

            HttpEntity httpEntity = response.getEntity();
            directionResponse = gson.fromJson(new InputStreamReader(httpEntity.getContent(), "UTF-8"), DirectionResponse.class);

        } catch (IOException ioe) {
            throw new MapsException("Fehler beim Verarbeiten des Response", ioe);
        }

        return directionResponse;
    }

    protected String createPath(Route route) {
        StringBuilder builder = new StringBuilder("weight:3");
        builder.append("|")
                .append("color:red")
                .append("|")
                .append("enc:")
                .append(route.getOverview_polyline().getPoints());

        return builder.toString();
    }

    protected String createMarker(Route route) {
        StringBuilder builder = new StringBuilder();
        Leg targetLeg = route.getLegs().get(route.getLegs().size() - 1);
        builder.append("color:red")
                .append("|")
                .append("label:E")
                .append("|")
                .append(targetLeg.getEnd_location().getLat())
                .append(",")
                .append(targetLeg.getEnd_location().getLng());

        return builder.toString();
    }

    /**
     * @return the start
     */
    public String getStart() {
        return start;
    }

}
