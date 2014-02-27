package be.kdg.FastradaMobile.controllers;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

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
        int port = prefs.getInt("port", 9000);

        // Create socket
        try {
            socket = new DatagramSocket(port);
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public byte[] receiveUdpPacket() {
        // Receive packet via UDP
        byte[] result = new byte[10];
        try {
            DatagramPacket packet = new DatagramPacket(result, result.length);
            socket.receive(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
