package be.kdg.FastradaMobile.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.test.ActivityUnitTestCase;
import android.util.Log;
import be.kdg.FastradaMobile.services.ArduinoService;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Random;

/**
 * Created by FezzFest on 5/02/14.
 */
public class TestArduinoService extends ActivityUnitTestCase<MainActivity> {
    private Activity activity;
    private Thread thread;

    public TestArduinoService() {
        super(MainActivity.class);
    }

    @Before
    protected void setUp() throws Exception {
        super.setUp();
        Intent intent = new Intent(getInstrumentation().getTargetContext(), MainActivity.class);
        startActivity(intent, null, null);
        activity = getActivity();
    }

    @Test
    public void testServiceWithFixedValue() throws IOException, InterruptedException {
        // Construct packet
        byte [] packet = {(byte)0x14,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF};
        sendUdpPackets(packet, 9000);

        // Start service
        Intent intent = new Intent(activity.getApplicationContext(), ArduinoService.class);
        activity.startService(intent);

        // Give service the time to execute
        Thread.sleep(3000);

        // Check value
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity.getApplicationContext());
        int speed = prefs.getInt("speed", 0);

        // Kill thread
        thread.interrupt();

        assertEquals("Speed must be 20.", 20, speed);
    }

    @Test
    public void testServiceWithVariable() throws IOException, InterruptedException {
        // Construct packet
        Random random = new Random();
        int randomInt = random.nextInt(255);
        byte byte1 = (byte) (Integer.parseInt(Integer.toString(randomInt), 16) & 0xff);

        byte [] packet = {byte1,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF};
        sendUdpPackets(packet, 9000);

        // Start service
        Intent intent = new Intent(activity.getApplicationContext(), ArduinoService.class);
        activity.startService(intent);

        // Give service time to execute
        Thread.sleep(3000);

        // Check value
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity.getApplicationContext());
        int speed = prefs.getInt("speed", 0);

        assertEquals("Speed must be " + randomInt, randomInt, speed);
    }

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
