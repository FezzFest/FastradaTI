package be.kdg.FastradaMobile.services;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;
import be.kdg.FastradaMobile.config.Parameter;
import be.kdg.FastradaMobile.config.Sensor;
import be.kdg.FastradaMobile.controllers.BufferController;
import be.kdg.FastradaMobile.controllers.ConfigController;
import be.kdg.FastradaMobile.controllers.InputDataController;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by FezzFest on 5/02/14.
 */
public class ArduinoService extends IntentService {
    public ArduinoService() {
        super("ArduinoService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        InputDataController controller = new InputDataController(getApplicationContext());
        BufferController buffer = BufferController.getInstance();
        ConfigController config = new ConfigController(getApplicationContext());

        while (true) {
            byte[] received = controller.receiveUdpPacket();

          /*  StringBuilder sb = new StringBuilder();
            for (byte b : received) {
                sb.append(String.format("%02x ", b & 0xFF));
            }
            Log.d("Fastrada", "Arduino: " + sb.toString());*/

            //Get the sensor Id
            //TODO inlezen van id is nu als integer. Wordt wel als hex beschreven in config file
            int id = Integer.parseInt(String.format("%02X%02X", received[0], received[1]), 16) & 0xffffff;
            //TODO inlezen van valuetype(signed of unsigned) is nu als string. Misschien inlezen als boolean
            Sensor sensor = config.getSensorConfig(id);
            //TODO optioneel-performance: hashmap omzetten naar arraylist
            Iterator it = sensor.getParameters().entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                Parameter parameter = (Parameter) pair.getValue();
                int rawValue = 0;
                if (parameter.getStartBit() / 8 == 2 && parameter.getLength() / 8 == 2) {
                    rawValue = Integer.parseInt(String.format("%02X%02X", received[parameter.getStartBit() / 8], received[parameter.getLength()/8 + parameter.getStartBit()/8 -1]), 16) & 0xffffff;
                    Log.d("Fastrada", "startpos:" + parameter.getStartBit()/8);
                    Log.d("Fastrada", "stoppos:" + (parameter.getStartBit()/8 + parameter.getLength()/8));
                    Log.d("Fastrada", "Aangekregen Byte: "+ rawValue);
                    int value = (int) (rawValue * parameter.getFactor() + 0.5);
                    Log.d("Fastrada", "Omgerekend:  "+ value);
                    String name = pair.getKey().toString();
                    buffer.setValue(name, value);
                }
            }
        }
    }
}
