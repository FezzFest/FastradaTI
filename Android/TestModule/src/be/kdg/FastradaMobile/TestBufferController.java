package be.kdg.FastradaMobile;

import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.util.Log;
import be.kdg.FastradaMobile.activities.MainActivity;
import be.kdg.FastradaMobile.controllers.BufferController;
import org.junit.Assert;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.Assert.assertArrayEquals;

/**
 * Created by FezzFest on 19/02/14.
 */
public class TestBufferController extends ActivityUnitTestCase<MainActivity> {
    private MainActivity activity;
    private BufferController buffer;
    private static Logger log = Logger.getLogger(TestBufferController.class.getClass().getName());

    public TestBufferController() {
        super(MainActivity.class);
    }

    protected void setUp() throws Exception {
        super.setUp();
        Intent intent = new Intent(getInstrumentation().getTargetContext(), MainActivity.class);
        startActivity(intent, null, null);
        activity = getActivity();
        buffer = BufferController.getInstance();
    }

    public void testSinglePacket() {
        // Define packet
        byte[] packet = {(byte) 0x01, (byte) 0x02, (byte) 0x03, (byte) 0x04, (byte) 0x05, (byte) 0x06, (byte) 0x07, (byte) 0x08, (byte) 0x09, (byte) 0x10};

        // Add packet to buffer
        buffer.addPacket(packet);

        // Assert
        assertArrayEquals("Packets must be equal", packet, stripTimeStamp(buffer.getPackets()));
    }

    public void testLotsOfPackets() {
        int numerOfPackets = 100;

        // Define packets
        Random random = new Random();
        byte[][] packets = new byte[numerOfPackets][10];
        for (int i = 0; i < numerOfPackets; i++) {
            packets[i] = new byte[10];
            random.nextBytes(packets[i]);

            buffer.addPacket(packets[i]);
        }

        // Convert double array to single array
        int counter = 0;
        byte[] result = new byte[10 * numerOfPackets];
        for (int i = 0; i < numerOfPackets; i++) {
            byte[] packet = packets[i];
            for (byte b : packet) {
                result[counter] = b;
                counter++;
            }
        }

        // Assert
        byte[] bufferBytes = buffer.getPackets();
        assertArrayEquals("Arrays must be equal", result, stripTimeStamp(bufferBytes));
    }

    public void testSwitchingBuffers() throws InterruptedException {
        // Generate packets
        Random random = new Random();
        final byte[][] packets = new byte[100][10];
        for (int i = 0; i < 100; i++) {
            packets[i] = new byte[10];
            random.nextBytes(packets[i]);
        }

        // Send packets
        Thread sendPackets = new Thread() {
            public void run() {
                for (int i = 0; i < 100; i++) {
                    // Add packet
                    buffer.addPacket(packets[i]);

                    // Log
                    Log.d("Fastrada", "Added " + i + " packets");

                    // Sleep
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        log.log(Level.WARNING, e.getMessage(), e);
                    }
                }
            }
        };

        // Get from buffer
        final List<byte[]> bufferList = new ArrayList<byte[]>();
        Thread getPackets = new Thread() {
            public void run() {
                while (true) {
                    // Read packets
                    byte[] bufferBytes = buffer.getPackets();
                    bufferList.add(bufferBytes);

                    // Log
                    Log.d("Fastrada", "Received " + bufferBytes.length / 10 + " packets");

                    // Sleep
                    try {
                        Thread.sleep(30);
                    } catch (InterruptedException e) {
                        log.log(Level.WARNING, e.getMessage(), e);
                    }
                }
            }
        };

        sendPackets.start();
        getPackets.start();

        Thread.sleep(10000);
        byte[] result = convertDoubleToSingle(packets);
        byte[] bufferResult = convertDoubleToSingle(bufferList.toArray(new byte[][]{}));
        assertArrayEquals("Arrays must be equal", result, stripTimeStamp(bufferResult));
    }

    private byte[] convertDoubleToSingle(byte[][] array) {
        int counter = 0;

        // Determine result size
        int size = 0;
        for (int i=0; i<array.length; i++) {
            for (int j=0; j<array[i].length; j++) {
                size++;
            }
        }

        byte[] result = new byte[size];
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[i].length; j++) {
                byte b = array[i][j];
                result[counter] = b;
                counter++;
            }
        }

        return result;
    }

    private byte[] stripTimeStamp(byte[] buffer) {
        int packets = buffer.length / 18;

        byte[] result = new byte[packets * 10];
        for(int i=0;i<packets;i++){
            System.arraycopy(buffer, (i*18)+8, result, i*10, 10);
        }
        return result;
    }
}