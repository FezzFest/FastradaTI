package app;

import java.util.Date;

public class Parameter {
    private Date timestamp;
    private double value;

    public Parameter(Date timestamp, double value) {
        this.timestamp = timestamp;
        this.value = value;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Parameter parameter = (Parameter) o;

        if (Double.compare(parameter.value, value) != 0) return false;
        if (!timestamp.equals(parameter.timestamp)) return false;

        return true;
    }
}
