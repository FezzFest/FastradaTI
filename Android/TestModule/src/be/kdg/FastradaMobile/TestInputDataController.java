package be.kdg.FastradaMobile;

import android.app.Activity;
import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.util.Log;
import be.kdg.FastradaMobile.activities.MainActivity;
import be.kdg.FastradaMobile.controllers.InputDataController;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import static org.junit.Assert.assertArrayEquals;

/**
 * Created by FezzFest on 5/02/14.
 */
public class TestInputDataController extends ActivityUnitTestCase<MainActivity> {
    private Activity activity;
    private InputDataController controller;
    private Thread thread;

    public TestInputDataController() {
        super(MainActivity.class);
    }

    protected void setUp() throws Exception {
        super.setUp();
        Intent intent = new Intent(getInstrumentation().getTargetContext(), MainActivity.class);
        startActivity(intent, null, null);
        activity = getActivity();
        controller = new InputDataController(activity.getApplicationContext());
    }

    public void testFixedUdpPacket() throws IOException, InterruptedException {
        // Construct packet
        byte[] packet = {(byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF};
        // Start sending packets
        sendUdpPackets(packet, 9000);
        // Sleep for 3 seconds
        Thread.sleep(3000);
        // Receive packet
        byte[] result = controller.receiveUdpPacket();
        // Stop sending packets
        thread.interrupt();

        assertArrayEquals("Sent and received fixed packet must be the same.", packet, result);
    }

    /*public void testVariableUdpPacket() throws IOException, InterruptedException {
        // Construct packet
        Random random = new Random();
        byte[] bytes = new byte[10];
        random.nextBytes(bytes);
        // Start sending packets
        sendUdpPackets(bytes, 9000);
        // Sleep for 3 seconds
        Thread.sleep(3000);
        // Receive packet
        byte[] result = controller.receiveUdpPacket();
        // Stop sending packets
        thread.interrupt();

        assertArrayEquals("Sent and received variable packet must be the same.", bytes, result);
    }*/

    private void sendUdpPackets(final byte[] packet, final int port) {
        thread = new Thread() {
            public void run() {
                byte[] arrayStream = packet;
                try {
                    DatagramSocket datagramSocket = new DatagramSocket();
                    while (true) {
                        InetAddress address = InetAddress.getByName("127.0.0.1");
                        DatagramPacket packet = new DatagramPacket(arrayStream, arrayStream.length, address, port);
                        datagramSocket.send(packet);
                        Log.d("Fastrada", "Sent packet: " + arrayStream[0]);

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
