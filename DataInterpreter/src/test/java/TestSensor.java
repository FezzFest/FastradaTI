import dataInterpreter.Parameter;
import dataInterpreter.Sensor;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Created by Carlo on 28/02/14.
 */
public class TestSensor {
    @Test
    public void testAddParameterTwice(){
        Sensor testSensor = new Sensor();
        Parameter testParameter = new Parameter("RPM",8,16,"unsigned",1,0,0,65535,"r/min");
        testSensor.addParamater(testParameter);
        testSensor.addParamater(testParameter);

        assertEquals(1,testSensor.getParameters().size());
    }

    @Test
    public void testAddParameterEmptyName(){
        Sensor testSensor = new Sensor();
        Parameter testParameter = new Parameter("",8,16,"unsigned",1,0,0,65535,"r/min");
        testSensor.addParamater(testParameter);

        assertEquals(0,testSensor.getParameters().size());
    }
}
