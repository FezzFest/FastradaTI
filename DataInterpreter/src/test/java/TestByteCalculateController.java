import dataInterpreter.ByteCalculateController;
import dataInterpreter.ConfigController;
import org.junit.Assert;
import org.junit.Test;

import java.util.Map;

/**
 * Created by Carlo on 28/02/14.
 */
public class TestByteCalculateController {

    @Test
    public void byteArrayCalculateTest() {
        // Construct packet
        byte[] packet = {(byte) 0x01, (byte) 0x01, (byte) 0xD1, (byte) 0xC6, (byte) 0x01, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF};
        ByteCalculateController byteCalculateController = new ByteCalculateController(new ConfigController("src/main/resources/config.xml"));
        Map<String, Double> result = byteCalculateController.calculatePacket(packet);
        double speed = Math.round(result.get("Vehicle_Speed"));
        Assert.assertEquals("Speed", 295, (int) speed);
    }
}
