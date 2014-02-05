package be.kdg.FastradaMobile;

import be.kdg.FastradaMobile.controllers.InputDataController;
import org.junit.Test;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Random;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * Created by Jonathan on 5/02/14.
 */
public class TestInputDataController {

    private void sendUdpPackets(final byte[] packet, final int port) {
        Thread thread = new Thread() {
            public void run() {
                byte[] arrayStream = packet;
                try {
                    DatagramSocket datagramSocket = new DatagramSocket();
                    while (true) {
                        InetAddress address = InetAddress.getByName("127.0.0.1");
                        DatagramPacket packet = new DatagramPacket(arrayStream, arrayStream.length, address, port);
                        datagramSocket.send(packet);
                        Thread.sleep(500);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }

    @Test
    public void testReceiveFixedUdpPacket() throws IOException {
        byte [] packet = {(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF};
        sendUdpPackets(packet, 9000);

        InputDataController inputDataController = new InputDataController();
        byte[] result = inputDataController.receiveUdpPacket(9000);

        //byte test = (byte) Integer.parseInt("FF",16);

        assertArrayEquals("Send and reveive bytes must be the same", packet, result);
    }


    @Test
    public void testReceiveVariableUdpPacket() throws IOException {
        Random random = new Random();
        byte[] bytes = new byte[10];
        random.nextBytes(bytes);
        sendUdpPackets(bytes, 9001);

        InputDataController inputDataController = new InputDataController();
        byte[] result = inputDataController.receiveUdpPacket(9001);

        assertArrayEquals("Variable receive udp bytes must be the same", bytes, result);

    }


    @Test
    public void testEngineMsgParser() {

    }

    @Test
    public void testCarMsgParser() {
    }

    @Test
    public void testAppMsgParser() {
    }
}