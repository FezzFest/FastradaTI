package be.kdg.FastradaMobile.controllers;

/**
 * Created by FezzFest on 6/02/14.
 */
public class BufferController {
    private static BufferController instance;

    private int speed;
    private int rpm;
    private int pressure;
    private int temperature;

    private BufferController() {}

    public static BufferController getInstance() {
        if (instance == null) createInstance();
        return instance;
    }

    private synchronized static void createInstance () {
        if (instance == null) instance = new BufferController();
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getRpm() {
        return rpm;
    }

    public void setRpm(int rpm) {
        this.rpm = rpm;
    }

    public int getPressure() {
        return pressure;
    }

    public void setPressure(int pressure) {
        this.pressure = pressure;
    }

    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }
}
