package de.friedrichs.alarmfax.handler;

import de.friedrichs.alarmfax.ocr.OCRResponse;
import de.friedrichs.alarmfax.ocr.TesseractHandler;
import de.friedrichs.alarmfax.ocr.exception.OCRException;
import java.io.File;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import org.apache.camel.CamelException;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Alexander Friedrichs <friedrichs.alexander@gmail.com>
 */
public class OCRProcessor implements Processor {

    private static final Logger LOG = LoggerFactory.getLogger(OCRProcessor.class);
    
    private static final String ORT = "ORT";
    private static final String ORTSTEIL = "ORTSTEIL";
    private static final String STRASSE = "STRASSE";
    private static final String OCR_ERROR = "OCR_ERROR";
    
    private final TesseractHandler handler;

    public OCRProcessor() {
        this.handler = new TesseractHandler();
    }

    public OCRProcessor(String storagePath) {
        this();
        this.handler.setStoragePath(storagePath);
    }
    
    @Override
    public void process(Exchange exchange) throws Exception {
        
        this.handler.setStoragePath(exchange.getIn().getHeader("storagePath", String.class));
        
        File tiffFile = exchange.getIn().getBody(File.class);
        OCRResponse response;
        try{
            response = handler.parseImage(tiffFile, new OCRResponse());
            response = handler.optimizeOutput(response);
        }catch(OCRException ocre){
            throw new CamelException(ocre);
        }
        
        exchange.getIn().setBody(response, OCRResponse.class);
        
        //Speichern der txt Datei als Attachment
        exchange.getIn().addAttachment(
                response.getOutput().getName(), 
                new DataHandler(new FileDataSource(response.getOutput())));
        
        String ort = "";
        String ortsteil = "";
        String strasse = "";

        for(String string : response.getRows()){
            if (string.toUpperCase().contains(ORTSTEIL) && ortsteil.isEmpty()) {
                ortsteil = string.toUpperCase();
                LOG.debug("Ortsteil: {}", ortsteil);
            } else if (string.toUpperCase().contains(ORT) && ort.isEmpty()) {
                ort = string.toUpperCase();
                LOG.debug("Ort: {}", ort);
            } else if (string.toUpperCase().contains(STRASSE) && strasse.isEmpty()) {
                strasse = string.toUpperCase();
                LOG.debug("Straße: {}", strasse);
            }
        }
        ort = ort.replace(ORT, "").replace(":", "").trim();
        ortsteil = ortsteil.replace(ORTSTEIL, "").replace(":", "").trim();
        strasse = strasse.replace(STRASSE, "").replace(":", "").trim();

        LOG.info("Lets go to Straße '{}' im Ortsteil '{}' in der Stadt '{}'", new Object[]{strasse, ortsteil, ort});
        exchange.getIn().setHeader(ORT, ort);
        exchange.getIn().setHeader(ORTSTEIL, ortsteil);
        exchange.getIn().setHeader(STRASSE, strasse);

        if (ort.isEmpty() && ortsteil.isEmpty() && strasse.isEmpty()) {
            exchange.getIn().setHeader(OCR_ERROR, true);
        }

    }

}
