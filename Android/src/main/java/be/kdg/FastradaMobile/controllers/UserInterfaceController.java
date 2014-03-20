package be.kdg.FastradaMobile.controllers;

/**
 * Created by Peter Van Akelyen on 6/02/14.
 */
public class UserInterfaceController {
    private static UserInterfaceController instance;

    private int speed;
    private int rpm;
    private double temperature;
    private int gear;

    private UserInterfaceController() {
    }

    public static UserInterfaceController getInstance() {
        if (instance == null) createInstance();
        return instance;
    }

    private synchronized static void createInstance() {
        if (instance == null) instance = new UserInterfaceController();
    }

    public void setValue(String paramName, double value) {
        if (paramName.equals("Speed")) {
            speed = (int) (value + 0.5);
        } else if (paramName.equals("Gear")) {
            gear = (int) (value + 0.5);
        } else if (paramName.equals("RPM")) {
            rpm = (int) (value + 0.5);
        } else if (paramName.equals("Engine Temperature")) {
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
