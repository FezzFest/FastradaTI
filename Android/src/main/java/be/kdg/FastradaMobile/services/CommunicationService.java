package be.kdg.FastradaMobile.services;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;
import be.kdg.FastradaMobile.controllers.BufferController;
import be.kdg.FastradaMobile.controllers.OutputDataController;
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
        OutputDataController output = new OutputDataController();

        while (true) {
            // Get packets from buffer
            byte[] packets = buffer.getPackets();

            // Compress packets
            byte[] compressed = CompressorController.compress(packets);
            Log.d("Fastrada", "Compression ratio: " + packets.length/compressed.length);

            // Send request
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            StringBuilder url = new StringBuilder();
            String host = prefs.getString("pref_service_address", "http://vps42465.ovh.net");
            String port = prefs.getString("pref_service_port", "8080");
            String path = "/api/sessions/post";
            url.append(host);
            url.append(":");
            url.append(port);
            url.append(path);
            String result = output.doPost(url.toString(), compressed);

            // Log result
            Log.d("Fastrada", "Server response: " + result);

            // 1 second delay
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
