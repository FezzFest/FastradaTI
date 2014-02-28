import dataInterpreter.Parameter;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

/**
 * Created by Carlo on 28/02/14.
 */
public class TestParameter {

    @Test
    public void testEqualsTrue(){
        Parameter p1 = new Parameter("RPM",8,16,"Intel",1,0,0,65535,"r/min");
        Parameter p2 = new Parameter("RPM",8,16,"Intel",1,0,0,65535,"r/min");

        assertEquals(p1,p2);
    }

    @Test
    public void testEqualsFalse(){
        Parameter p1 = new Parameter("RPM",8,16,"Intel",1,0,0,65535,"r/min");
        Parameter p2 = new Parameter("RPM",7,16,"Intel",1,0,0,65535,"r/min");

        assertNotSame(p1, p2);
    }
}
