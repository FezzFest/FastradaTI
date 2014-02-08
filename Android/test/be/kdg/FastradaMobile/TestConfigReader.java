package be.kdg.FastradaMobile;

import be.kdg.FastradaMobile.config.ConfigReader;
import be.kdg.FastradaMobile.config.Parameter;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by philip on 5/02/14.
 */
public class TestConfigReader {

    private static ConfigReader reader;

    @BeforeClass
    public static void initializeTestConfigReader(){
        reader = new ConfigReader("res/raw/testconfig.xml");
    }


    @Test
    public void testReadRPMStartbit() {
        int startbit = reader.getConfigIntValue("RPM", "startbit");

        assertEquals(8, startbit);
    }

    @Test
    public void testReadThrottleStartbit(){
        int startbit = reader.getConfigIntValue("Throttle_position", "startbit");

        assertEquals(24, startbit);
    }

    @Test
    public void testReadVehicle_SpeedOffset(){
       int offset = reader.getConfigIntValue("Vehicle_Speed", "offset");

        assertEquals(0,offset);
    }

    @Test
    public void testReadEngine_TempOffset(){
        int offset = reader.getConfigIntValue("Engine_Temp", "offset");

        assertEquals(50,offset);
    }

    @Test
         public void testReadVehicle_SpeedFactor(){
        double offset = reader.getConfigDoubleValue("Vehicle_Speed","factor");

        assertEquals(0.00549324,offset,0);
    }

    @Test
    public void testReadGearUnit(){
        String unit = reader.getConfigStringValue("Gear","unit");

        assertEquals("",unit);
    }

    @Test
    public void testReadEngine_TempUnit(){
        String unit = reader.getConfigStringValue("Engine_Temp","unit");

        assertEquals("°C",unit);
    }

    @Test
    public void testReadEngine_TempMinimum(){
        Double minimum = reader.getConfigDoubleValue("Engine_Temp", "minimum");

        assertEquals(-50.0014,minimum,0);
    }

    @Test
    public void testGetRpmAsParameter(){
        Parameter Rpm = reader.getParameterConfig("RPM");
        Parameter p1 = new Parameter(8,16,"Intel","unsigned",1,0,0,65535,"r/min");

        assertEquals(Rpm,p1);
    }

    @Test
    public void testGetFuelMapAsParameter(){
        Parameter Fuelmap = reader.getParameterConfig("Fuel_map");
        Parameter p1 = new Parameter(8,8,"Intel","unsigned",1,0,0,255,"");

        assertEquals(Fuelmap,p1);
    }

    @Test
    public void testReadSensor100ParameterNames() {
        List<String> parameterNames = reader.getParameterNames(100);

        List<String> controleParameterNames = new ArrayList<String>();
        controleParameterNames.add("RPM");
        controleParameterNames.add("Throttle_position");
        controleParameterNames.add("Engine_Temp");

        assertEquals(controleParameterNames, parameterNames);
    }


}
