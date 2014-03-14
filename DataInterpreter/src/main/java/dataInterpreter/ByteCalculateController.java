package dataInterpreter;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Carlo on 28/02/14.
 */
public class ByteCalculateController {
    ConfigController configController;

    public ByteCalculateController(ConfigController configController) {
        this.configController = configController;
    }

    public Map<String, Double> calculatePacket(byte[] received) {
        Map<String, Double> map = new HashMap();
        // Get the sensor Id
        byte[] bId = {0x0, 0x0, received[0], received[1]};
        int id = ByteBuffer.wrap(bId).getInt(0);
        try {
            Sensor sensor = configController.getSensor(id);
            Iterator it = sensor.getParameters().iterator();
            while (it.hasNext()) {
                Parameter parameter = (Parameter) it.next();
                int rawValue = 0;
                int startByte = parameter.getStartBit() / 8;
                //int stopByte = parameter.getStartBit() / 8 + parameter.getLength() / 8 - 1;
                switch (parameter.getLength() / 8) {
                    case 1:
                        byte[] arrayOfOne = {0x0, 0x0, 0x0, received[startByte]};
                        rawValue = ByteBuffer.wrap(arrayOfOne).getInt(0);
                        break;
                    case 2:
                        byte[] arrayOfTwo = {0x0, 0x0, received[startByte], received[startByte + 1]};
                        rawValue = ByteBuffer.wrap(arrayOfTwo).getInt(0);
                        break;
                    case 3:
                        byte[] arrayOfThree = {0x0, received[startByte], received[startByte + 1], received[startByte + 2]};
                        rawValue = ByteBuffer.wrap(arrayOfThree).getInt(0);
                        break;
                    case 4:
                        byte[] arrayOfFour = {received[startByte], received[startByte + 1], received[startByte + 2], received[startByte + 3]};
                        rawValue = ByteBuffer.wrap(arrayOfFour).getInt(0);
                        break;
                }
                double value = (rawValue * parameter.getFactor()) - parameter.getOffset();
                String name = parameter.getName();
                map.put(name, value);
            }
        } catch (Exception e) {
            System.out.println("An error occured during parsing: " + e.getMessage());
        }
        return map;
    }
}
