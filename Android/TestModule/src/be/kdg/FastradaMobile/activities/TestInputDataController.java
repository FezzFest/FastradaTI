package be.kdg.FastradaMobile.activities;

import android.app.Activity;
import android.content.Intent;
import android.test.ActivityUnitTestCase;
import be.kdg.FastradaMobile.controllers.InputDataController;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Random;

import static org.junit.Assert.assertArrayEquals;

/**
 * Created by FezzFest on 5/02/14.
 */
public class TestInputDataController extends ActivityUnitTestCase<MainActivity> {
    private Activity activity;
    private InputDataController controller;

    public TestInputDataController() {
        super(MainActivity.class);
    }

    @Before
    protected void setUp() throws Exception {
        super.setUp();
        Intent intent = new Intent(getInstrumentation().getTargetContext(), MainActivity.class);
        startActivity(intent, null, null);
        activity = getActivity();
        controller = new InputDataController(activity.getApplicationContext());
    }

    @Test
    public void testReceiveFixedUdpPacket() throws IOException {
        byte [] packet = {(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF};
        sendUdpPackets(packet, 9000);

        byte[] result = controller.receiveUdpPacket();

        assertArrayEquals("Send and reveive bytes must be the same", packet, result);
    }

    @Test
    public void testReceiveVariableUdpPacket() throws IOException {
        Random random = new Random();
        byte[] bytes = new byte[10];
        random.nextBytes(bytes);
        sendUdpPackets(bytes, 9000);

        byte[] result = controller.receiveUdpPacket();

        assertArrayEquals("Variable receive udp bytes must be the same", bytes, result);
    }

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
}
