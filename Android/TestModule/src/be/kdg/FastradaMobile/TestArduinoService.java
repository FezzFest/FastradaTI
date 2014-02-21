package be.kdg.FastradaMobile;

import android.content.Intent;
import android.test.ServiceTestCase;
import android.util.Log;
import be.kdg.FastradaMobile.controllers.UserInterfaceController;
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
public class TestArduinoService extends ServiceTestCase<ArduinoService> {
    private UserInterfaceController buffer;

    public TestArduinoService() {
        super(ArduinoService.class);
    }

    protected void setUp() throws Exception {
        super.setUp();
        buffer = UserInterfaceController.getInstance();
    }

    public void startArduinoService() throws InterruptedException {
        // Start service
        Intent intent = new Intent(getSystemContext(), ArduinoService.class);
        startService(intent);
        Thread.sleep(5000);
    }

    public void test1ServiceFixedEngineDataPacketToSpeedValue() throws IOException, InterruptedException {
        startArduinoService();
        // Construct packet
        byte[] packet = {(byte) 0x01, (byte) 0x01, (byte) 0xD1, (byte) 0xC6, (byte) 0x01, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF};
        sendUdpPacket(packet, 9000);

        // Wait until package is processed by the service
        Thread.sleep(50);

        // Assert
        assertEquals("Speed must be 295.", 295, buffer.getSpeed());
    }

    public void test2ServiceWithVariable() throws IOException, InterruptedException {
        // Construct packet
        Random random = new Random();
        int randomInt = random.nextInt(255);
        int randomFactor = (int) (randomInt / 0.00549324);

        byte[] randomBytes = intToByteArray(randomFactor);

        // Construct packet
        byte[] packet = {(byte) 0x01, (byte) 0x01, randomBytes[2], randomBytes[3], (byte) 0x02, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF};
        sendUdpPacket(packet, 9000);

        // Wait until package is processed by the service
        Thread.sleep(50);

        // Assert
        assertEquals("Speed must be " + randomInt, randomInt, buffer.getSpeed());
    }

    public void test3TempPacket() throws InterruptedException {

        int sendFactor = (int) ((-30 + 50) / 0.0030518);
        Log.d("Fastrada", "Send randomfactor TEMP " + sendFactor);
        byte[] randomBytes = intToByteArray(sendFactor);
        // Construct packet
        byte[] packet = {(byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, randomBytes[2], randomBytes[3], (byte) 0xFF, (byte) 0xFF};
        sendUdpPacket(packet, 9000);

        // Wait until package is processed by the service
        Thread.sleep(50);

        // Assert
        assertEquals("Temperature must be -30.", -30, Math.round(buffer.getTemperature()));
    }

    public void test4ServiceWithMultiplePackets() throws InterruptedException {
        // Construct packet
        Random random = new Random();
        int randomInts[] = new int[10];
        int receivedInts[] = new int[10];

        for (int i = 0; i < 10; i++) {
            int randomInt = random.nextInt(255);
            randomInts[i] = randomInt;
            int randomFactor = (int) (randomInt / 0.00549324);
            byte[] randomBytes = intToByteArray(randomFactor);

            byte[] packet = {(byte) 0x01, (byte) 0x01, randomBytes[2], randomBytes[3], (byte) 0x03, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF};
            sendUdpPacket(packet, 9000);

            // Wait until package is processed by the service
            Thread.sleep(50);

            UserInterfaceController controller = UserInterfaceController.getInstance();
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

    private byte[] intToByteArray(int a) {
        return new byte[]{
                (byte) ((a >> 24) & 0xFF),
                (byte) ((a >> 16) & 0xFF),
                (byte) ((a >> 8) & 0xFF),
                (byte) (a & 0xFF)
        };
    }
}
