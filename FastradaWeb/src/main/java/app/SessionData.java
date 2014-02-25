package app;

public class SessionData {
    public int sessionId;
    public String name;
    public int startTime;
    public int date;

    public SessionData(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
