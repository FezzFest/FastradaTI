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
   public void testGetSensor100Config(){
    ConfigController controller = new ConfigController("res/raw/testconfig.xml");

    Sensor sensor = controller.getSensorConfig(100);

    Sensor tSensor = new Sensor();
    tSensor.addParamater("Throttle_position", new Parameter(24, 16, "Intel", "unsigned", 0.0015259, 0, 0, 99.9999, "%"));
    tSensor.addParamater("Engine_Temp", new Parameter(40, 16, "Intel", "signed", 0.0030518, 50, -50.0014, 149.998, "°C"));
    tSensor.addParamater("RPM", new Parameter(8, 16, "Intel", "unsigned", 1, 0, 0, 65535, "r/min"));

    assertTrue(tSensor.equals(sensor));
}
}
