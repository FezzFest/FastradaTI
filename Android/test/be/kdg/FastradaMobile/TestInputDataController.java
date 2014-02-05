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

    private void sendUdpPackets(final byte[] packet) {
        Thread thread = new Thread() {
            public void run() {
                byte[] arrayStream = packet;
                try {
                    DatagramSocket datagramSocket = new DatagramSocket();
                    while (true) {
                        InetAddress address = InetAddress.getByName("127.0.0.1");
                        DatagramPacket packet = new DatagramPacket(arrayStream, arrayStream.length, address, 9000);
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
  /*      sendUdpPackets("test".getBytes());

        InputDataController inputDataController = new InputDataController();
        byte[] result = inputDataController.receiveUdpPacket("127.0.0.1", 9000);

        assertArrayEquals("Send and reveive bytes must be the same", "test".getBytes(), result);
*/
    }


    @Test
    public void testReceiveVariableUdpPacket() throws IOException {
     /*   Random random = new Random();
        byte[] bytes = new byte[30];
       random.nextBytes(bytes);
        sendUdpPackets(bytes);

        InputDataController inputDataController = new InputDataController();
        byte[] result = inputDataController.receiveUdpPacket("127.0.0.1", 9000);

        assertArrayEquals("Variable receive udp bytes must be the same", bytes, result);*/

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