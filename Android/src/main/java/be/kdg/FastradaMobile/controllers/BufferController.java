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
    private int temperature;
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

    public void setValue(String paramName, int value) {
        if (paramName.equals("Vehicle_Speed")) {
            speed = value;
        } else if (paramName.equals("Gear")){
            gear = value;
        }
    }

    public int getSpeed() {
        return speed;
    }
}
