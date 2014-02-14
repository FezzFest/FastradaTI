package be.kdg.FastradaMobile.controllers;

import android.util.Log;
import be.kdg.FastradaMobile.config.Parameter;
import be.kdg.FastradaMobile.config.Sensor;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Jonathan on 11/02/14.
 */
public class ByteCalculateController {
    ConfigController configController;

    public ByteCalculateController(ConfigController configController) {
        this.configController = configController;
    }

    public Map<String, Double> calculatePacket(byte[] received) {
        Map<String, Double> map = new HashMap();
        //Get the sensor Id
        //TODO inlezen van id is nu als integer. Wordt wel als hex beschreven in config file
        int id = Integer.parseInt(String.format("%02X%02X", received[0], received[1]), 16) & 0xffffff;
        //TODO inlezen van valuetype(signed of unsigned) is nu als string. Misschien inlezen als boolean
        Sensor sensor = null;
        try {
            sensor = configController.getSensor(id);
        } catch (Exception e) {
            Log.e("Fastrada", e.getMessage());
        }
        Iterator it = sensor.getParameters().iterator();
        while (it.hasNext()) {
            Parameter parameter = (Parameter) it.next();
            int rawValue = 0;
            int startByte = parameter.getStartBit() / 8;
            int stopByte = parameter.getStartBit() / 8 + parameter.getLength() / 8 - 1;
            switch (parameter.getLength() / 8) {
                case 1:
                    rawValue = Integer.parseInt(String.format("%02X", received[startByte]), 16) & 0xffffff;
                    break;
                case 2:
                    rawValue = Integer.parseInt(String.format("%02X%02X", received[startByte], received[stopByte]), 16) & 0xffffff;
                    break;
                case 3:
                    rawValue = Integer.parseInt(String.format("%02X%02X%02X", received[startByte], received[stopByte - 1], received[stopByte]), 16) & 0xffffff;
                    break;
                case 4:
                    rawValue = Integer.parseInt(String.format("%02X%02X%02X%02X", received[startByte], received[stopByte - 2], received[stopByte - 3], received[stopByte]), 16) & 0xffffff;
                    break;
            }
            double value = (rawValue * parameter.getFactor())-parameter.getOffset();
            String name = parameter.getName();
            map.put(name,value);
        }
        return map;
    }
}
