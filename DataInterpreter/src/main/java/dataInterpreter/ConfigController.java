package dataInterpreter;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Carlo on 28/02/14.
 */
public class ConfigController {
    private ConfigReader configReader;
    private HashMap<Integer, Sensor> sensors;

    public static void main(String[] args) {

    }

    public ConfigController(String testConfigPath) {
        try {
            configReader = new ConfigReader(testConfigPath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        readSensorConfigs();
    }

    public ConfigController(InputStream configFileStream) {
        // TODO Android app should call this method with the InputStream;
        //configFileStream = null;
        //configFileStream = context.getResources().openRawResource(R.raw.config);
        configReader = new ConfigReader(configFileStream);
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
