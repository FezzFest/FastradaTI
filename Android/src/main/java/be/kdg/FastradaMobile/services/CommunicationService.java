package be.kdg.FastradaMobile.services;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;
import be.kdg.FastradaMobile.Constants;
import be.kdg.FastradaMobile.controllers.BufferController;
import be.kdg.FastradaMobile.controllers.OutputDataController;
import dataInterpreter.CompressorController;

import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Peter Van Akelyen on 18/02/14.
 */
public class CommunicationService extends IntentService {
    public static boolean isRunning = false;
    private int sessionId;
    private Handler mHandler;
    private static Logger log = Logger.getLogger(CommunicationService.class.getClass().getName());

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
        Log.i(Constants.TAG, "Communication Service started.");

        // Get session ID
        sessionId = intent.getIntExtra("sessionId", -1);

        // Log
        String sessionStarted = "Session with ID #" + sessionId + " started";
        Log.i(Constants.TAG, sessionStarted);

        // Show toast
        showToast(sessionStarted);

        // New controllers
        BufferController buffer = BufferController.getInstance();
        OutputDataController output = new OutputDataController();

        // Start Location Service
        startService(new Intent(this,LocationService.class));

        while (true) {
            // Get packets from buffer and add session ID
            byte[] packets = addSessionId(buffer.getPackets());

            // Compress packets
            byte[] compressed = CompressorController.compress(packets);
            Log.d(Constants.TAG, "Compression ratio: " + packets.length / compressed.length);

            // Send request
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            StringBuilder url = new StringBuilder();
            String host = prefs.getString("pref_service_address", Constants.DEF_SERVER_ADDR);
            String port = prefs.getString("pref_service_port", Constants.DEF_SERVER_PORT);
            String path = "/api/sessions/post";
            url.append(host);
            url.append(":");
            url.append(port);
            url.append(path);
            String result = output.doPost(url.toString(), compressed);

            // Log result
            Log.d(Constants.TAG, "Server response: " + result);

            // 1 second delay
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                log.log(Level.WARNING, e.getMessage(), e);
            }
        }
    }

    @Override
    public void onDestroy() {
        isRunning = false;
        String sessionStopped = "Session with ID #" + sessionId + " stopped.";

        // Log
        Log.d(Constants.TAG, sessionStopped);

        // Show toast
        showToast(sessionStopped);
    }

    private void showToast(final String text) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(CommunicationService.this, text, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private byte[] addSessionId(byte[] packet) {
        byte[] bSessionId = ByteBuffer.allocate(4).putInt(sessionId).array();
        byte[] newPacket = new byte[packet.length + bSessionId.length];
        System.arraycopy(bSessionId, 0, newPacket, 0, bSessionId.length);
        System.arraycopy(packet, 0, newPacket, bSessionId.length, packet.length);

        return newPacket;
    }
}
