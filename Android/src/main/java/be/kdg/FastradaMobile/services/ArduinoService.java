package be.kdg.FastradaMobile.services;

import android.app.IntentService;
import android.content.Intent;
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

            int value = received[0];
            buffer.setSpeed(value);
        }
    }
}
