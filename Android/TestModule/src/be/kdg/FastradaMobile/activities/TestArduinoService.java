package be.kdg.FastradaMobile.activities;

import android.app.Activity;
import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.util.Log;
import be.kdg.FastradaMobile.controllers.BufferController;
import be.kdg.FastradaMobile.services.ArduinoService;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Random;

import static org.junit.Assert.assertArrayEquals;

/**
 * Created by FezzFest on 5/02/14.
 */
public class TestArduinoService extends ActivityUnitTestCase<MainActivity> {
    private Activity activity;
    private BufferController buffer;

    public TestArduinoService() {
        super(MainActivity.class);
    }

    protected void setUp() throws Exception {
        super.setUp();
        Intent intent = new Intent(getInstrumentation().getTargetContext(), MainActivity.class);
        startActivity(intent, null, null);
        activity = getActivity();
        buffer = BufferController.getInstance();
    }

    public void testServiceWithFixedValue() throws IOException, InterruptedException {
        // Start service
        Intent intent = new Intent(activity.getApplicationContext(), ArduinoService.class);
        activity.startService(intent);
        Thread.sleep(5000);

        // Construct packet
        byte[] packet = {(byte) 0x14, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF};
        sendUdpPacket(packet, 9000);

        // Wait until package is processed by the service
        Thread.sleep(50);

        // Assert
        assertEquals("Speed must be 20.", 20, buffer.getSpeed());
    }

    public void testServiceWithVariable() throws IOException, InterruptedException {
        // Construct packet
        Random random = new Random();
        int randomInt = random.nextInt(255) - 128;
        byte randomByte = (byte) randomInt;

        // Construct packet
        byte[] packet = {randomByte, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF};
        sendUdpPacket(packet, 9000);

        // Wait until package is processed by the service
        Thread.sleep(50);

        // Assert
        assertEquals("Speed must be " + randomInt, randomInt, buffer.getSpeed());
    }

    public void testServiceWithMultiplePackets() throws InterruptedException {
        // Construct packet
        Random random = new Random();
        int randomInts[] = new int[10];
        int receivedInts[] = new int[10];

        for (int i = 0; i < 10; i++) {
            randomInts[i] = random.nextInt(255) - 128;
            byte randomByte = (byte) randomInts[i];

            byte[] packet = {randomByte, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF};
            sendUdpPacket(packet, 9000);

            // Wait until package is processed by the service
            Thread.sleep(50);

            BufferController controller = BufferController.getInstance();
            receivedInts[i] = controller.getSpeed();
        }

        // Assert
        assertArrayEquals("Sent and received arrays of speeds must be the same.", randomInts, receivedInts);
    }

    private void sendUdpPacket(final byte[] packet, final int port) {
        Thread thread = new Thread() {
            public void run() {
                byte[] arrayStream = packet;
                try {
                    DatagramSocket datagramSocket = new DatagramSocket();
                    InetAddress address = InetAddress.getByName("127.0.0.1");
                    DatagramPacket packet = new DatagramPacket(arrayStream, arrayStream.length, address, port);
                    datagramSocket.send(packet);

                    Log.d("Fastrada", "[UDP] Sent packet: " + arrayStream[0]);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }
}
