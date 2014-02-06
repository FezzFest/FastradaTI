package be.kdg.FastradaMobile.controllers;

import be.kdg.FastradaMobile.config.ConfigReader;
import be.kdg.FastradaMobile.config.Parameter;
import be.kdg.FastradaMobile.config.Sensor;

import java.util.HashMap;
import java.util.List;

/**
 * Created by philip on 6/02/14.
 */
public class ConfigController {
    private ConfigReader configReader;

    private HashMap<Integer, Sensor> sensors;

    public ConfigController(String testConfigPath) {
       configReader = new ConfigReader(testConfigPath);

       sensors = new HashMap<Integer, Sensor>();
    }

    public ConfigController() {
        this("res/xml/config.xml");
    }

    public Sensor getSensorConfig(int sensorId) {
        if(!sensors.containsKey(sensorId)){
            readSensorConfigToMap(sensorId);
        }
        return sensors.get(sensorId);
    }

    private void readSensorConfigToMap(int sensorId) {
        Sensor newSensor = new Sensor();

        List<String> parameterNames = configReader.getParameterNames(sensorId);

        for(int parameterCounter = 0;parameterCounter < parameterNames.size();parameterCounter++){
            String parameterName = parameterNames.get(parameterCounter);
            Parameter newParameter = configReader.getParameterConfig(parameterName);

            newSensor.addParamater(parameterName,newParameter);
        }
        sensors.put(sensorId,newSensor);
    }
}
