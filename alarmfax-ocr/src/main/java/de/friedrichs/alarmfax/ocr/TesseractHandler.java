package de.friedrichs.alarmfax.ocr;

import de.friedrichs.alarmfax.ocr.exception.OCRException;
import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Alexander Friedrichs <friedrichs.alexander@gmail.com>
 */
public class TesseractHandler {

    private static final Logger LOG = LoggerFactory.getLogger(TesseractHandler.class);
    private static final String CMD = "tesseract %1 %2 -l deu";
    private static final String DEFAULT_STORAGEPATH = FileUtils.getTempDirectoryPath();
    private String storagePath = DEFAULT_STORAGEPATH;

    public OCRResponse parseImage(File sourceFile, OCRResponse response) throws OCRException {
        response.setOrgInput(sourceFile);
        String targetFileName = sourceFile.getName().replace(".tif", ".txt");

        File targetFolder = new File(getStoragePath());
        File targetFile = new File(getStoragePath(), targetFileName);
        if (!targetFolder.exists()) {
            try {
                FileUtils.forceMkdir(targetFolder);
            } catch (IOException ie) {
                throw new OCRException("Ordnerstruktur konnte nicht angelegt werden", ie);
            }
        }

        String cmd = CMD.replace("%1", sourceFile.getAbsolutePath()).replace("%2", targetFile.getAbsolutePath().replace(".txt", ""));
        LOG.info("CMD: {}", cmd);

        Runtime rt = Runtime.getRuntime();
        Process exec;
        int waitFor;
        try {
            exec = rt.exec(cmd);
            waitFor = exec.waitFor();
            LOG.info("Process terminierte mit Wert {}", waitFor);
            response.setOutput(targetFile);
        } catch (IOException | InterruptedException ie) {
            throw new OCRException("Commando '" + cmd + "' wurde nicht korrekt beendet.", ie);
        }

        return response;
    }

    public OCRResponse optimizeOutput(OCRResponse response) throws OCRException {
        try {
            for (String line : FileUtils.readLines(response.getOutput(), "UTF-8")) {
                response.getRows().add(normalizeAddress(line));
            }
        } catch (IOException ie) {
            throw new OCRException("Datei konnte nicht eingelesen werden.", ie);
        }
        return response;
    }

    protected String normalizeAddress(String str) {
        return str.replaceAll("(?<=\\w)[B]{1}(?=\\w)", "ß")
                .replaceAll("(?<=\\w)[1]{1}(?=\\w)", "l")
                .replaceAll("(?<=\\w)[—]{1}(?=\\w)", "-")
                .replaceAll("[o]{1}[n]{1}[e]{1}[r]{1}[-]{1}(?=\\w)", "Ober-")
                .replaceAll("(?<=\\w)[o]{1}[s]{1}[n]{1}[a]{1}(?=\\w)", "osba")
                .replaceAll("(?<=\\w)[a]{1}[e]{1}[h]{1}", "ach")
                .replaceAll("[K]{1}[o]{1}[h]{1}[r]{1}(?=\\w)", "Konr")
                .replaceAll("(?<=\\w)[d]{1}[e]{1}[h]{1}[a]{1}(?=\\w)", "dena")
                .replaceAll("(?<=\\w)[-]{1}[s]{1}[t]{1}[r]{1}[a]{1}[a]{1}(?=\\w)", "-Straß");
    }

    /**
     * @return the storagePath
     */
    public String getStoragePath() {
        return storagePath;
    }

    /**
     * @param storagePath the storagePath to set
     */
    public void setStoragePath(String storagePath) {
        this.storagePath = storagePath;
    }

}
