package be.kdg.FastradaMobile.services;

import android.app.IntentService;
import android.content.Intent;
import be.kdg.FastradaMobile.R;
import be.kdg.FastradaMobile.controllers.*;
import dataInterpreter.ByteCalculateController;
import dataInterpreter.ConfigController;

import java.util.Map;

/**
 * Created by Peter Van Akelyen on 5/02/14.
 */
public class ArduinoService extends IntentService {
    public ArduinoService() {
        super("ArduinoService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        InputDataController inputDataController = new InputDataController(getApplicationContext());
        ConfigController config = new ConfigController(getApplicationContext().getResources().openRawResource(R.raw.config));
        UserInterfaceController userInterface = UserInterfaceController.getInstance();
        BufferController buffer = BufferController.getInstance();
        ByteCalculateController byteCalculateController = new ByteCalculateController(config);

        while (true) {
            // Receive packet
            byte[] received = inputDataController.receiveUdpPacket();

            // Add packet to buffer
            if (CommunicationService.isRunning) {
                buffer.addPacket(received);
            }

            // Calculate values for packet
            Map<String, Double> map = byteCalculateController.calculatePacket(received);
            for (Map.Entry<String, Double> entry : map.entrySet()) {
                userInterface.setValue(entry.getKey(), entry.getValue());
            }
        }
    }
}

