package be.kdg.FastradaMobile.services;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;
import be.kdg.FastradaMobile.controllers.BufferController;
import be.kdg.FastradaMobile.controllers.InputDataController;

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

        while (true) {
            byte[] received = controller.receiveUdpPacket();

            StringBuilder sb = new StringBuilder();
            for (byte b : received) {
                sb.append(String.format("%02x ", b & 0xFF));
            }
            Log.d("Fastrada", "Arduino: " + sb.toString());

            int value = received[0];
            buffer.setSpeed(value);
        }
    }
}
