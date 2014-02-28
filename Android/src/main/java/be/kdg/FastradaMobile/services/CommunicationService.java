package be.kdg.FastradaMobile.services;

import android.app.IntentService;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;
import be.kdg.FastradaMobile.controllers.BufferController;
import dataInterpreter.CompressorController;

/**
 * Created by Peter Van Akelyen on 18/02/14.
 */
public class CommunicationService extends IntentService {
    public static boolean isRunning = false;
    private Handler mHandler;

    public CommunicationService() {
        super("CommunicationService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mHandler = new Handler();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // Change service status
        isRunning = true;
        Log.i("Fastrada", "Communication Service started.");

        // Get session ID
        final int sessionId = intent.getIntExtra("sessionId", -1);
        Log.i("Fastrada", "Session with ID #" + sessionId + " started.");

        // Show toast
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(CommunicationService.this, "Session with ID #" + sessionId + " started.", Toast.LENGTH_SHORT).show();
            }
        });

        // New controllers
        BufferController buffer = BufferController.getInstance();
        CompressorController compressor = new CompressorController();

        while (true) {
            // Get packets from buffer
            byte[] packets = buffer.getPackets();

            // Compress packets
            byte[] compressed = compressor.compress(packets);

            // Log
            Log.d("Fastrada", "Compression ratio: " + packets.length/compressed.length);

            // 1 second delay
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
