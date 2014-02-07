import org.junit.Test;
import java.io.IOException;
import static org.junit.Assert.assertArrayEquals;

public class MockTest {

    @Test
    public void testZeros(){
        byte[] stream;
        stream = Run.generateData(0, 0);
        assertArrayEquals(new byte[]{
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00
        }, stream);

    }

    @Test
    public void testValue(){
        byte[] stream;
        stream = Run.generateData(0, 256);
        assertArrayEquals(new byte[]{
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x01, (byte) 0x00
        }, stream);
    }

    @Test
    public void readLine() throws IOException {
        Run.initFile();
        byte[] stream;
        stream = Run.getLine();
        assertArrayEquals(new byte[]{
                (byte) 0x01, (byte) 0x00, (byte) 0xff, (byte) 0xff, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00
        }, stream);
    }

    @Test
    public void sendZerosFor20Secs() throws IOException {
        byte[] stream;
        stream = Run.generateData(0, 0);
        for(int i=0; i<20; i++){
            Run.sendPacket(stream);
        }
    }

}
