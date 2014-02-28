package be.kdg.FastradaMobile.controllers;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import java.io.IOException;
import java.net.*;

/**
 * Created by Peter Van Akelyen on 4/02/14.
 */
public class InputDataController {
    Context context;
    DatagramSocket socket;

    public InputDataController(Context context) {
        this.context = context;
        initializeSocket();
    }

    private void initializeSocket() {
        // Get port from SharedPreferences
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        int port = Integer.parseInt(prefs.getString("pref_arduino_port", "9000"));

        // Create socket
        try {
            socket = new DatagramSocket(port);
        } catch (SocketException e) {
            Log.e("Fastrada", "Could not open socket: " + e.getMessage());
        }
    }

    public byte[] receiveUdpPacket() {
        // Receive packet via UDP
        byte[] result = new byte[10];
        try {
            DatagramPacket packet = new DatagramPacket(result, result.length);
            socket.receive(packet);
        } catch (IOException e) {
            Log.e("Fastrada", "Could not receive packet: " + e.getMessage());
        }
        return result;
    }
}
