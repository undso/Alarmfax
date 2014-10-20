package de.friedrichs.alarmfax.monitor.model;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Alexander Friedrichs
 */
@XmlRootElement
public class Status implements Serializable {

    private static final long serialVersionUID = 1569261695804742350L;
    
    private String pid;
    
    public Status() {
    }

    /**
     * @return the pid
     */
    public String getPid() {
        return pid;
    }

    /**
     * @param pid the pid to set
     */
    public void setPid(String pid) {
        this.pid = pid;
    }
    
    
}
