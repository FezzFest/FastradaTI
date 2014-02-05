package be.kdg.FastradaMobile.controllers;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by FezzFest on 4/02/14.
 */
public class InputDataController {
    private long speed;

    public long getSpeed() {
        return 20;
    }

    public byte[] receiveUdpPacket(String strAddress, int port) {
        byte[] result = new byte[100];
        try {
            InetAddress address = InetAddress.getByName(strAddress);
            DatagramPacket packet = new DatagramPacket(result, result.length, address, port);
            DatagramSocket datagramSocket = new DatagramSocket();
            datagramSocket.setSoTimeout(5000);
            datagramSocket.receive(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
