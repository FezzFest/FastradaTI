package app;

import java.io.Serializable;
import java.util.Date;

public class SessionData implements Serializable {
    private String name;
    private Date date;
    private String comment;
    private String track;

    public SessionData(String name, Date date, String comment, String track) {
        this.name = name;
        this.date = date;
        this.comment = comment;
        this.track = track;
    }

    public String getName() {
        return name;
    }

    public Date getDate() {
        return date;
    }

    public String getComment() {
        return comment;
    }

    public String getTrack() {
        return track;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setTrack(String track) {
        this.track = track;
    }
}
