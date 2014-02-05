package be.kdg.FastradaMobile.controllers;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by FezzFest on 4/02/14.
 */
public class InputDataController {
    Context context;

    public InputDataController(Context context) {
        this.context = context;
    }

    public byte[] receiveUdpPacket() {
        // Get port from SharedPreferences
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        int port = prefs.getInt("port", 9000);

        // Receive packet via UDP
        byte[] result = new byte[10];
        try {
            DatagramPacket packet = new DatagramPacket(result, result.length);
            DatagramSocket datagramSocket = new DatagramSocket(port);
            datagramSocket.receive(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
