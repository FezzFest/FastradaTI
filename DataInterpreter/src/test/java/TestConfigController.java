import dataInterpreter.ConfigController;
import dataInterpreter.Parameter;
import dataInterpreter.Sensor;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Created by Carlo on 28/02/14.
 */
public class TestConfigController {
    @Test
    public void testGetSensor100Config() throws Exception {
        ConfigController controller = new ConfigController("src/main/resources/testconfig.xml");

        Sensor sensor = controller.getSensor(256);

        Sensor tSensor = new Sensor();
        tSensor.addParamater(new Parameter("Throttle_position", 32, 16, "Intel", 0.0015259, 0, 0, 99.9999, "%"));
        tSensor.addParamater(new Parameter("Engine_Temp", 48, 16, "Intel", 0.0030518, 50, -50.0014, 149.998, "°C"));
        tSensor.addParamater(new Parameter("RPM", 16, 16, "Intel", 1, 0, 0, 65535, "r/min"));

        assertTrue(tSensor.equals(sensor));
    }
}
