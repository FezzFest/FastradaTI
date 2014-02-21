package be.kdg.FastradaMobile.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by FezzFest on 19/02/14.
 */
public class BufferController {
    private static BufferController instance;
    private boolean primaryBufferEnabled = true;
    private List<byte[]> primaryBuffer = new ArrayList<byte[]>();
    private List<byte[]> alternateBuffer = new ArrayList<byte[]>();

    private BufferController() {
    }

    public static BufferController getInstance() {
        if (instance == null) createInstance();
        return instance;
    }

    private synchronized static void createInstance() {
        if (instance == null) instance = new BufferController();
    }

    public void addPacket(byte[] packet) {
        if (primaryBufferEnabled) {
            primaryBuffer.add(packet);
        } else {
            alternateBuffer.add(packet);
        }
    }

    public byte[] getPackets() {
        while (true) {
            byte[] result = getBuffer();
            if (result.length != 0) {
                return result;
            }

            // Sleep
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private byte[] getBuffer() {
        byte[] result;
        if (primaryBufferEnabled) {
            // Switch to alternative buffer
            primaryBufferEnabled = false;

            // Packets to array
            int counter = 0;
            result = new byte[10 * primaryBuffer.size()];
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
            result = new byte[10 * alternateBuffer.size()];
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
