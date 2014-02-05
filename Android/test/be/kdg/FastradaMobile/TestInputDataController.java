package be.kdg.FastradaMobile;

import be.kdg.FastradaMobile.controllers.InputDataController;
import org.junit.Test;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * Created by Jonathan on 5/02/14.
 */
public class TestInputDataController {
    @Test
    public void testReceiveUdpPacket() throws IOException {
       /* InputDataController inputDataController = new InputDataController();
        byte[] arrayStream = "test".getBytes();
        InetAddress address = InetAddress.getByName("127.0.0.1");
        DatagramPacket packet = new DatagramPacket(arrayStream, arrayStream.length, address, 9000);
        DatagramSocket datagramSocket = new DatagramSocket();
        datagramSocket.send(packet);
        byte[] result = inputDataController.receiveUdpPacket("127.0.0.1", "9000");

        assertArrayEquals("Send and reveive bytes must be the same", result, "test".getBytes());*/

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