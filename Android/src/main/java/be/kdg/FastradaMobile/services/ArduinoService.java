package be.kdg.FastradaMobile.services;

import android.app.IntentService;
import android.content.Intent;
import be.kdg.FastradaMobile.controllers.*;

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
        ConfigController config = new ConfigController(getApplicationContext());
        UserInterfaceController userInterface = UserInterfaceController.getInstance();
        BufferController buffer = BufferController.getInstance();
        ByteCalculateController byteCalculateController = new ByteCalculateController(config);

        while (true) {
            // Receive packet
            byte[] received = inputDataController.receiveUdpPacket();

            // Add packet to buffer
            if (CommunicationService.running) {
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

