package be.kdg.FastradaMobile.controllers;

import be.kdg.FastradaMobile.config.ConfigReader;
import be.kdg.FastradaMobile.config.Parameter;
import be.kdg.FastradaMobile.config.Sensor;

import android.content.Context;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;

/**
 * Created by philip on 6/02/14.
 */
public class ConfigController {
    private ConfigReader configReader;
    private HashMap<Integer, Sensor> sensors;

    public ConfigController(String testConfigPath) {
        try {
            configReader = new ConfigReader(testConfigPath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        readSensorConfigs();
    }

    public ConfigController(Context context) {
        configReader = new ConfigReader(context);
        readSensorConfigs();

    }

    private void readSensorConfigs() {
        sensors = new HashMap<Integer, Sensor>();

        int[] sensorIds = configReader.getSensorIds();
        for (int sensorId : sensorIds) {
            readSensorConfigToMap(sensorId);
        }
    }

    public Sensor getSensor(int sensorId) throws Exception {
        if (!sensors.containsKey(sensorId)) {
            throw new Exception("Id is not defined in config file.");
        }
        return sensors.get(sensorId);
    }

    private void readSensorConfigToMap(int sensorId) {
        Sensor newSensor = new Sensor();

        List<String> parameterNames = configReader.getParameterNames(sensorId);

        for (int i = 0; i < parameterNames.size(); i++) {
            String parameterName = parameterNames.get(i);
            Parameter newParameter = configReader.getParameterConfig(parameterName);
            newSensor.addParamater(newParameter);
        }
        sensors.put(sensorId, newSensor);
    }
}
