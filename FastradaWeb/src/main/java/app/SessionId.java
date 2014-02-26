package app;

import java.io.Serializable;

/**
 * Created by Jonathan on 26/02/14.
 */
public class SessionId implements Serializable {
    private int sessionId;

    public SessionId(int sessionId) {
        this.sessionId = sessionId;
    }

    public SessionId() {
    }

    public int getSessionId() {
        return sessionId;
    }

    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }
}
