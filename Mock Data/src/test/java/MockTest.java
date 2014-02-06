import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * Created by Thomas on 04/02/14.
 */
public class MockTest {



    @Test
    public void testZeros(){
        byte[] stream= new byte[9];
        stream = Run.generateData(0, 0);
        assertArrayEquals(new byte[]{
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00
        }, stream);

    }

    @Test
    public void testValue(){
        byte[] stream= new byte[9];
        stream = Run.generateData(0, 256);
        assertArrayEquals(new byte[]{
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x01, (byte) 0x00
        }, stream);
    }

    @Test
    public void readLine() throws IOException {
        Run.initFile();
        byte[] stream= new byte[10];
        stream = Run.getLine();
        assertArrayEquals(new byte[]{
                (byte) 0x01, (byte) 0x00, (byte) 0xff, (byte) 0xff, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00
        }, stream);
    }






}
