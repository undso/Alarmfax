package de.friedrichs.alarmfax.ocr;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Alexander Friedrichs <friedrichs.alexander@gmail.com>
 */
public class OCRResponse implements Serializable {

    private File orgInput;
    private File output;    
    private List<String> rows;

    public OCRResponse() {
    }

    /**
     * @return the orgInput
     */
    public File getOrgInput() {
        return orgInput;
    }

    /**
     * @param orgInput the orgInput to set
     */
    public void setOrgInput(File orgInput) {
        this.orgInput = orgInput;
    }

    /**
     * @return the output
     */
    public File getOutput() {
        return output;
    }

    /**
     * @param output the output to set
     */
    public void setOutput(File output) {
        this.output = output;
    }

    /**
     * @return the rows
     */
    public List<String> getRows() {
        if(rows == null){
            rows = new ArrayList<>();
        }
        return rows;
    }

    /**
     * @param rows the rows to set
     */
    public void setRows(List<String> rows) {
        this.rows = rows;
    }

}
