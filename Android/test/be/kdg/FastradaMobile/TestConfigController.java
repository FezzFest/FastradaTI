package be.kdg.FastradaMobile;

import be.kdg.FastradaMobile.config.Parameter;
import be.kdg.FastradaMobile.config.Sensor;
import be.kdg.FastradaMobile.controllers.ConfigController;
import org.junit.Test;

import static junit.framework.Assert.assertTrue;

/**
 * Created by philip on 5/02/14.
 */
public class TestConfigController {
@Test
   public void testGetSensor100Config() throws Exception {
    ConfigController controller = new ConfigController("res/raw/testconfig.xml");

    Sensor sensor = controller.getSensor(100);

    Sensor tSensor = new Sensor();
    tSensor.addParamater(new Parameter("Throttle_position",24, 16, "Intel", "unsigned", 0.0015259, 0, 0, 99.9999, "%"));
    tSensor.addParamater(new Parameter("Engine_Temp",40, 16, "Intel", "signed", 0.0030518, 50, -50.0014, 149.998, "°C"));
    tSensor.addParamater(new Parameter("RPM",8, 16, "Intel", "unsigned", 1, 0, 0, 65535, "r/min"));

    assertTrue(tSensor.equals(sensor));
}
}
