package app;

/**
 * Created by Jonathan on 11/03/14.
 */
public class GpsValue {
    private long date;
    private Coordinate coordinate;

    public GpsValue(long date, Coordinate coordinate) {
        this.date = date;
        this.coordinate = coordinate;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
    }
}
