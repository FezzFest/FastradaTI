package be.kdg.FastradaMobile.controllers;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Peter Van Akelyen on 19/02/14.
 */
public class BufferController {
    private static final int PACKET_SIZE = 18;

    private boolean primaryBufferEnabled = true;
    private List<byte[]> primaryBuffer = new ArrayList<byte[]>();
    private List<byte[]> alternateBuffer = new ArrayList<byte[]>();
    private static BufferController instance;
    private static Logger log = Logger.getLogger(BufferController.class.getClass().getName());

    private BufferController() {}

    public static BufferController getInstance() {
        if (instance == null) createInstance();
        return instance;
    }

    private synchronized static void createInstance() {
        if (instance == null) instance = new BufferController();
    }

    public void addPacket(byte[] packet) {
        byte[] newPacket = addTimestamp(packet);
        if (primaryBufferEnabled) {
            primaryBuffer.add(newPacket);
        } else {
            alternateBuffer.add(newPacket);
        }
    }

    public byte[] getPackets() {
        while (true) {
            byte[] result = getDataFromBuffer();
            if (result.length != 0) {
                return result;
            }

            // Sleep
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                log.log(Level.WARNING, e.getMessage(), e);
            }
        }
    }

    public void clear() {
        primaryBuffer.clear();
        alternateBuffer.clear();
    }

    private byte[] addTimestamp(byte[] packet) {
        long timestamp = System.currentTimeMillis();
        byte[] bTimeStamp = ByteBuffer.allocate(8).putLong(timestamp).array();
        byte[] newPacket = new byte[packet.length + bTimeStamp.length];
        System.arraycopy(bTimeStamp, 0, newPacket, 0, bTimeStamp.length);
        System.arraycopy(packet, 0, newPacket, bTimeStamp.length, packet.length);

        return newPacket;
    }

    private byte[] getDataFromBuffer() {
        byte[] result;
        if (primaryBufferEnabled) {
            // Switch to alternative buffer
            primaryBufferEnabled = false;

            // Packets to array
            int counter = 0;
            result = new byte[PACKET_SIZE * primaryBuffer.size()];
            ListIterator<byte[]> it = primaryBuffer.listIterator();
            while (it.hasNext()) {
                byte[] next = it.next();
                for (int i = 0; i < next.length; i++) {
                    result[counter] = next[i];
                    counter++;
                }
            }

            // Clear buffer
            primaryBuffer.clear();
        } else {
            // Switch to primary buffer
            primaryBufferEnabled = true;

            // Packets to array
            int counter = 0;
            result = new byte[PACKET_SIZE * alternateBuffer.size()];
            ListIterator<byte[]> it = alternateBuffer.listIterator();
            while (it.hasNext()) {
                byte[] next = it.next();
                for (int i = 0; i < next.length; i++) {
                    result[counter] = next[i];
                    counter++;
                }
            }

            // Clear buffer
            alternateBuffer.clear();
        }

        return result;
    }
}
