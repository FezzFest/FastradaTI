package be.kdg.FastradaMobile.controllers;

import java.util.HashMap;

/**
 * Created by FezzFest on 6/02/14.
 */
public class BufferController {
    private static BufferController instance;

    private int speed;
    private int rpm;
    private int pressure;
    private double temperature;
    private int gear;

    private BufferController() {
    }


    public static BufferController getInstance() {
        if (instance == null) createInstance();
        return instance;
    }

    private synchronized static void createInstance() {
        if (instance == null) instance = new BufferController();
    }

    public void setValue(String paramName, double value) {
        if (paramName.equals("Vehicle_Speed")) {
            speed = (int) (value + 0.5);
        } else if (paramName.equals("Gear")) {
            gear = (int) (value + 0.5);
        } else if (paramName.equals("RPM")) {
            rpm = (int) (value + 0.5);
        } else if (paramName.equals("Engine_Temp")) {
            temperature = value;
        }
    }

    public int getSpeed() {
        return speed;
    }

    public int getRpm() {
        return rpm;
    }

    public double getTemperature() {
        return temperature;
    }

    public int getGear() {
        return gear;
    }

}
