package be.kdg.FastradaMobile.json;

import java.io.Serializable;
import java.util.Date;

public class SessionData implements Serializable {
    private String sessionName;
    private String trackName;
    private String vehicleName;
    private String comment;
    private Date date;

    public SessionData() {
    }

    public SessionData(String sessionName, String trackName, String vehicleName, String comment, Date date) {
        this.sessionName = sessionName;
        this.trackName = trackName;
        this.vehicleName = vehicleName;
        this.comment = comment;
        this.date = date;
    }

    public String getSessionName() {
        return sessionName;
    }

    public void setSessionName(String sessionName) {
        this.sessionName = sessionName;
    }

    public String getTrackName() {
        return trackName;
    }

    public void setTrackName(String trackName) {
        this.trackName = trackName;
    }

    public String getVehicleName() {
        return vehicleName;
    }

    public void setVehicleName(String vehicleName) {
        this.vehicleName = vehicleName;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
