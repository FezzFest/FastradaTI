package be.kdg.FastradaMobile.services;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;
import be.kdg.FastradaMobile.controllers.BufferController;
import be.kdg.FastradaMobile.controllers.CompressorController;

/**
 * Created by FezzFest on 18/02/14.
 */
public class CommunicationService extends IntentService {
    public CommunicationService() {
        super("CommunicationService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        BufferController buffer = BufferController.getInstance();
        CompressorController compressor = new CompressorController();

        while (true) {
            // Get packets from buffer
            byte[] packets = buffer.getPackets();

            // Compress packets
            byte[] compressed = compressor.compress(packets);
            Log.d("Fastrada", "Got " + packets.length + " packets from buffer.");

            // 1 second delay
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
