package be.kdg.FastradaMobile.services;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;
import be.kdg.FastradaMobile.config.Parameter;
import be.kdg.FastradaMobile.config.Sensor;
import be.kdg.FastradaMobile.controllers.BufferController;
import be.kdg.FastradaMobile.controllers.ByteCalculateController;
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
        InputDataController inputDataController = new InputDataController(getApplicationContext());
        BufferController buffer = BufferController.getInstance();
        ConfigController config = new ConfigController(getApplicationContext());
        ByteCalculateController byteCalculateController = new ByteCalculateController(config);

        while (true) {
            byte[] received = inputDataController.receiveUdpPacket();
            Map<String, Double> map = byteCalculateController.calculatePacket(received);
            for (Map.Entry<String, Double> entry : map.entrySet()) {
                buffer.setValue(entry.getKey(), entry.getValue());
            }
        }
    }
}

