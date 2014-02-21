import org.junit.Test;
import java.io.IOException;
import static org.junit.Assert.assertArrayEquals;

public class MockTest {

    private byte[] packet;

    @Test
    public void testZeros(){
        //Generate a packet with all zeros
        packet = Run.generateData(0, 0);
        //Now check its content
        assertArrayEquals(new byte[]{
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00
        }, packet);

    }

    @Test
    public void testValue(){
        //Generate a packet with a specific value
        packet = Run.generateData(0, 256);
        //Now check its content
        assertArrayEquals(new byte[]{
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x01, (byte) 0x00
        }, packet);
    }

    @Test
    public void readLine() throws IOException {
        //Load the data file
        Run.initFile();
        //Retrieve the first line
        packet = Run.getLine();
        //Now check its content
        assertArrayEquals(new byte[]{
                (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xFF, (byte) 0x00, (byte) 0x00
        }, packet);
    }

    @Test
    public void sendZeros() throws IOException {
        //Generate a zeros packet
        packet = Run.generateData(0, 0);
        //Send it 20 times, the received packets are tested on the Android device
        for(int i=0; i<20; i++){
            Run.sendPacket(packet);
        }
    }
}
