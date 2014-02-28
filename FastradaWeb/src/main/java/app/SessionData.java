package app;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Jonathan on 26/02/14.
 */

public class SessionData implements Serializable {
    private String sessionName;
    private String trackName;
    private String vehicleName;
    private String comment;
    private long date;
    private int sessionId;

    public SessionData() {
    }

    public SessionData(String sessionName, String trackName, String vehicleName, String comment, long date) {
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

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }

    public int getSessionId() {
        return sessionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SessionData that = (SessionData) o;

        if (date != that.date) return false;
        if (sessionId != that.sessionId) return false;
        if (!comment.equals(that.comment)) return false;
        if (!sessionName.equals(that.sessionName)) return false;
        if (!trackName.equals(that.trackName)) return false;
        if (!vehicleName.equals(that.vehicleName)) return false;

        return true;
    }

}
