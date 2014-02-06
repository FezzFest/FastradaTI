package be.kdg.FastradaMobile;

import be.kdg.FastradaMobile.config.Parameter;
import be.kdg.FastradaMobile.config.Sensor;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Created by philip on 6/02/14.
 */
public class TestSensor {
    @Test
    public void testAddParameterTwice(){
        Sensor testSensor = new Sensor();
        Parameter testParameter = new Parameter(8,16,"Intel","unsigned",1,0,0,65535,"r/min");
        testSensor.addParamater("RPM",testParameter);
        testSensor.addParamater("RPM",testParameter);

        assertEquals(1,testSensor.getParameters().size());
    }

    @Test
    public void testAddParameterEmptyName(){
        Sensor testSensor = new Sensor();
        Parameter testParameter = new Parameter(8,16,"Intel","unsigned",1,0,0,65535,"r/min");
        testSensor.addParamater("",testParameter);

        assertEquals(0,testSensor.getParameters().size());
    }
}
